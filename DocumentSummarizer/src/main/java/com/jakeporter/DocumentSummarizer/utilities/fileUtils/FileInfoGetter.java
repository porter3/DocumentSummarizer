package com.jakeporter.DocumentSummarizer.utilities.fileUtils;

import com.jakeporter.DocumentSummarizer.exceptions.UnsupportedFileFormatException;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileInfoGetter {

    Logger logger = LoggerFactory.getLogger(FileInfoGetter.class);

    public FileType getFileType(String fileExtension) {
        final String exceptionMessage = "Unsupported file format: ." + fileExtension;
        switch (fileExtension.toLowerCase()) {
            case "doc":
                return FileType.DOC;
            case "docx":
                return FileType.DOCX;
            case "pdf":
                return FileType.PDF;
            case "txt":
                return FileType.TXT;
            default:
                logger.error(exceptionMessage, UnsupportedFileFormatException.class);
                throw new UnsupportedFileFormatException(exceptionMessage);
        }
    }

    // create absolute path of the file and normalize it for different OSs
    public Path getFileLocation(MultipartFile file, String uploadDirectory) {
        return Paths.get(uploadDirectory + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
    }
}
