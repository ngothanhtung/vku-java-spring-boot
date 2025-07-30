package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum StudentStatus {
    ACTIVE("ACT"), INACTIVE("INA"), SUSPENDED("SUS");

    private final String code;

    StudentStatus(String code) {
        this.code = code;
    }

    public static StudentStatus fromCode(String code) {
        for (StudentStatus s : StudentStatus.values()) {
            if (s.code.equals(code))
                return s;
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}