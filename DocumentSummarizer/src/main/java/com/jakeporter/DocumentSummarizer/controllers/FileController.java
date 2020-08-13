package com.jakeporter.DocumentSummarizer.controllers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakeporter.DocumentSummarizer.domainEntities.SummaryComponents;
import com.jakeporter.DocumentSummarizer.exceptions.JsonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.jakeporter.DocumentSummarizer.service.FileService;


@RestController
@RequestMapping("/summarize")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<String> summarizeFile(@RequestParam("file") MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString;
        fileService.validateFileSize(file);
        String fileUrl = fileService.uploadFile(file);
        SummaryComponents components = fileService.summarize(file, fileUrl);
        try {
            jsonString = objectMapper.writeValueAsString(components);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // need to use custom exception, JsonProcessingException is protected
            throw new JsonException("Something went wrong.");
        }
        return new ResponseEntity<>(jsonString, HttpStatus.OK);
    }

    @PostMapping("/text")
    public ResponseEntity<String> summarizeText(@RequestBody String text) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonString;
        SummaryComponents components = fileService.summarize(text);
        try {
            jsonString = objectMapper.writeValueAsString(components);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonException("Something went wrong.");
        }
        return new ResponseEntity<>(jsonString, HttpStatus.OK);
    }

}
