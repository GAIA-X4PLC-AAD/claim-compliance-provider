package com.msg.ccp.exception;

/**
 * ErrorResponse class returned by the controller.
 * @param message the message of the error.
 * @param errorDetails details of the error if available.
 * @param exceptionMessage the exception message.
 * @param statuscode the HTTP status code of the error.
 * @param verifiablePresentationId the verifiable presentation id if available.
 */
public record ErrorResponse(String message, String errorDetails, String exceptionMessage, int statuscode, String verifiablePresentationId) {

}
