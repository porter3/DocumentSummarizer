package com.jakeporter.DocumentAnalyzer.controllers;

import com.jakeporter.DocumentAnalyzer.error.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@RestController
public class ExceptionHandlerController {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Error> handleFileSizeLimitExceeded(MaxUploadSizeExceededException e) {
        return new ResponseEntity<>(new Error("File size cannot exceed 1MB"), HttpStatus.PAYLOAD_TOO_LARGE);
    }
}
