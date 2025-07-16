package com.ms.email.enums;

public enum StatusEmail {
    PENDING(0, "S"),
    ERROR(1, "N"),
    SENT(2, "T");


    private final int code;
    private final String description;

    StatusEmail(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
