package com.msg.ccp.exception;

public class CcpException extends RuntimeException{
    public CcpException(final String message, Exception cause) {
        super(message, cause);
    }
}
