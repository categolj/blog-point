package am.ik.blog.point.domain;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventId {
	@JsonProperty("eventId")
	final UUID value;

	public EventId(UUID value) {
		this.value = value;
	}

	public static EventId random() {
		return new EventId(UUID.randomUUID());
	}

	@Override
	public String toString() {
		if (this.value == null) {
			return null;
		}
		return this.value.toString();
	}
}
