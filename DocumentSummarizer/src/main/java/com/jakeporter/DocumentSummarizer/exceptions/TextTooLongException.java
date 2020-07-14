package com.jakeporter.DocumentSummarizer.exceptions;

public class TextTooLongException extends RuntimeException {

    public TextTooLongException(String message) {
        super(message);
    }

    public TextTooLongException(String message, Throwable e) {
        super(message, e);
    }
}
