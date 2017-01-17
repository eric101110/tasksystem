package se.group.tasksystem.api.resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import se.group.tasksystem.api.exception.ExceptionResponse;
import se.group.tasksystem.data.model.User;
import se.group.tasksystem.data.model.WorkItem;
import se.group.tasksystem.data.model.WorkItem.WorkItemStatus;
import se.group.tasksystem.data.repository.UserRepository;
import se.group.tasksystem.data.repository.WorkItemRepository;
import se.group.tasksystem.data.service.WorkItemService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WorkItemResourceTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private WorkItemService workItemService;

	@MockBean
	private WorkItemRepository workItemRepository;

	@MockBean
	private UserRepository userRepository;

	@LocalServerPort
	private int port;

	private URL base;

	private HttpHeaders headers;

	@Before
	public void setUp() throws Exception {
		this.base = new URL("http://localhost:" + port + "/");
		headers = new HttpHeaders();
		headers.add("authtoken", "secret");
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
	public void shouldThrowIfHeaderDoesntHaveAuthToken() throws JsonParseException, JsonMappingException, IOException {

		ResponseEntity<ExceptionResponse> response = this.restTemplate.exchange("/workitems", HttpMethod.GET, null,
				ExceptionResponse.class);
		assertEquals("Unauthorized", response.getBody().getStatus().toString());
		assertEquals("Authenticate header missing", response.getBody().getMessage());
	}

	@Test
	public void createWorkItem() {

		WorkItem workItem = new WorkItem("title", "description", WorkItemStatus.UNSTARTED);
		WorkItem mockedWorkItem = mock(WorkItem.class);
		when(mockedWorkItem.getId()).thenReturn(1L);
		when(workItemService.createWorkItem(any())).thenReturn(mockedWorkItem);

		HttpEntity<WorkItem> httpEntity = new HttpEntity<WorkItem>(workItem, headers);
		ResponseEntity<String> response = this.restTemplate.postForEntity("/workitems", httpEntity, String.class);

		assertEquals("201", response.getStatusCode().toString());
		assertEquals(base + "workitems/1", response.getHeaders().get("Location").get(0));
	}

	@Test
	public void changeStatusOfWorkItem() {

		WorkItem workItem = new WorkItem("title", "description", WorkItemStatus.STARTED);
		WorkItem mockedWorkItem = mock(WorkItem.class);
		when(mockedWorkItem.getId()).thenReturn(1L);
		when(workItemService.changeWorkItemStatus(any())).thenReturn(mockedWorkItem);

		HttpEntity<WorkItem> httpEntity = new HttpEntity<WorkItem>(workItem, headers);

		ResponseEntity<String> response = this.restTemplate.exchange("/workitems", HttpMethod.PUT, httpEntity,
				String.class);

		assertEquals("201", response.getStatusCode().toString());
		assertEquals(base + "workitems/1", response.getHeaders().get("Location").get(0));
	}

	@Test
	public void deleteWorkItem() {

		HttpEntity<WorkItem> httpEntity = new HttpEntity<WorkItem>(headers);

		ResponseEntity<String> response = this.restTemplate.exchange("/workitems/1", HttpMethod.DELETE, httpEntity,
				String.class);

		assertEquals("204", response.getStatusCode().toString());
	}

	@Test
	public void addWorkItemToUser() {

		User user = new User("PersonalCode", "username18", "firstname", "lastname", null);
		WorkItem workItem = new WorkItem("title", "description", WorkItemStatus.UNSTARTED);

		when(userRepository.exists(any())).thenReturn(true);
		when(workItemRepository.findOne(any())).thenReturn(workItem);
		when(userRepository.findOne(any())).thenReturn(user);

		HttpEntity<WorkItem> httpEntity = new HttpEntity<WorkItem>(headers);
		ResponseEntity<String> response = this.restTemplate.exchange("/workitems/1/user/1", HttpMethod.PUT, httpEntity,
				String.class);

		assertEquals("204", response.getStatusCode().toString());
	}

	@Test
	public void getWorkItemsByStatus() {

		WorkItem workItem = new WorkItem("title", "description", WorkItemStatus.UNSTARTED);
		when(workItemService.getWorkItemsByStatus(WorkItemStatus.UNSTARTED)).thenReturn(Arrays.asList(workItem));

		HttpEntity<WorkItem> httpEntity = new HttpEntity<WorkItem>(headers);

		ResponseEntity<List<WorkItem>> response = this.restTemplate.exchange("/workitems?status=UNSTARTED",
				HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<WorkItem>>() {
				});
		assertEquals("200", response.getStatusCode().toString());
		assertEquals("title", response.getBody().get(0).getTitle());
	}

	@Test
	public void getWorkItemsByTeam() {
		
		WorkItem workItem = new WorkItem("title", "description", WorkItemStatus.UNSTARTED);
		when(workItemService.getAllWorkItemsByTeam(1L)).thenReturn(Arrays.asList(workItem));

		HttpEntity<WorkItem> httpEntity = new HttpEntity<WorkItem>(headers);

		ResponseEntity<List<WorkItem>> response = this.restTemplate.exchange("/workitems/team/1", HttpMethod.GET,
				httpEntity, new ParameterizedTypeReference<List<WorkItem>>() {
				});

		assertEquals("200", response.getStatusCode().toString());
		assertEquals("title", response.getBody().get(0).getTitle());
	}

	@Test
	public void getWorkItemsByUser() {
		
		WorkItem workItem = new WorkItem("title", "description", WorkItemStatus.UNSTARTED);
		when(workItemService.getAllWorkItemsByUser(1L)).thenReturn(Arrays.asList(workItem));

		HttpEntity<WorkItem> httpEntity = new HttpEntity<WorkItem>(headers);

		ResponseEntity<List<WorkItem>> response = this.restTemplate.exchange("/workitems/user/1", HttpMethod.GET,
				httpEntity, new ParameterizedTypeReference<List<WorkItem>>() {
				});

		assertEquals("200", response.getStatusCode().toString());
		assertEquals("title", response.getBody().get(0).getTitle());
	}

	@Test
	public void getWorkItemByDescription() {

		WorkItem workItem = new WorkItem("title", "123", WorkItemStatus.UNSTARTED);
		when(workItemService.getWorkItemsByDescription("123")).thenReturn(Arrays.asList(workItem));

		HttpEntity<WorkItem> httpEntity = new HttpEntity<WorkItem>(headers);

		ResponseEntity<List<WorkItem>> response = this.restTemplate.exchange("/workitems?description=123",
				HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<WorkItem>>() {
				});

		assertEquals("123", response.getBody().get(0).getDescription());
		assertEquals("200", response.getStatusCode().toString());

	}

}
