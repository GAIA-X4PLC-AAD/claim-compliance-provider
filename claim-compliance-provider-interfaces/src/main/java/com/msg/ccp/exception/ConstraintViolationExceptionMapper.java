package com.msg.ccp.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * Exception mapper for RestClientException.
 */
@Slf4j
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    /**
     * Maps a ConstraintViolationExceptionMapper to a response. This exception is triggered when a constraint
     * (e.g. @NotNull) is violated.
     * @param exception the exception to map.
     * @return the response.
     */
    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        log.error("A ConstraintViolationException occurred", exception);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("Constraint violation in interface", exception.toString(), exception.getMessage(), Response.Status.BAD_REQUEST.getStatusCode(), "unknown"))
                .build();
    }
}
