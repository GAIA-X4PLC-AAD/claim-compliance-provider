package com.msg.ccp.exception;

import lombok.Getter;

@Getter
public class RestClientException extends RuntimeException {
    private final String httpError;
    private final String exceptionText;
    private final int statusCode;
    private String verifiablePresentationId = "unknown";

    public RestClientException(final String message, final String httpError, final String exceptionText, final int statusCode) {
        super(message);
        this.httpError = httpError;
        this.exceptionText = exceptionText;
        this.statusCode = statusCode;
    }

    public RestClientException(final String message, final String httpError, final String exceptionText, final int statusCode, String verifiablePresentationId) {
        this(message, httpError, exceptionText, statusCode);
        this.verifiablePresentationId = verifiablePresentationId;
    }
}
