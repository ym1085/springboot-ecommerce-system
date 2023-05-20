package com.post.constant;

public enum FileExtension {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    ;

    private final String extension;

    FileExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
