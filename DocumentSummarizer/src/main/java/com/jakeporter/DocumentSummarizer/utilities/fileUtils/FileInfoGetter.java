package com.jakeporter.DocumentSummarizer.utilities.fileUtils;

import com.jakeporter.DocumentSummarizer.exceptions.UnsupportedFileFormatException;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileInfoGetter {

    Logger logger = LoggerFactory.getLogger(FileInfoGetter.class);

    public FileType getFileType(String fileExtension) {
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
                throw new UnsupportedFileFormatException();
        }
    }

    // create absolute path of the file and normalize it for different OSs
    public Path getFileLocation(MultipartFile file, String uploadDirectory) {
        return Paths.get(uploadDirectory + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
    }
}
