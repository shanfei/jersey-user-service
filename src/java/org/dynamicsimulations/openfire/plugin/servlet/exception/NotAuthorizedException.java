package org.dynamicsimulations.openfire.plugin.servlet.exception;

import org.dynamicsimulations.openfire.plugin.servlet.response.ErrorResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NotAuthorizedException extends WebApplicationException {

    public NotAuthorizedException(final Exception e) {
        super(Response.status(401)
                .entity(new ErrorResponse(e))
                .type(MediaType.APPLICATION_JSON)
                .build());
    }

    public NotAuthorizedException() {
        super(Response.status(401)
                .entity(new ErrorResponse(NotAuthorizedException.class, "Please provide a valid authorization token"))
                .type(MediaType.APPLICATION_JSON)
                .build());
    }

}
