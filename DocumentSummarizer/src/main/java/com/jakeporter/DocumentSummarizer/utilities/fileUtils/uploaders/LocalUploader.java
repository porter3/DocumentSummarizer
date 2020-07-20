package com.jakeporter.DocumentSummarizer.utilities.fileUtils.uploaders;

import com.jakeporter.DocumentSummarizer.exceptions.FileStorageException;
import com.jakeporter.DocumentSummarizer.exceptions.UnsupportedFileFormatException;
import com.jakeporter.DocumentSummarizer.utilities.fileUtils.FileInfoGetter;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.FileType;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component("localUploader")
public class LocalUploader implements FileUploader {

    @Value("${upload.dir:${user.home}}")
    String uploadDirectory;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String uploadFile(MultipartFile file) {
        FileType fileType;
        Path copyLocation;
        FileInfoGetter infoGetter = new FileInfoGetter();
        try {
            fileType = infoGetter.getFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
            logger.info("File type: " + fileType);
            logger.info("Upload directory: " + uploadDirectory);
            copyLocation = infoGetter.getFileLocation(file, uploadDirectory);
            // copy the file's input stream to the path and replace any file with the same name
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Error storing " + file.getOriginalFilename() + ".");
        } catch (UnsupportedFileFormatException e) {
            throw new UnsupportedFileFormatException("File format not supported.");
        }
        return copyLocation.toString();
    }
}
