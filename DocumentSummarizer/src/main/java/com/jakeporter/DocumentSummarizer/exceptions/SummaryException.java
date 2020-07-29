package com.jakeporter.DocumentSummarizer.exceptions;

public class SummaryException extends RuntimeException {

    public SummaryException(String message) {
        super(message);
    }

    public SummaryException(String message, Throwable e) {
        super(message, e);
    }
}
