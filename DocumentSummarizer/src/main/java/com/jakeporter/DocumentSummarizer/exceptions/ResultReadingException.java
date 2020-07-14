package com.jakeporter.DocumentSummarizer.exceptions;

public class ResultReadingException extends RuntimeException {

    public ResultReadingException(String message) {
        super(message);
    }

    public ResultReadingException(String message, Throwable e) {
        super(message, e);
    }
}
