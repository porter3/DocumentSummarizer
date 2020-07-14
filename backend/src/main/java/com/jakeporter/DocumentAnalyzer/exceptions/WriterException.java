package com.jakeporter.DocumentAnalyzer.exceptions;

public class WriterException extends RuntimeException {

    public WriterException(String message) {
        super(message);
    }

    public WriterException(String message, Throwable e) {
        super(message, e);
    }
}
