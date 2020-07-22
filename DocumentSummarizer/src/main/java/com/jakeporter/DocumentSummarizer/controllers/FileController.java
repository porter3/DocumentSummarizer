package com.jakeporter.DocumentSummarizer.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.jakeporter.DocumentSummarizer.service.FileService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/summarize")
public class FileController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // JSON key for responses
    private static final String RESPONSE_KEY = "summaries";

    @Autowired
    FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<Map<String, Set<String>>> summarizeFile(@RequestParam("file") MultipartFile file) {
        Map<String, Set<String>> response = new HashMap();
        String fileUrl = fileService.uploadFile(file);
        Set<String> summaries = fileService.summarize(file, fileUrl);
        response.put(RESPONSE_KEY, summaries);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/text")
    public ResponseEntity<Map<String, Set<String>>> summarizeText(@RequestBody String text) {
        Map<String, Set<String>> response = new HashMap();
        Set<String> summaries = fileService.summarize(text);
        response.put(RESPONSE_KEY, summaries);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
