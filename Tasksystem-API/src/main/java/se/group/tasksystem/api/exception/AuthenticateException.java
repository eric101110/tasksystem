package se.group.tasksystem.api.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public final class AuthenticateException extends WebApplicationException {

	private static final long serialVersionUID = 2449882176398342064L;

	public AuthenticateException(String message) {
		super(Response.status(Status.UNAUTHORIZED).entity(new ExceptionResponse(Status.UNAUTHORIZED, message)).build());

	}

}
