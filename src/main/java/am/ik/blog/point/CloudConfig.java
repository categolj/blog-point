package am.ik.blog.point;

import javax.sql.DataSource;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cloud")
public class CloudConfig extends AbstractCloudConfig {
	@Bean
	@Primary
	ConnectionFactory pointRabbitConnectionFactory() {
		return connectionFactory().rabbitConnectionFactory("point-rabbit");
	}

	// @Bean
	// ConnectionFactory zipkinRabbitConnectionFactory() {
	// 	return connectionFactory().rabbitConnectionFactory("zipkin-rabbit");
	// }

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.tomcat")
	DataSource dataSource() {
		return connectionFactory().dataSource();
	}
}
