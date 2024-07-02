package com.msg.ccp.exception;

/**
 * Exception class for CCP.
 */
public class CcpException extends RuntimeException{
    /**
     * Constructor for CcpException.
     * @param message the message of the error.
     * @param cause the cause of the error.
     */
    public CcpException(final String message, final Exception cause) {
        super(message, cause);
    }

    /**
     * Constructor for CcpException.
     * @param message the message of the error.
     */
    public CcpException(final String message) {
        super(message);
    }
}
