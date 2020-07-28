package com.jakeporter.DocumentSummarizer.exceptions;

public class FileTooLargeException extends RuntimeException {

    public FileTooLargeException(String message) {
        super(message);
    }

    public FileTooLargeException(String message, Throwable e) {
        super(message, e);
    }
}
