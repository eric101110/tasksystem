package se.group.tasksystem.api.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import se.group.tasksystem.api.exception.AuthenticateException;

@Provider
@Priority(Priorities.AUTHENTICATION)
@Authenticate
public final class AuthenticationFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext request) throws IOException {

		String authHeader = request.getHeaderString("authtoken");

		if (authHeader == null) {
			throw new AuthenticateException("Authenticate header missing");
		}

		if (!authHeader.equals("secret")) {
			throw new AuthenticateException("Not a valid authentication token");

		}

	}

}
