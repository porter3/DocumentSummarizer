package com.jakeporter.DocumentSummarizer.service;

import com.jakeporter.DocumentSummarizer.exceptions.*;
import com.jakeporter.DocumentSummarizer.utilities.fileUtils.FileInfoGetter;
import com.jakeporter.DocumentSummarizer.utilities.fileUtils.uploaders.AWSS3Client;
import com.jakeporter.DocumentSummarizer.utilities.summarizers.DocumentSummarizer;
import com.jakeporter.DocumentSummarizer.utilities.summarizers.PythonSummarizer;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Service
public class FileService {

    @Autowired
    private AWSS3Client awsClient;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // had to turn off Spring's spring.servlet.multipart.max-file-size/max-request-size boundaries in application.properties and handle file size limits here (nothing else worked properly)
    public void validateFileSize(MultipartFile file) {
        // maxSize is exactly 3 MB
        BigDecimal maxSize = new BigDecimal("3145728");
        BigDecimal mbInBytes = new BigDecimal("1048576");
        BigDecimal fileSize = new BigDecimal(String.valueOf(file.getSize()));
        if (fileSize.compareTo(maxSize) == 1) {
            throw new FileTooLargeException("File size cannot exceed " + maxSize.divide(mbInBytes, RoundingMode.FLOOR) + " MB. Your file is " + fileSize.divide(mbInBytes).setScale(2, RoundingMode.FLOOR) + " MB.");
        }
    }

    public String uploadFile(MultipartFile file) {
        String fileUrl;
        try {
            // this is just here to throw an exception early on if the file format is unsupported
            new FileInfoGetter().getFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
        } catch (UnsupportedFileFormatException e) {
            e.printStackTrace();
            throw new UnsupportedFileFormatException(e.getMessage());
        }
        try {
            fileUrl = awsClient.uploadFile(file);
        } catch (FileUploaderException e) {
            e.printStackTrace();
            throw new FileUploaderException(e.getMessage());
        }
        return fileUrl;
    }

    /* There's an ugly chain of exception throws in here- they're specific because I want to send different status codes to the client for each specific problem
       and I have to re-throw them in here for the ExceptionHandlerController to intercept them */
    public Set<String> summarize(MultipartFile mpFile, String fileUrl) {
        FileType fileType = new FileInfoGetter().getFileType(FilenameUtils.getExtension(mpFile.getOriginalFilename()));
        InputStream s3ObjectStream;
        try {
            s3ObjectStream = awsClient.getS3ObjectInputStream(fileUrl);
        } catch (FileUploaderException e) {
            throw new FileUploaderException(e.getMessage());
        }
        Set<String> summaries = null;
        try {
            FileTextExtractor extractor = FileTextExtractorFactory.getExtractor(fileType);
            DocumentSummarizer summarizer = new PythonSummarizer(extractor);
            summaries = summarizer.summarize(s3ObjectStream);
            logger.info("Summary: " + summaries);
        } catch (TextExtractorException e) {
            e.printStackTrace();
            throw new TextExtractorException(e.getMessage());
        } catch (SummaryException e) { // Don't know what uncaught exceptions could throw this at the moment, but I want to ensure the deletion of any uploaded files
            e.printStackTrace();
            throw new SummaryException(e.getMessage());
        } catch (PythonScriptException e) {
            e.printStackTrace();
            throw new PythonScriptException(e.getMessage());
        } finally {
            awsClient.deleteFileFromS3Bucket(fileUrl);
        }
        return summaries;
    }

    public Set<String> summarize(String text) {
        DocumentSummarizer summarizer = new PythonSummarizer();
        return summarizer.summarizeDocument(text);
    }
}
