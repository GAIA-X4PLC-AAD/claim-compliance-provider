package com.msg.ccp.exception;

/**
 * Exception class for validation errors in CCP.
 */
public class ValidationException extends RuntimeException{

    /**
     * Constructor for ValidationException.
     * @param message the message of the error.
     */
    public ValidationException(final String message) {
        super(message);
    }
}
