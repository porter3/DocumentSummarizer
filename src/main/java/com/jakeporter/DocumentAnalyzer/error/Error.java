package com.jakeporter.DocumentAnalyzer.error;

import java.time.LocalDateTime;

/*
 * Used to compose ResponseEntities when exception is thrown/handled
 */

public class Error {

    public Error() {}
    public Error(String message) {
        this.message = message;
    }

    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
