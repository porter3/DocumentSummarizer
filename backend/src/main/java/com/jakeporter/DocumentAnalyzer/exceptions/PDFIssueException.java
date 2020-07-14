package com.jakeporter.DocumentAnalyzer.exceptions;

public class PDFIssueException extends RuntimeException {

    public PDFIssueException(String message) {
        super(message);
    }

    public PDFIssueException(String message, Throwable e) {
        super(message, e);
    }
}
