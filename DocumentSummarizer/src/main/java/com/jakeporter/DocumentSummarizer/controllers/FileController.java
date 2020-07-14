package com.jakeporter.DocumentSummarizer.controllers;

import com.jakeporter.DocumentSummarizer.exceptions.FileStorageException;
import com.jakeporter.DocumentSummarizer.exceptions.ProblematicTextException;
import com.jakeporter.DocumentSummarizer.utilities.textExtractors.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.jakeporter.DocumentSummarizer.service.FileService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/summarize")
public class FileController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // JSON key for responses
    private static final String RESPONSE_KEY = "summaries";

    @Autowired
    FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<Map<String, List<String>>> summarizeFile(@RequestParam("file") MultipartFile file) {
        Map<String, List<String>> response = new HashMap();
        FileType fileType = fileService.uploadFile(file);
        List<String> summaries = fileService.summarize(file, fileType);
        response.put(RESPONSE_KEY, summaries);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/text")
    public ResponseEntity<Map<String, List<String>>> summarizeText(@RequestBody String text) {
        Map<String, List<String>> response = new HashMap();
        List<String> summaries = fileService.summarize(text);
        response.put(RESPONSE_KEY, summaries);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
