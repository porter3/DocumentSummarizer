package com.jakeporter.DocumentAnalyzer.controllers;

import com.jakeporter.DocumentAnalyzer.exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jakeporter.DocumentAnalyzer.service.FileService;

@Controller
public class FileController {

    @Autowired
    FileService fileService;

    @GetMapping("/")
    public ResponseEntity<String> test() {
        System.out.println("This is being accessed!!!!!!!!!!");
        return new ResponseEntity<>("Hi", HttpStatus.OK);
    }

    // File will be removed from memory after request has been processed
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        try{
            fileService.uploadFile(file);
        } catch (Exception e) {
            return new ResponseEntity<>("Error " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("File uploaded");
        return new ResponseEntity<>("Sucess!", HttpStatus.OK);
    }
}
