package com.msg.ccp.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * Generic exception mapper handling all exceptions and returning an INTERNAL_SERVER_ERROR.
 */
@Slf4j
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    /**
     * Maps an exception to a response.
     * @param exception the exception to map.
     * @return the response.
     */
    @Override
    public Response toResponse(final Exception exception) {
        log.error("An error occurred", exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(exception.toString(), "Internal Server Error", exception.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "unknown"))
                .build();
    }
}