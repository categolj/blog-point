package am.ik.blog.point.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class PointEvent implements Comparable<PointEvent> {
	@JsonUnwrapped
	final EventId eventId;
	final EventType eventType;
	@JsonUnwrapped
	final PointAmount pointAmount;
	@JsonUnwrapped
	final Username username;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	final Instant eventDate;

	public PointEvent(EventId eventId, EventType eventType, PointAmount pointAmount,
			Username username, Instant eventDate) {
		this.eventId = eventId;
		this.eventType = eventType;
		this.pointAmount = pointAmount;
		this.username = username;
		this.eventDate = eventDate;
	}

	@Override
	public int compareTo(PointEvent o) {
		int eventDateCompared = this.eventDate.compareTo(o.eventDate);
		if (eventDateCompared == 0) {
			return this.eventId.value.compareTo(o.eventId.value);
		}
		return -eventDateCompared;
	}

	public EventId eventId() {
		return eventId;
	}

	public EventType eventType() {
		return eventType;
	}

	public PointAmount pointAmount() {
		return pointAmount;
	}

	public Username username() {
		return username;
	}

	public Instant eventDate() {
		return eventDate;
	}
}
