package am.ik.blog.point;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class TokenAuthorizationInterceptor implements ClientHttpRequestInterceptor {

	private final String token;

	public TokenAuthorizationInterceptor(String token) {
		this.token = token;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
			ClientHttpRequestExecution execution) throws IOException {
		request.getHeaders().add("Authorization", "Bearer " + token);
		return execution.execute(request, body);
	}
}
