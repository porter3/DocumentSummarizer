package com.jakeporter.DocumentSummarizer.exceptions;

public class GenericFileException extends RuntimeException {

    public GenericFileException(String message) {
        super(message);
    }

    public GenericFileException(String message, Throwable e) {
        super(message, e);
    }
}
