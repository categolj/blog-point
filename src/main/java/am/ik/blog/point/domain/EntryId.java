package am.ik.blog.point.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EntryId {
	private static final String ENTRY_ID = "entryId";
	@JsonProperty(ENTRY_ID)
	final Integer value;

	@JsonCreator
	public EntryId(@JsonProperty(ENTRY_ID) Integer value) {
		this.value = value;
	}

	public EntryId(String value) {
		this(Integer.valueOf(value));
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public Integer value() {
		return this.value;
	}
}
