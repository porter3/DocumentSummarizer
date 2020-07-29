package com.jakeporter.DocumentSummarizer.exceptions;

public class TextExtractorException extends RuntimeException {

    public TextExtractorException(String message) {
        super(message);
    }

    public TextExtractorException(String message, Throwable e) {
        super(message, e);
    }
}
