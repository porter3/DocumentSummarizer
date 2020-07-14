package com.jakeporter.DocumentSummarizer.exceptions;

public class DOCIssueException extends RuntimeException {

    public DOCIssueException(String message) {
        super(message);
    }

    public DOCIssueException(String message, Throwable e) {
        super(message, e);
    }
}
