package com.jakeporter.DocumentSummarizer.service;

import com.jakeporter.DocumentSummarizer.exceptions.GenericFileException;
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
import java.util.Set;

@Service
public class FileService {

    @Autowired
    private AWSS3Client awsClient;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public String uploadFile(MultipartFile file) {
        // only here to throw an exception and prevent upload if file type is unsupported
        new FileInfoGetter().getFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
        return awsClient.uploadFile(file);
    }

    public Set<String> summarize(MultipartFile mpFile, String fileUrl) {
        FileType fileType = new FileInfoGetter().getFileType(FilenameUtils.getExtension(mpFile.getOriginalFilename()));
        InputStream s3ObjectStream = awsClient.getS3ObjectInputStream(fileUrl);
        Set<String> summaries = null;
        try {
            FileTextExtractor extractor = FileTextExtractorFactory.getExtractor(fileType);
            DocumentSummarizer summarizer = new PythonSummarizer(extractor);
            summaries = summarizer.summarize(s3ObjectStream);
            logger.info("Summary: " + summaries);
        } catch (Exception e) { // Don't know what uncaught exceptions could throw this at the moment, but I want to ensure the deletion of any uploaded files
            e.printStackTrace();
            throw new GenericFileException("Something went wrong.");
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
