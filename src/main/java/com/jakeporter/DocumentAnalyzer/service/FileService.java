package com.jakeporter.DocumentAnalyzer.service;

import com.jakeporter.DocumentAnalyzer.exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    // value can be set by com.jakeporter.DocumentAnalyzer.app.upload.dir in application.properties - defaults to user.home if not defined
    @Value("${com.jakeporter.DocumentAnalyzer.app.upload.dir:${user.home}}")
    public String uploadDirectory;

    public void uploadFile(MultipartFile file) throws FileStorageException {

        try {
            // create absolute path of the file and normalize it for different OSs
            Path copyLocation = Paths.get(uploadDirectory + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            // copy the file's input stream to the path and replace any file with the same name
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("Error storing " + file.getOriginalFilename() + ".");
        }
    }
}
