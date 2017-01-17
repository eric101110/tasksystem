package se.group.tasksystem.api.resource;

import static org.junit.Assert.assertEquals;
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

import se.group.tasksystem.api.exception.ExceptionResponse;
import se.group.tasksystem.data.exception.ServiceException;
import se.group.tasksystem.data.model.User;
import se.group.tasksystem.data.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserResourceTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private UserService userService;

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

		ResponseEntity<ExceptionResponse> response = this.restTemplate.exchange("/users", HttpMethod.GET, null,
				ExceptionResponse.class);
		assertEquals("Unauthorized", response.getBody().getStatus().toString());
		assertEquals("Authenticate header missing", response.getBody().getMessage());
	}

	@Test
	public void createUser() {
		User user = new User("PersonalCode", "username18", "firstname", "lastname", null);
		User mockedUser = mock(User.class);
		when(mockedUser.getId()).thenReturn(1L);
		when(userService.createUser(any())).thenReturn(mockedUser);

		HttpEntity<User> httpEntity = new HttpEntity<User>(user, headers);
		ResponseEntity<String> response = this.restTemplate.postForEntity("/users", httpEntity, String.class);

		assertEquals("201", response.getStatusCode().toString());
		assertEquals(base + "users/1", response.getHeaders().get("Location").get(0));
	}

	@Test
	public void shouldRespondWithBadRequestIfUsernameIsTooShort() {
		User user = new User("137", "u", "firstname", "lastname", null);
		User mockedUser = mock(User.class);
		when(mockedUser.getId()).thenReturn(1L);
		when(mockedUser.getUsername()).thenReturn("username1");
		when(userService.createUser(any()))
				.thenThrow(new ServiceException("Username too short, must be 10 characters"));

		HttpEntity<User> httpEntity = new HttpEntity<User>(user, headers);
		ResponseEntity<ExceptionResponse> response = this.restTemplate.postForEntity("/users", httpEntity,
				ExceptionResponse.class);
		assertEquals("Bad Request", response.getBody().getStatus().toString());
		assertEquals("Username too short, must be 10 characters", response.getBody().getMessage());
	}

	@Test
	public void updateUser() {

		User updatedUser = new User("updatedPersonalCode", "updatedUsername18", "firstname", "lastname", null);
		User mockedUser = mock(User.class);
		when(mockedUser.getId()).thenReturn(1L);
		when(userService.updateUser(any())).thenReturn(mockedUser);

		HttpEntity<User> httpEntity = new HttpEntity<User>(updatedUser, headers);
		ResponseEntity<String> response = this.restTemplate.exchange("/users", HttpMethod.PUT, httpEntity,
				String.class);
		assertEquals("201", response.getStatusCode().toString());
		assertEquals(base + "users/1", response.getHeaders().get("Location").get(0));
	}

	@Test
	public void getUserByPersonalCode() {

		User user = new User("123", "usernamen1", "firstname", "lastname", null);
		when(userService.getUserByPersonalCode("123")).thenReturn(user);

		HttpEntity<User> httpEntity = new HttpEntity<User>(headers);
		ResponseEntity<User> response = this.restTemplate.exchange("/users/personalcode/123", HttpMethod.GET,
				httpEntity, User.class);
		assertEquals("123", response.getBody().getPersonalCode());
		assertEquals("200", response.getStatusCode().toString());
	}

	@Test
	public void getUserByParams() {

		User user1 = new User("123", "username18", "firstname", "lastname", null);
		User user2 = new User("532", "username19", "firstname", "lastname", null);
		User user3 = new User("175", "username20", "firstname", "lastname", null);
		when(userService.getUsersBy("username18", "", "")).thenReturn(Arrays.asList(user1));
		when(userService.getUsersBy("", "firstname", "")).thenReturn(Arrays.asList(user1, user2, user3));
		when(userService.getUsersBy("", "", "lastname")).thenReturn(Arrays.asList(user1, user2, user3));
		when(userService.getUsersBy("username19", "firstname", "lastname")).thenReturn(Arrays.asList(user2));

		HttpEntity<User> httpEntity = new HttpEntity<User>(headers);

		ResponseEntity<List<User>> response1 = this.restTemplate.exchange("/users?username=username18", HttpMethod.GET,
				httpEntity, new ParameterizedTypeReference<List<User>>() {
				});

		ResponseEntity<List<User>> response2 = this.restTemplate.exchange("/users?firstname=firstname", HttpMethod.GET,
				httpEntity, new ParameterizedTypeReference<List<User>>() {
				});

		ResponseEntity<List<User>> response3 = this.restTemplate.exchange("/users?lastname=lastname", HttpMethod.GET,
				httpEntity, new ParameterizedTypeReference<List<User>>() {
				});

		ResponseEntity<List<User>> response4 = this.restTemplate.exchange(
				"/users?username=username19&firstname=firstname&lastname=lastname", HttpMethod.GET, httpEntity,
				new ParameterizedTypeReference<List<User>>() {
				});

		assertEquals("username18", response1.getBody().get(0).getUsername());
		assertEquals(3, response2.getBody().size());
		assertEquals(3, response3.getBody().size());
		assertEquals("username19", response4.getBody().get(0).getUsername());
	}

	@Test
	public void getAllUsersFromTeam() {

		User user1 = new User("123", "username18", "firstname", "lastname", null);
		User user2 = new User("532", "username19", "firstname", "lastname", null);
		Long teamId = 1l;

		when(userService.getAllUserFromTeam(teamId)).thenReturn(Arrays.asList(user1, user2));

		HttpEntity<User> httpEntity = new HttpEntity<User>(headers);
		ResponseEntity<List<User>> response = this.restTemplate.exchange("/users/team/1", HttpMethod.GET, httpEntity,
				new ParameterizedTypeReference<List<User>>() {
				});

		assertEquals("200", response.getStatusCode().toString());
		assertEquals("username18", response.getBody().get(0).getUsername());
		assertEquals("username19", response.getBody().get(1).getUsername());
	}

}
