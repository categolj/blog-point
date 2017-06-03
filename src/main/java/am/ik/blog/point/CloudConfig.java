package am.ik.blog.point;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("cloud")
public class CloudConfig extends AbstractCloudConfig {
	@Bean
	@Primary
	ConnectionFactory pointRabbitConnectionFactory() {
		return connectionFactory().rabbitConnectionFactory("point-rabbit");
	}

	@Bean
	ConnectionFactory zipkinRabbitConnectionFactory() {
		return connectionFactory().rabbitConnectionFactory("zipkin-rabbit");
	}
}
