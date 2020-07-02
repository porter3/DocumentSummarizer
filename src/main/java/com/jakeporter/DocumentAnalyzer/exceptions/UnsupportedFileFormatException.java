package com.jakeporter.DocumentAnalyzer.exceptions;

public class UnsupportedFileFormatException extends RuntimeException{

    public UnsupportedFileFormatException(String message) {
        super(message);
    }

    public UnsupportedFileFormatException(String message, Throwable e) {
        super(message, e);
    }
}
