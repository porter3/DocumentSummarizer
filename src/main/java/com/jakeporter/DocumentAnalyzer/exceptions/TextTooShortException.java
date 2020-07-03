package com.jakeporter.DocumentAnalyzer.exceptions;

public class TextTooShortException extends RuntimeException {

    public TextTooShortException(String message) {
        super(message);
    }

    public TextTooShortException(String message, Throwable e) {
        super(message, e);
    }
}