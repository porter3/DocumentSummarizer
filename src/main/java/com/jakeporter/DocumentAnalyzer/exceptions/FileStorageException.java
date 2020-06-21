package com.jakeporter.DocumentAnalyzer.exceptions;

public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable e) {
        super(message, e);
    }
}
