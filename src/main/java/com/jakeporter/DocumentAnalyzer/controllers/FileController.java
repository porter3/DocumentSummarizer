package com.jakeporter.DocumentAnalyzer.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.jakeporter.DocumentAnalyzer.service.FileService;

@Controller
public class FileController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FileService fileService;

    // File will be removed from memory after request has been processed
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        try{
            fileService.uploadFile(file);
        } catch (Exception e) {
            return new ResponseEntity<>("Error " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("File uploaded");
        return new ResponseEntity<>("Success!", HttpStatus.OK);
    }
}
