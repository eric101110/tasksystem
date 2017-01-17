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

import se.group.tasksystem.api.exception.ExceptionResponse;
import se.group.tasksystem.data.model.Team;
import se.group.tasksystem.data.service.TeamService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TeamResourceTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private TeamService teamService;

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

		ResponseEntity<ExceptionResponse> response = this.restTemplate.exchange("/teams", HttpMethod.GET, null,
				ExceptionResponse.class);
		assertEquals("Unauthorized", response.getBody().getStatus().toString());
		assertEquals("Authenticate header missing", response.getBody().getMessage());
	}

	@Test
	public void createTeam() {

		Team team = new Team("Team one");
		Team mockedTeam = mock(Team.class);
		when(mockedTeam.getId()).thenReturn(1L);
		when(teamService.createTeam(any())).thenReturn(mockedTeam);

		HttpEntity<Team> httpEntity = new HttpEntity<Team>(team, headers);
		ResponseEntity<String> response = this.restTemplate.postForEntity("/teams", httpEntity, String.class);

		assertEquals("201", response.getStatusCode().toString());
		assertEquals(base + "teams/1", response.getHeaders().get("Location").get(0));
	}

	@Test
	public void updateTeam() {

		Team team = new Team("Team one");
		Team mockedTeam = mock(Team.class);
		when(mockedTeam.getId()).thenReturn(1L);
		when(teamService.updateTeam(any())).thenReturn(mockedTeam);

		HttpEntity<Team> httpEntity = new HttpEntity<Team>(team, headers);

		ResponseEntity<String> response = this.restTemplate.exchange("/teams", HttpMethod.PUT, httpEntity,
				String.class);
		assertEquals("201", response.getStatusCode().toString());
		assertEquals(base + "teams/1", response.getHeaders().get("Location").get(0));
	}

	@Test
	public void getAllTeams() {
		Team team1 = new Team("Team one");
		Team team2 = new Team("Team two");
		Team team3 = new Team("Team three");

		when(teamService.getAllTeams()).thenReturn(Arrays.asList(team1, team2, team3));
		
		HttpEntity<Team> httpEntity = new HttpEntity<Team>(headers);

		ResponseEntity<List<Team>> response = this.restTemplate.exchange("/teams", HttpMethod.GET, httpEntity,
				new ParameterizedTypeReference<List<Team>>() {
				});

		assertEquals(3, response.getBody().size());
		assertEquals("200", response.getStatusCode().toString());
	}

	@Test
	public void addUserToTeam() {

		Team team = new Team("Team one");
		Team mockedTeam = mock(Team.class);
		when(mockedTeam.getId()).thenReturn(1L);
		when(teamService.addUserToTeam(1L)).thenReturn(mockedTeam);

		HttpEntity<Team> httpEntity = new HttpEntity<Team>(team, headers);

		ResponseEntity<String> response = this.restTemplate.exchange("/teams/user/1", HttpMethod.PUT, httpEntity,
				String.class);

		assertEquals("204", response.getStatusCode().toString());

	}

}