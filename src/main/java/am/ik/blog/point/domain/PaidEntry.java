package am.ik.blog.point.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class PaidEntry {
	@JsonUnwrapped
	final EntryId entryId;
	@JsonUnwrapped
	final EventId eventId;

	public PaidEntry(EntryId entryId, EventId eventId) {
		this.entryId = entryId;
		this.eventId = eventId;
	}

	public EntryId entryId() {
		return entryId;
	}

	public EventId eventId() {
		return eventId;
	}
}
