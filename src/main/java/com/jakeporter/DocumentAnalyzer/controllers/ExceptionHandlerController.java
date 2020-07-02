package com.jakeporter.DocumentAnalyzer.controllers;

import com.jakeporter.DocumentAnalyzer.error.Error;
import com.jakeporter.DocumentAnalyzer.exceptions.ProblematicTextException;
import com.jakeporter.DocumentAnalyzer.exceptions.TextTooShortException;
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
        return new ResponseEntity<>(new Error("File size cannot exceed 1MB."), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(TextTooShortException.class)
    public ResponseEntity<Error> handleTextTooShort(TextTooShortException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // for more generic text-based errors that cause issues in the Python script
    @ExceptionHandler(ProblematicTextException.class)
    public ResponseEntity<Error> handleProblematicText(ProblematicTextException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
