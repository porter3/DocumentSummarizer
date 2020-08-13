package com.jakeporter.DocumentSummarizer.exceptions;

public class JsonException extends RuntimeException {

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Throwable e) {
        super(message, e);
    }
}
