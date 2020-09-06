package com.jakeporter.DocumentSummarizer.service;

import com.jakeporter.DocumentSummarizer.domainEntities.SummaryComponents;
import com.jakeporter.DocumentSummarizer.exceptions.*;
import com.jakeporter.DocumentSummarizer.utilities.fileUtils.FileInfoGetter;
import com.jakeporter.DocumentSummarizer.utilities.fileUtils.uploaders.AWSS3Client;
import com.jakeporter.DocumentSummarizer.utilities.summarizers.PythonSummarizer;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.*;
import com.jakeporter.DocumentSummarizer.utilities.validators.PythonValidator;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FileService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // had to turn off Spring's spring.servlet.multipart.max-file-size/max-request-size boundaries in application.properties and handle file size limits here (nothing else worked properly)
    public void validateFileSize(MultipartFile file) {
        final String FIVE_MB_IN_BYTES = "5242880";
        final String MB_IN_BYTES = "1048576";
        BigDecimal maxSize = new BigDecimal(FIVE_MB_IN_BYTES);
        BigDecimal mbInBytes = new BigDecimal(MB_IN_BYTES);
        BigDecimal fileSize = new BigDecimal(String.valueOf(file.getSize()));
        if (fileSize.compareTo(maxSize) > 0) {
            throw new FileTooLargeException("File size cannot exceed " + maxSize.divide(mbInBytes, RoundingMode.FLOOR) + " MB. Your file is " + fileSize.divide(mbInBytes).setScale(2, RoundingMode.FLOOR) + " MB.");
        }
    }

    /* There's an ugly chain of exception throws in here- they're specific because I want to send different status codes to the client for each specific problem
       and I have to re-throw them in here for the ExceptionHandlerController to intercept them */
    public SummaryComponents summarize(MultipartFile mpFile, String language) {
        SummaryComponents components = null;
        FileType fileType;
        InputStream inputStream;
        try {
            fileType = new FileInfoGetter().getFileType(FilenameUtils.getExtension(mpFile.getOriginalFilename()));
        } catch (FileException e) {
            throw new FileException(e.getMessage());
        }
        try {
            inputStream = mpFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException("Something went wrong processing the file.");
        }
        try {
            FileTextExtractor extractor = FileTextExtractorFactory.getExtractor(fileType);
            long start = System.currentTimeMillis();
            components = new PythonSummarizer(extractor, new PythonValidator()).summarize(inputStream, language);
            double executionTime = (System.currentTimeMillis() - start) / 1000D;
            logger.info("-- Execution time (sec): " + executionTime);
            logger.info("Summary components: " + components.toString());
        } catch (TextExtractorException e) {
            e.printStackTrace();
            throw new TextExtractorException(e.getMessage());
        } catch (SummaryException e) {
            e.printStackTrace();
            throw new SummaryException(e.getMessage());
        } catch (PythonScriptException e) {
            e.printStackTrace();
            throw new PythonScriptException(e.getMessage());
        } catch (Exception e) { // Don't know what uncaught exceptions could throw this at the moment, but I want to ensure the deletion of any uploaded files
            e.printStackTrace();
        }
        return components;
    }

    public SummaryComponents summarize(String text, String language) {
        SummaryComponents components;
        try {
            components = new PythonSummarizer(new PythonValidator()).summarize(text, language);
        } catch (SummaryException e) {
            e.printStackTrace();
            throw new SummaryException(e.getMessage());
        } catch (PythonScriptException e) {
            e.printStackTrace();
            throw new PythonScriptException(e.getMessage());
        }
        return components;
    }
}
