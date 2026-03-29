package com.minmvp.common;

public class Constants {

    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_MANAGER = "manager";
    public static final String ROLE_USER = "user";

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_DISABLED = 0;

    public static final Integer NOT_DELETED = 0;
    public static final Integer DELETED = 1;

    private Constants() {
        // prevent instantiation
    }
}
