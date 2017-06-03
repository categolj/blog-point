package am.ik.blog.point.app;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;

import am.ik.blog.point.TokenAuthorizationInterceptor;
import am.ik.blog.point.UserInfoServer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({ "classpath:/delete-test-data.sql", "classpath:/insert-test-data.sql" })
public class PointApiControllerTest {
	@Autowired
	TestRestTemplate restTemplate;
	UserInfoServer userInfoServer;

	@Before
	public void setUp() {
		userInfoServer = new UserInfoServer(34539);
		userInfoServer.start();
	}

	@After
	public void tearDown() {
		userInfoServer.shutdown();
	}

	@Test
	public void getPointEvents_non_existing_user() throws Exception {
		restTemplate.getRestTemplate()
				.setInterceptors(singletonList(new TokenAuthorizationInterceptor("foo")));
		ResponseEntity<JsonNode> response = restTemplate.getForEntity("/v1/point_events",
				JsonNode.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		JsonNode body = response.getBody();
		Iterator<JsonNode> pointEvents = body.get("pointEvents").elements();
		assertThat(pointEvents).isNotNull();
		assertThat(pointEvents.hasNext()).isFalse();
	}

	@Test
	public void getPointEvents_test_user_1() throws Exception {
		restTemplate.getRestTemplate().setInterceptors(
				singletonList(new TokenAuthorizationInterceptor("test-user-1")));
		ResponseEntity<JsonNode> response = restTemplate.getForEntity("/v1/point_events",
				JsonNode.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		JsonNode body = response.getBody();
		System.out.println(body);
		List<JsonNode> jsonNodes = StreamSupport
				.stream(body.get("pointEvents").spliterator(), false)
				.collect(Collectors.toList());
		assertThat(jsonNodes).hasSize(4);

		assertThat(jsonNodes.get(0).get("eventId").asText())
				.isEqualTo("10000000-0000-0000-0000-000000000004");
		assertThat(jsonNodes.get(0).get("eventType").asText()).isEqualTo("CONSUME");
		assertThat(jsonNodes.get(0).get("amount").asInt()).isEqualTo(-50);
		assertThat(jsonNodes.get(0).get("username").asText()).isEqualTo("test-user-1");
		assertThat(jsonNodes.get(0).get("eventDate").asText())
				.isEqualTo("2017-05-31T16:55:56Z");

		assertThat(jsonNodes.get(1).get("eventId").asText())
				.isEqualTo("10000000-0000-0000-0000-000000000003");
		assertThat(jsonNodes.get(1).get("eventType").asText()).isEqualTo("CONSUME");
		assertThat(jsonNodes.get(1).get("amount").asInt()).isEqualTo(-100);
		assertThat(jsonNodes.get(1).get("username").asText()).isEqualTo("test-user-1");
		assertThat(jsonNodes.get(1).get("eventDate").asText())
				.isEqualTo("2017-05-31T16:54:56Z");

		assertThat(jsonNodes.get(2).get("eventId").asText())
				.isEqualTo("10000000-0000-0000-0000-000000000002");
		assertThat(jsonNodes.get(2).get("eventType").asText()).isEqualTo("ADD");
		assertThat(jsonNodes.get(2).get("amount").asInt()).isEqualTo(200);
		assertThat(jsonNodes.get(2).get("username").asText()).isEqualTo("test-user-1");
		assertThat(jsonNodes.get(2).get("eventDate").asText())
				.isEqualTo("2017-05-31T16:53:56Z");

		assertThat(jsonNodes.get(3).get("eventId").asText())
				.isEqualTo("10000000-0000-0000-0000-000000000001");
		assertThat(jsonNodes.get(3).get("eventType").asText()).isEqualTo("ADD");
		assertThat(jsonNodes.get(3).get("amount").asInt()).isEqualTo(100);
		assertThat(jsonNodes.get(3).get("username").asText()).isEqualTo("test-user-1");
		assertThat(jsonNodes.get(3).get("eventDate").asText())
				.isEqualTo("2017-05-31T16:52:56Z");
	}

	@Test
	public void getUser_test_user_1() throws Exception {
		restTemplate.getRestTemplate().setInterceptors(
				singletonList(new TokenAuthorizationInterceptor("test-user-1")));
		ResponseEntity<JsonNode> response = restTemplate.getForEntity("/v1/user",
				JsonNode.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		JsonNode body = response.getBody();
		assertThat(body.get("point").asInt()).isEqualTo(150);
		List<JsonNode> jsonNodes = StreamSupport
				.stream(body.get("entryIds").spliterator(), false)
				.collect(Collectors.toList());

		assertThat(jsonNodes).hasSize(2);

		assertThat(jsonNodes.get(0).asInt()).isEqualTo(77);
		assertThat(jsonNodes.get(1).asInt()).isEqualTo(88);
	}

	@Test
	public void getUser_non_existing_user() throws Exception {
		restTemplate.getRestTemplate()
				.setInterceptors(singletonList(new TokenAuthorizationInterceptor("foo")));
		ResponseEntity<JsonNode> response = restTemplate.getForEntity("/v1/user",
				JsonNode.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		JsonNode body = response.getBody();
		assertThat(body.get("point").asInt()).isEqualTo(0);
		Iterator<JsonNode> entryIds = body.get("entryIds").elements();
		assertThat(entryIds).isNotNull();
		assertThat(entryIds.hasNext()).isFalse();
	}

	@Test
	public void checkConsumption_ok() throws Exception {
		restTemplate.getRestTemplate().setInterceptors(
				singletonList(new TokenAuthorizationInterceptor("test-user-1")));
		Map<String, Object> req = new HashMap<>();
		req.put("amount", -150);
		req.put("entryId", 99);

		ResponseEntity<JsonNode> response = restTemplate
				.postForEntity("/v1/check_consumption", req, JsonNode.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().get("message").asText()).isEqualTo("OK");
	}

	@Test
	public void checkConsumption_shortage() throws Exception {
		restTemplate.getRestTemplate().setInterceptors(
				singletonList(new TokenAuthorizationInterceptor("test-user-1")));
		Map<String, Object> req = new HashMap<>();
		req.put("amount", -200);
		req.put("entryId", 99);

		ResponseEntity<JsonNode> response = restTemplate
				.postForEntity("/v1/check_consumption", req, JsonNode.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.PAYMENT_REQUIRED);
		assertThat(response.getBody().get("message").asText())
				.isEqualTo("Point is not enough! 50 points is required.");
	}

	@Test
	public void checkConsumption_already_paid() throws Exception {
		restTemplate.getRestTemplate().setInterceptors(
				singletonList(new TokenAuthorizationInterceptor("test-user-1")));
		Map<String, Object> req = new HashMap<>();
		req.put("amount", -100);
		req.put("entryId", 88);

		ResponseEntity<JsonNode> response = restTemplate
				.postForEntity("/v1/check_consumption", req, JsonNode.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(response.getBody().get("message").asText())
				.isEqualTo("You have already paid for entryId = 88");
	}

	@Test
	public void checkConsumption_positive_amount() throws Exception {
		restTemplate.getRestTemplate().setInterceptors(
				singletonList(new TokenAuthorizationInterceptor("test-user-1")));
		Map<String, Object> req = new HashMap<>();
		req.put("amount", 100);
		req.put("entryId", 99);

		ResponseEntity<JsonNode> response = restTemplate
				.postForEntity("/v1/check_consumption", req, JsonNode.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody().get("message").asText())
				.isEqualTo("'amount' must be less than or equals to 0.");
	}

	@Test
	public void checkConsumption_non_existing_user() throws Exception {
		restTemplate.getRestTemplate()
				.setInterceptors(singletonList(new TokenAuthorizationInterceptor("foo")));
		Map<String, Object> req = new HashMap<>();
		req.put("amount", -100);
		req.put("entryId", 99);

		ResponseEntity<JsonNode> response = restTemplate
				.postForEntity("/v1/check_consumption", req, JsonNode.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.PAYMENT_REQUIRED);
		assertThat(response.getBody().get("message").asText())
				.isEqualTo("Point is not enough! 100 points is required.");
	}
}