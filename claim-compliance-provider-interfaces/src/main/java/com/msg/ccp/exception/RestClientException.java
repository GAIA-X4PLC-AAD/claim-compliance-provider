package com.msg.ccp.exception;

import lombok.Getter;

/**
 * Exception class for RestClientException. This is thrown when an error occurs in the REST client.
 */
@Getter
public class RestClientException extends RuntimeException {
    private final String errorDetails;
    private final String exceptionText;
    private final int statusCode;
    private String verifiablePresentationId = "unknown";

    /**
     * Constructor for RestClientException.
     * @param message the message of the error.
     * @param errorDetails details of the error if available.
     * @param exceptionText the exception message.
     * @param statusCode the HTTP status code of the error.
     */
    public RestClientException(final String message, final String errorDetails, final String exceptionText, final int statusCode) {
        super(message);
        this.errorDetails = errorDetails;
        this.exceptionText = exceptionText;
        this.statusCode = statusCode;
    }

    /**
     * Constructor for RestClientException.
     * @param message the message of the error.
     * @param errorDetails details of the error if available.
     * @param exceptionText the exception message.
     * @param statusCode the HTTP status code of the error.
     * @param verifiablePresentationId the verifiable presentation id if available.
     */
    public RestClientException(final String message, final String errorDetails, final String exceptionText, final int statusCode, final String verifiablePresentationId) {
        this(message, errorDetails, exceptionText, statusCode);
        this.verifiablePresentationId = verifiablePresentationId;
    }
}
