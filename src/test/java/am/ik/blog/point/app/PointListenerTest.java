package am.ik.blog.point.app;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({ "classpath:/delete-test-data.sql", "classpath:/insert-test-data.sql" })
public class PointListenerTest {
	@Autowired
	Sink sink;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	TestRestTemplate restTemplate;

	@Test
	public void add_to_test_user_1() throws Exception {
		Map<String, Object> req = new HashMap<>();
		String username = "test-user-1";
		req.put("username", username);
		req.put("amount", 100);
		Message<?> message = MessageBuilder
				.withPayload(objectMapper.writeValueAsString(req))
				.setHeader("eventType", "add").build();
		sink.input().send(message);

		int point = restTemplate
				.getForObject("/v1/users/{username}", JsonNode.class, username)
				.get("point").asInt();
		assertThat(point).isEqualTo(250);
	}

	@Test
	public void add_to_test_user_3() throws Exception {
		Map<String, Object> req = new HashMap<>();
		String username = "test-user-3";
		req.put("username", username);
		req.put("amount", 100);
		Message<?> message = MessageBuilder
				.withPayload(objectMapper.writeValueAsString(req))
				.setHeader("eventType", "add").build();
		sink.input().send(message);

		int point = restTemplate
				.getForObject("/v1/users/{username}", JsonNode.class, username)
				.get("point").asInt();
		assertThat(point).isEqualTo(100);
	}

	@Test
	public void consume_ok() throws Exception {
		Map<String, Object> req = new HashMap<>();
		String username = "test-user-1";
		req.put("username", username);
		req.put("amount", -150);
		req.put("entryId", 99);

		Message<?> message = MessageBuilder
				.withPayload(objectMapper.writeValueAsString(req))
				.setHeader("eventType", "consume").build();
		sink.input().send(message);

		JsonNode response = restTemplate.getForObject("/v1/users/{username}",
				JsonNode.class, username);
		int point = response.get("point").asInt();
		assertThat(point).isEqualTo(0);
		Set<Integer> entryIds = StreamSupport
				.stream(response.get("entryIds").spliterator(), false)
				.map(JsonNode::asInt).collect(Collectors.toSet());
		assertThat(entryIds).contains(99);
	}

	@Test
	public void consume_shortage() throws Exception {
		Map<String, Object> req = new HashMap<>();
		String username = "test-user-1";
		req.put("username", username);
		req.put("amount", -200);
		req.put("entryId", 99);

		Message<?> message = MessageBuilder
				.withPayload(objectMapper.writeValueAsString(req))
				.setHeader("eventType", "consume").build();
		sink.input().send(message);

		JsonNode response = restTemplate.getForObject("/v1/users/{username}",
				JsonNode.class, username);
		int point = response.get("point").asInt();
		assertThat(point).isEqualTo(150);
		Set<Integer> entryIds = StreamSupport
				.stream(response.get("entryIds").spliterator(), false)
				.map(JsonNode::asInt).collect(Collectors.toSet());
		assertThat(entryIds).doesNotContain(99);
	}

	@Test
	public void consume_already_paid() throws Exception {
		Map<String, Object> req = new HashMap<>();
		String username = "test-user-1";
		req.put("username", username);
		req.put("amount", -150);
		req.put("entryId", 88);

		Message<?> message = MessageBuilder
				.withPayload(objectMapper.writeValueAsString(req))
				.setHeader("eventType", "consume").build();
		sink.input().send(message);

		JsonNode response = restTemplate.getForObject("/v1/users/{username}",
				JsonNode.class, username);
		int point = response.get("point").asInt();
		assertThat(point).isEqualTo(150);
	}
}