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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/summarize")
public class FileController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // JSON keys for responses
    private final String SUCCESS_KEY = "summary";
    private final String ERROR_KEY = "error";

    @Autowired
    FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<Map<String, String>> summarizeFile(@RequestParam("file") MultipartFile file) {
        String summary;
        Map<String, String> response = new HashMap();

        try {
            fileService.uploadFile(file);
        } catch (FileStorageException e) {
            response.put(ERROR_KEY, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            summary = fileService.summarize(file);
            logger.info("Summary: " + summary);
        } catch (IOException e) {
            response.put(ERROR_KEY, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(SUCCESS_KEY, summary);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/text")
    public ResponseEntity<Map<String, String>> summarizeText(@RequestBody String text) {
        String summary;
        Map<String, String> response = new HashMap();
        try {
            summary = fileService.summarize(text);
        } catch (IOException e) {
            response.put(ERROR_KEY, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put(SUCCESS_KEY, summary);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
