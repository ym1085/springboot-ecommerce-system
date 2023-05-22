package com.post.constant;

public enum ErrorMessage {
    NONE_EXIST_ID("Id doesn't exist. please try again."),
    INVALID_REQUEST_PARAMETER("Invalid request. Please check your input and try again."),
    INVALID_FILE_EXTENSION("Disallowed file extensions. Please try again.")
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
