package am.ik.blog.point.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Username {
	private static final String USERNAME = "username";
	@JsonProperty(USERNAME)
	final String value;

	@JsonCreator
	public Username(@JsonProperty(USERNAME) String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
