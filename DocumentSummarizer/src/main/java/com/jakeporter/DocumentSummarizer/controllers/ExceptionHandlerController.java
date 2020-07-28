package com.jakeporter.DocumentSummarizer.controllers;

import com.jakeporter.DocumentSummarizer.error.Error;
import com.jakeporter.DocumentSummarizer.exceptions.*;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.File;


@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(FileTooLargeException.class)
    public ResponseEntity<Error> handleFileTooLarge(FileTooLargeException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.PAYLOAD_TOO_LARGE);
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

    @ExceptionHandler(TextTooLongException.class)
    public ResponseEntity<Error> handleTextTooLong(TextTooLongException e) {
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

    @ExceptionHandler(DOCXIssueException.class)
    public ResponseEntity<Error> handleDocxIssues(DOCXIssueException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DOCIssueException.class)
    public ResponseEntity<Error> handleDocIssues(DOCIssueException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PDFIssueException.class)
    public ResponseEntity<Error> handlePDFIssues(PDFIssueException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TXTIssueException.class)
    public ResponseEntity<Error> handleTxtIssues(TXTIssueException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PythonScriptException.class)
    public ResponseEntity<Error> handlePythonScriptIssue(PythonScriptException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WriterException.class)
    public ResponseEntity<Error> handleWritingToStdOutIssue(WriterException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResultReadingException.class)
    public ResponseEntity<Error> handleResultReadingIssue(ResultReadingException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GenericFileException.class)
    public ResponseEntity<Error> handleGenericFileException(GenericFileException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileUploaderException.class)
    public ResponseEntity<Error> handleFileUploadIssue(FileUploaderException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AWSException.class)
    public ResponseEntity<Error> handleAWSIssue(AWSException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}