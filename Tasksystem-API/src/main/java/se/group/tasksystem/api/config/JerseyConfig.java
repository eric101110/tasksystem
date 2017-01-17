package se.group.tasksystem.api.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import se.group.tasksystem.api.exception.AuthenticateException;
import se.group.tasksystem.api.exception.ServiceExceptionMapper;
import se.group.tasksystem.api.filter.AuthenticationFilter;
import se.group.tasksystem.api.resource.IssueResource;
import se.group.tasksystem.api.resource.TeamResource;
import se.group.tasksystem.api.resource.UserResource;
import se.group.tasksystem.api.resource.WorkItemResource;

@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(UserResource.class);
		register(TeamResource.class);
		register(WorkItemResource.class);
		register(IssueResource.class);
		register(ServiceExceptionMapper.class);
		register(AuthenticationFilter.class);
	}

}
