package se.group.tasksystem.api.resource;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

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

import se.group.tasksystem.api.exception.ExceptionResponse;
import se.group.tasksystem.data.model.Issue;
import se.group.tasksystem.data.model.WorkItem;
import se.group.tasksystem.data.model.WorkItem.WorkItemStatus;
import se.group.tasksystem.data.service.IssueService;
import se.group.tasksystem.data.service.WorkItemService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IssueResourceTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private IssueService issueService;
	
	@MockBean
	private WorkItemService workItemService;

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

		ResponseEntity<ExceptionResponse> response = this.restTemplate.exchange("/issues", HttpMethod.GET, null,
				ExceptionResponse.class);
		assertEquals("Unauthorized", response.getBody().getStatus().toString());
		assertEquals("Authenticate header missing", response.getBody().getMessage());
	}

	@Test
	public void createIssueAndAddToWorkItem() {

		Issue issue = new Issue("title", "description");
		Issue mockedIssue = mock(Issue.class);
		when(mockedIssue.getId()).thenReturn(1L);
		when(issueService.createIssueAndAddToWorkItem(any(), any())).thenReturn(mockedIssue);

		HttpEntity<Issue> httpEntity = new HttpEntity<Issue>(issue, headers);
		ResponseEntity<String> response = this.restTemplate.postForEntity("/issues/workitem/1", httpEntity,
				String.class);

		assertEquals("201", response.getStatusCode().toString());
		assertEquals(base + "issues/1", response.getHeaders().get("Location").get(0));

	}

	@Test
	public void updateIssue() {

		Issue issue = new Issue("title", "description");
		Issue mockedIssue = mock(Issue.class);
		when(mockedIssue.getId()).thenReturn(1L);
		when(issueService.updateIssue(any())).thenReturn(mockedIssue);

		HttpEntity<Issue> httpEntity = new HttpEntity<Issue>(issue, headers);
		ResponseEntity<String> response = this.restTemplate.exchange("/issues", HttpMethod.PUT, httpEntity,
				String.class);

		assertEquals("201", response.getStatusCode().toString());
		assertEquals(base + "issues/1", response.getHeaders().get("Location").get(0));
	}

	@Test
	public void getWorkItemsWithIssue() {

		WorkItem workItem = new WorkItem("title", "description", WorkItemStatus.UNSTARTED);
		when(workItemService.getWorkItemsWithIssue()).thenReturn(Arrays.asList(workItem));
		
		HttpEntity<WorkItem> httpEntity = new HttpEntity<WorkItem>(headers);

		ResponseEntity<List<WorkItem>> response = this.restTemplate.exchange("/issues/workitems", HttpMethod.GET, httpEntity,
				new ParameterizedTypeReference<List<WorkItem>>() {
				});

		assertEquals("200", response.getStatusCode().toString());
		assertEquals("title", response.getBody().get(0).getTitle());

	}

}
