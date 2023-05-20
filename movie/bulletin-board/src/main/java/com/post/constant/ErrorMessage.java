package com.post.constant;

public enum ErrorMessage {
    INVALID_ARGUMENTS_REQUEST("Invalid request. please try again."),
    INVALID_FILE_EXTENSION("Invalid extension. please try again.")
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
