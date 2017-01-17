package se.group.tasksystem.api.resource;

import java.net.URI;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.group.tasksystem.api.filter.Authenticate;
import se.group.tasksystem.api.model.WorkItemRequestBean;
import se.group.tasksystem.data.model.WorkItem;
import se.group.tasksystem.data.service.UserService;
import se.group.tasksystem.data.service.WorkItemService;

@Component
@Path("workitems")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticate
public final class WorkItemResource {

	@Autowired
	private WorkItemService workItemService;

	@Autowired
	private UserService userService;

	@Context
	private UriInfo uriInfo;

	@POST
	public Response createWorkItem(WorkItem workItem) {
		WorkItem createdWorkItem = workItemService.createWorkItem(workItem);
		URI location = uriInfo.getAbsolutePathBuilder().path(createdWorkItem.getId().toString()).build();
		return Response.created(location).build();
	}

	@PUT
	public Response changeWorkItemStatus(WorkItem workItem) {
		WorkItem updatedWorkItem = workItemService.changeWorkItemStatus(workItem);
		URI location = uriInfo.getAbsolutePathBuilder().path(updatedWorkItem.getId().toString()).build();
		return Response.created(location).build();
	}

	@DELETE
	@Path("{id}")
	public Response deleteWorkItem(@PathParam("id") Long id) {
		workItemService.removeWorkItem(id);
		return Response.status(Status.NO_CONTENT).build();
	}

	@GET
	public List<WorkItem> getWorkItemsBy(@BeanParam WorkItemRequestBean request) {

		if (request.getStatus() != null) {
			return workItemService.getWorkItemsByStatus(request.getStatus());
		}
		if (request.getDescription() != null) {
			return workItemService.getWorkItemsByDescription(request.getDescription());
		}

		return workItemService.getAllWorkitem();
	}

	@PUT
	@Path("{workItemId}/user/{userId}")
	public Response addWorkItemToUser(@PathParam("workItemId") Long workItemId, @PathParam("userId") Long userId) {
		userService.addWorkItemToUser(userId, workItemId);
		return Response.status(Status.NO_CONTENT).build();
	}

	@GET
	@Path("team/{teamId}")
	public List<WorkItem> getWorkItemsByTeam(@PathParam("teamId") Long teamId) {
		return workItemService.getAllWorkItemsByTeam(teamId);
	}

	@GET
	@Path("user/{userId}")
	public List<WorkItem> getWorkItemsByUser(@PathParam("userId") Long userId) {
		return workItemService.getAllWorkItemsByUser(userId);
	}

}
