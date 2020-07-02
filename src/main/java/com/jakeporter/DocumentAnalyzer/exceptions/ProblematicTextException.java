package com.jakeporter.DocumentAnalyzer.exceptions;

// for more generic errors that cause issues in the Python script
public class ProblematicTextException extends RuntimeException {

    public ProblematicTextException(String message) {
        super(message);
    }

    public ProblematicTextException(String message, Throwable e) {
        super(message, e);
    }
}
