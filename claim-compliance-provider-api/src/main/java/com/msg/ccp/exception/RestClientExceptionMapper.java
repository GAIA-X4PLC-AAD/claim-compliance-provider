package com.msg.ccp.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class RestClientExceptionMapper implements ExceptionMapper<RestClientException> {
    @Override
    public Response toResponse(final RestClientException exception) {
        log.error("A RestClientException occurred", exception);
        return Response.status(exception.getStatusCode())
                .entity(new ErrorResponse(exception.getMessage(), exception.getHttpError(), exception.getExceptionText(), exception.getStatusCode()))
                .build();
    }
}
