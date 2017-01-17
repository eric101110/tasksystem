package se.group.tasksystem.api.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.group.tasksystem.api.filter.Authenticate;
import se.group.tasksystem.api.model.UserRequestBean;
import se.group.tasksystem.data.model.User;
import se.group.tasksystem.data.service.UserService;

@Component
@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticate
public final class UserResource {

	@Autowired
	private UserService userService;

	@Context
	private UriInfo uriInfo;

	@POST
	public Response createUser(User user) {
		User createdUser = userService.createUser(user);
		URI location = uriInfo.getAbsolutePathBuilder().path(createdUser.getId().toString()).build();
		return Response.created(location).build();
	}

	@PUT
	public Response updateUser(User user) {
		User updatedUser = userService.updateUser(user);
		URI location = uriInfo.getAbsolutePathBuilder().path(updatedUser.getId().toString()).build();
		return Response.created(location).build();
	}

	@GET
	@Path("{id}")
	public User getUserById(@PathParam("id") Long id) {
		return userService.getUserById(id);
	}

	@GET
	@Path("personalcode/{personalcode}")
	public User getUserByPersonalCode(@PathParam("personalcode") String code) {
		return userService.getUserByPersonalCode(code);
	}

	@GET
	public List<User> getUsersBy(@BeanParam UserRequestBean request) {
		List<User> users = userService.getUsersBy(request.getUsername(), request.getFirstname(), request.getLastname());
		return users;
	}

	@GET
	@Path("team/{teamId}")
	public List<User> getUsersFromTeam(@PathParam("teamId") Long teamId) {
		List<User> users = userService.getAllUserFromTeam(teamId);
		return users;
	}

}
