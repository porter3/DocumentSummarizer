package com.jakeporter.DocumentAnalyzer.exceptions;

public class FileDeletionException extends RuntimeException {

    public FileDeletionException(String message) {
        super(message);
    }

    public FileDeletionException(String message, Throwable e) {
        super(message, e);
    }
}
