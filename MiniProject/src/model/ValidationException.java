package model;

/**
 * Custom exception class for student data validation failures.
 * Demonstrates clean exception handling and encapsulation in OOP.
 */
public class ValidationException extends Exception {
    
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

