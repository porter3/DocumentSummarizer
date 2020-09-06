package com.jakeporter.DocumentSummarizer.exceptions;

public class FileException extends RuntimeException {

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable e) {
        super(message, e);
    }
}
