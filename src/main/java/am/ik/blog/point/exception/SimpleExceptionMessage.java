package am.ik.blog.point.exception;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleExceptionMessage implements Serializable {
	private final String message;

	@JsonCreator
	public SimpleExceptionMessage(@JsonProperty("message") String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
