package com.jakeporter.DocumentSummarizer.service;

import com.jakeporter.DocumentSummarizer.exceptions.FileDeletionException;
import com.jakeporter.DocumentSummarizer.exceptions.GenericFileException;
import com.jakeporter.DocumentSummarizer.utilities.fileUtils.FileInfoGetter;
import com.jakeporter.DocumentSummarizer.utilities.fileUtils.removers.FileRemover;
import com.jakeporter.DocumentSummarizer.utilities.fileUtils.removers.LocalRemover;
import com.jakeporter.DocumentSummarizer.utilities.fileUtils.uploaders.AWSS3Uploader;
import com.jakeporter.DocumentSummarizer.utilities.fileUtils.uploaders.FileUploader;
import com.jakeporter.DocumentSummarizer.utilities.summarizers.DocumentSummarizer;
import com.jakeporter.DocumentSummarizer.utilities.summarizers.PythonSummarizer;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
public class FileService {

    private final static boolean IS_LOCAL = false;
    private final static String BUILD = IS_LOCAL ? "local" : "aws";

    @Value("${upload.dir:${user.home}}")
    private String UPLOAD_DIRECTORY;

    @Autowired
    @Qualifier(BUILD)
    private FileUploader uploader;

    @Autowired
    @Qualifier(BUILD)
    private FileRemover remover;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public void uploadFile(MultipartFile file) {
        uploader.uploadFile(file);
    }

    public Set<String> summarize(MultipartFile file) {
        FileInfoGetter infoGetter = new FileInfoGetter();
        if (!IS_LOCAL) {
            file = getFileFromS3(file);
        }
        FileType fileType = infoGetter.getFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
        Set<String> summaries = null;
        try {
            FileTextExtractor extractor = FileTextExtractorFactory.getExtractor(fileType);
            DocumentSummarizer summarizer = new PythonSummarizer(extractor);
            summaries = summarizer.summarizeDocument(file);
            logger.info("Summary: " + summaries);
        } catch (Exception e) { // Don't know what uncaught exceptions could throw this at the moment, but I want to ensure the deletion of any uploaded files
            throw new GenericFileException("Something went wrong.");
        } finally {
            remover.deleteFile(infoGetter.getFileLocation(file, UPLOAD_DIRECTORY));
        }
        return summaries;
    }

    public Set<String> summarize(String text) {
        DocumentSummarizer summarizer = new PythonSummarizer();
        return summarizer.summarizeDocument(text);
    }

    private MultipartFile getFileFromS3(MultipartFile file) {
        // TODO
        return null;
    }

}
