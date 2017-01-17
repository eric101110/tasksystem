package se.group.tasksystem.api.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import se.group.tasksystem.data.exception.ServiceException;

@Provider
public final class ServiceExceptionMapper implements ExceptionMapper<ServiceException> {

	@Override
	public Response toResponse(ServiceException exception) {
		return Response.status(Status.BAD_REQUEST).entity(new ExceptionResponse(Status.BAD_REQUEST, exception.getMessage())).build();
	}

}
