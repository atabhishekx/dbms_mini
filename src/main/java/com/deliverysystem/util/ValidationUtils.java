package com.deliverysystem.util;

import java.util.regex.Pattern;

/**
 * Validation utility class.
 * Provides validation methods for input data.
 */
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[0-9]{10,15}$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[A-Za-z0-9_-]{3,20}$"
    );

    /**
     * Validate email
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate phone number
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validate username
     */
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * Validate password
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Validate not empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validate positive number
     */
    public static boolean isPositive(Number number) {
        return number != null && number.doubleValue() > 0;
    }

    /**
     * Validate positive integer
     */
    public static boolean isPositive(int number) {
        return number > 0;
    }
}