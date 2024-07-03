package com.msg.ccp.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * exception mapper handling all ValidationExceptions and returning an INTERNAL_SERVER_ERROR.
 */
@Slf4j
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    /**
     * Maps an exception to a response.
     * @param exception the exception to map.
     * @return the response.
     */
    @Override
    public Response toResponse(final ValidationException exception) {
        log.error("An ValidationException occurred", exception);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(exception.toString(), "Bad Request", exception.getMessage(), Response.Status.BAD_REQUEST.getStatusCode(), "unknown"))
                .build();
    }
}