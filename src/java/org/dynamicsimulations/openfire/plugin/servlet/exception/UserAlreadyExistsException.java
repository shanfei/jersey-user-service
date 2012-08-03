package org.dynamicsimulations.openfire.plugin.servlet.exception;

import org.dynamicsimulations.openfire.plugin.servlet.response.ErrorResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UserAlreadyExistsException extends WebApplicationException {

    public UserAlreadyExistsException(final Exception e) {
        super(Response.status(400)
                .entity(new ErrorResponse(e))
                .type(MediaType.APPLICATION_JSON)
                .build());
    }
}