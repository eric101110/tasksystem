package se.group.tasksystem.api.resource;

import java.net.URI;
import java.util.List;

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
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.group.tasksystem.api.filter.Authenticate;
import se.group.tasksystem.data.model.Team;
import se.group.tasksystem.data.service.TeamService;

@Component
@Path("teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticate
public final class TeamResource {

	@Autowired
	private TeamService teamService;

	@Context
	private UriInfo uriInfo;

	@POST
	public Response createTeam(Team team) {
		Team createdTeam = teamService.createTeam(team);
		URI location = uriInfo.getAbsolutePathBuilder().path(createdTeam.getId().toString()).build();
		return Response.created(location).build();
	}

	@PUT
	public Response updateTeam(Team team) {
		Team updatedTeam = teamService.updateTeam(team);
		URI location = uriInfo.getAbsolutePathBuilder().path(updatedTeam.getId().toString()).build();
		return Response.created(location).build();
	}

	@GET
	public List<Team> getTeams() {
		return teamService.getAllTeams();
	}

	@PUT
	@Path("user/{userId}")
	public Response addUserToTeam(@PathParam("userId") Long userId) {
		teamService.addUserToTeam(userId);
		return Response.status(Status.NO_CONTENT).build();
	}

}
