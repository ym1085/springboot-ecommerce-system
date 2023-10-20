package com.shoppingmall.constant;

import java.util.Arrays;

public enum FileExtension {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    GIF("gif"),
    PDF("pdf"),
    MP4("mp4")
    ;

    private final String extension;

    FileExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static boolean isAcceptFileExtension(String ext) {
        return Arrays.stream(FileExtension.values())
                .map(FileExtension::getExtension)
                .anyMatch(e -> e.equalsIgnoreCase(ext));
    }
}
