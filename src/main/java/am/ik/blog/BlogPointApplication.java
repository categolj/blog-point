package am.ik.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableBinding(Sink.class)
@EnableResourceServer
@EnableDiscoveryClient
public class BlogPointApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogPointApplication.class, args);
	}

	@Bean
	ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		ObjectMapper mapper = builder.build();
		mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
		mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
		return mapper;
	}
}
