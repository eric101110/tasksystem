package se.group.tasksystem.data;

import java.text.ParseException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import se.group.tasksystem.data.config.Infrastructure;
import se.group.tasksystem.data.exception.ServiceException;
import se.group.tasksystem.data.model.Issue;
import se.group.tasksystem.data.model.Team;
import se.group.tasksystem.data.model.User;
import se.group.tasksystem.data.service.IssueService;
import se.group.tasksystem.data.service.TeamService;
import se.group.tasksystem.data.service.UserService;
import se.group.tasksystem.data.service.WorkItemService;

public final class Main {

	public static void main(String[] args) throws ServiceException, ParseException {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("Production");
		context.register(Infrastructure.class);
		context.scan("se.group.tasksystem.data");
		context.refresh();

		UserService userService = context.getBean(UserService.class);
		TeamService teamService = context.getBean(TeamService.class);
		WorkItemService workitemService = context.getBean(WorkItemService.class);
		IssueService issueService = context.getBean(IssueService.class);

		/**
		 * User
		 */

		// Iterable<User> users = userService.getPaginatedUsers(new
		// PageRequest(0, 5));
		// users.forEach(System.out::println);

		// System.out.println(userService.getFilteredUser("username13", "",
		// ""));

//		 userService.createUser(new User("personalcode","username111", "firstname",
//		 "lastname", null));

		// userService.inActivateUser(1L);
		// userService.activateUser(1L);

		// User user = userService.getUserById(2L);
		// user.setFirstname("firstname1123");
		// user.setLastname("lastname1");
		// userService.saveOrUpdateUser(user);

		// List<User> users =
		// userService.getUserByUsernameAndFirstnameAndLastname("", "eric1",
		// "karlsson");

//		 List<User> users = userService.getAllUserFromTeam(1L);
//		 System.out.println(users.get(0).getUsername());
//		 System.out.println(users.get(1).getUsername());

		/**
		 * Team
		 */

//		 Team team = teamService.createTeam(new Team("Team 1"));

		// Team team = teamService.getTeamById(1L);

		// Team team teamService.inActivateTeam(1L);

		// Team team = teamService.activateTeam(1L);

		// List<Team> teams = teamService.getAllTeams();

		// System.out.println(teamService.addUserToTeam(3L));

		/**
		 * Workitem
		 */

		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// Date fromDate = sdf.parse("2016-08-01");
		// Date toDate = sdf.parse("2016-08-30");

		// WorkitemStatus status = WorkitemStatus.DONE;
		// List<Workitem> workitems =
		// workitemService.getWorkitemHistory(fromDate, toDate, status);
		// workitems.forEach(System.out::println);

		// for(int i = 0; i < 20; i++){
		// workitemService.saveOrUpdateWorkItem(new Workitem("work title
		// "+i,
		// "desc",WorkitemStatus.DONE));
		// }

//		 workitemService.saveOrUpdateWorkItem(new Workitem("work title 525",
//		 "desc",WorkitemStatus.UNSTARTED));

		// System.out.println(workitemService.getWorkitemById(1L).getUpdatedDate());

		// System.out.println(workitemService.getWorkitemsByStatus(WorkitemStatus.UNSTARTED));

		// workitemService.removeWorkitem(1L);

		// userService.addWorkItemToUser(2L, 1L);

//		 workitemService.changeWorkitemStatus(2L,
//		 WorkitemStatus.DONE);

		// workitemService.getAllWorkitemByUser(1L);

		// System.out.println(workitemService.getAllWorkitemByTeam(1L));

		// workitemService.getWorkitemByDescription("loco");

		/**
		 * Issue
		 */

//		 issueService.createIssueAndAddToWorkitem(new Issue("One issue",
//		 "Description"), 2L);

			/**
			 * User
			 */

			// User user = userService.createUser(new User("1005", "username100888", "firstname", "lastname", null));
			// System.out.println(user.getUsername());
		
			// User user = userService.updateUser(1L, new User("1102", "username0002","firstname","lastname", null));
			// System.out.println(user);

			// User user = userService.inActivateUser(1L);
			// System.out.println(user);
			
			// User user = userService.activateUser(2L);
			// System.out.println(user);

			// User user = userService.getUserById(2L);
			// System.out.println(user);
			
			// User user = userService.getUserByPersonalCode("1001");
			// System.out.println(user);
		
			// List<User> users = userService.getUserByUsernameAndFirstnameAndLastname("username0001", "firstname", "lastname");
			// users.forEach(user -> System.out.println(user));
			 
			// List<User> users = userService.getAllUserFromTeam(1L);
			// users.forEach(user -> System.out.println(user));
			
			// userService.addWorkItemToUser(1L, 1L);
			

			/**
			 * Team
			 */
			// Team team = teamService.createTeam(new Team("Team 1"));
			// System.out.println(team);
			
			// Team team = teamService.updateTeam(1L, "Team 2");
			// System.out.println(team);

			// Team team = teamService.getTeamById(1L);
			// System.out.println(team);

			// Team team = teamService.inActivateTeam(1L);
			// System.out.println(team);

			// Team team = teamService.activateTeam(1L);
			// System.out.println(team);

			// List<Team> teams = teamService.getAllTeams();
			// teams.forEach(team -> System.out.println(team));

			// Team team = teamService.addUserToTeam(1L);
			// System.out.println(team);

			/**
			 * WorkItem
			 */

			// WorkItem workItem = workItemService.createWorkItem(new WorkItem("workItem1", "desc1", WorkItemStatus.STARTED));
			// System.out.println(workItem);
			
			// WorkItem workItem = workItemService.updateWorkItem(1L, "workItem2", "desc2");
			// System.out.println(workItem);

			// 	List<WorkItem> workItems = workItemService.getWorkItemsByStatus(WorkItemStatus.STARTED);
			// 	workItems.forEach(workItem -> System.out.println(workItem));

			// 	workItemService.removeWorkItem(1L);
			
			// WorkItem workItem = workItemService.getWorkItemById(1L);
			// System.out.println(workItem);

		 	// userService.addWorkItemToUser(1L, 1L);

			// WorkItem workItem = workItemService.changeWorkItemStatus(1L, WorkItemStatus.DONE);
			// System.out.println(workItem);

			// List<WorkItem> workItems = workItemService.getAllWorkItemsByUser(1L);
			// workItems.forEach(workItem -> System.out.println(workItem));
			
			// List<WorkItem> workItems = workItemService.getAllWorkItemsByTeam(1L);
			// workItems.forEach(workItem -> System.out.println(workItem));

			// List<WorkItem> workItems = workItemService.getWorkItemsByDescription("desc");
			// workItems.forEach(workItem -> System.out.println(workItem));

			/**
			 * Issue
			 */

//			 Issue issue = issueService.createIssueAndAddToWorkItem(new Issue("123 issue", "Description"), 5L);
//			 System.out.println(issue);
			
			// Issue issue = issueService.updateIssue(1L, "title", "desc1");
			// System.out.println(issue);
			
			// Issue issue = issueService.getIssueById(1L);
			// System.out.println(issue);
			
			// List<WorkItem> workItemsWithIssue = workItemService.getWorkItemsWithIssue();
			// workItemsWithIssue.forEach(workItem -> System.out.println(workItem));

		}

	}

