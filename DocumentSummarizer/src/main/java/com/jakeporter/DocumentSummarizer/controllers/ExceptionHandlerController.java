package com.jakeporter.DocumentSummarizer.controllers;

import com.jakeporter.DocumentSummarizer.error.Error;
import com.jakeporter.DocumentSummarizer.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;


@ControllerAdvice
public class ExceptionHandlerController {

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

    @ExceptionHandler(SummaryException.class)
    public ResponseEntity<Error> handleSummaryException(SummaryException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PythonScriptException.class)
    public ResponseEntity<Error> handlePythonScriptIssue(PythonScriptException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileTooLargeException.class)
    public ResponseEntity<Error> handleFileTooLarge(FileTooLargeException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(UnsupportedFileFormatException.class)
    public ResponseEntity<Error> handleUnsupportedFileFormat(UnsupportedFileFormatException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TextExtractorException.class)
    public ResponseEntity<Error> handleTextExtractorIssues(TextExtractorException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileUploaderException.class)
    public ResponseEntity<Error> handleFileUploadIssue(FileUploaderException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JsonException.class)
    public ResponseEntity<Error> handleJsonIssue(JsonException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}