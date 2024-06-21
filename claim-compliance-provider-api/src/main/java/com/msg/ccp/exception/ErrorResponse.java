package com.msg.ccp.exception;

public record ErrorResponse(String message, String httpError, String exceptionMessage, int statuscode) {

}
