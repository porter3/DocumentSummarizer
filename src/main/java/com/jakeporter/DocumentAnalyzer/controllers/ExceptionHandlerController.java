package com.jakeporter.DocumentAnalyzer.controllers;

import com.jakeporter.DocumentAnalyzer.error.Error;
import com.jakeporter.DocumentAnalyzer.exceptions.ProblematicTextException;
import com.jakeporter.DocumentAnalyzer.exceptions.TextTooShortException;
import com.jakeporter.DocumentAnalyzer.exceptions.UnsupportedFileFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;


@ControllerAdvice
@RestController
public class ExceptionHandlerController {

    // thrown by Spring
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Error> handleFileSizeLimitExceeded(MaxUploadSizeExceededException e) {
        return new ResponseEntity<>(new Error("File size cannot exceed 1MB."), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    // thrown by Spring first for /file endpoint and is the only exception thrown for /text endpoint
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Error> handleNoBody(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(new Error("You must upload a document or text to generate a summary."), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // thrown by Spring after HttpMessageNotReadableException via /file endpoint
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Error> handleNoRequestPart(MissingServletRequestPartException e) {
        return new ResponseEntity<>(new Error("You must upload a document or text to generate a summary."), HttpStatus.UNPROCESSABLE_ENTITY);
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

    @ExceptionHandler(UnsupportedFileFormatException.class)
    public ResponseEntity<Error> handleUnsupportedFileFormat(UnsupportedFileFormatException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
