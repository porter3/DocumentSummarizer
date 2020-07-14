package com.jakeporter.DocumentAnalyzer.exceptions;

public class DOCXIssueException extends RuntimeException {

    public DOCXIssueException(String message) {
        super(message);
    }

    public DOCXIssueException(String message, Throwable e) {
        super(message, e);
    }
}
