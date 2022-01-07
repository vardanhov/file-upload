package com.example.uploadfile.domain.enums;

public enum UserProfileType {

    ADMIN(1, "ROLE_ADMIN"),

    USER(2, "ROLE_USER"),

    VIEWER(3, "ROLE_VIEWER"),

    AUDITOR(4, "ROLE_AUDITOR");


    private int value;

    private String name;


    UserProfileType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static UserProfileType ofValue(final int value) {
        for (UserProfileType item : UserProfileType.values()) {
            if (item.getValue() == value) {
                return item;
            }
        }

        return null;
    }

    public static UserProfileType ofName(final String name) {
        for (UserProfileType item : UserProfileType.values()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }

        return null;
    }


    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
