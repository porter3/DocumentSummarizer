package com.jakeporter.DocumentAnalyzer.controllers;

import com.jakeporter.DocumentAnalyzer.exceptions.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.jakeporter.DocumentAnalyzer.service.FileService;
import java.io.IOException;

@RestController
public class FileController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FileService fileService;

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String summary;

        try{
            fileService.uploadFile(file);
        } catch (FileStorageException e) {
            return new ResponseEntity<>("error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try{
            summary = fileService.summarize(file);
            logger.info("Summary: " + summary);
        } catch (IOException e) {
            return new ResponseEntity<>("error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }
}
