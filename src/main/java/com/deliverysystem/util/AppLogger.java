package com.deliverysystem.util;

/**
 * Application logger utility.
 * Provides logging functionality for the application.
 */
public class AppLogger {
    private static final boolean DEBUG_MODE = true;
    
    /**
     * Log info message
     */
    public static void info(String message) {
        if (DEBUG_MODE) {
            System.out.println("[INFO] " + System.currentTimeMillis() + ": " + message);
        }
    }
    
    /**
     * Log error message
     */
    public static void error(String message) {
        System.err.println("[ERROR] " + System.currentTimeMillis() + ": " + message);
    }
    
    /**
     * Log error with exception
     */
    public static void error(String message, Exception e) {
        System.err.println("[ERROR] " + System.currentTimeMillis() + ": " + message);
        e.printStackTrace();
    }
    
    /**
     * Log debug message
     */
    public static void debug(String message) {
        if (DEBUG_MODE) {
            System.out.println("[DEBUG] " + System.currentTimeMillis() + ": " + message);
        }
    }
}