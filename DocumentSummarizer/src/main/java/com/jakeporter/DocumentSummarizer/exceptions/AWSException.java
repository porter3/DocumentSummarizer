package com.jakeporter.DocumentSummarizer.exceptions;

public class AWSException extends RuntimeException {

    public AWSException(String message) {
        super(message);
    }

    public AWSException(String message, Throwable e) {
        super(message, e);
    }
}
