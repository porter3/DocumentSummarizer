package com.jakeporter.DocumentSummarizer.exceptions;

public class FileUploaderException extends RuntimeException {

    public FileUploaderException(String message) {
        super(message);
    }

    public FileUploaderException(String message, Throwable e) {
        super(message, e);
    }
}
