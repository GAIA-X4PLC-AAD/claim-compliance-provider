package com.msg.ccp.exception;

public class CcpException extends RuntimeException{
    public CcpException(final String message, final Exception cause) {
        super(message, cause);
    }

    public CcpException(final String message) {
        super(message);
    }
}
