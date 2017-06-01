package am.ik.blog.point.app;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import am.ik.blog.point.domain.EntryId;
import am.ik.blog.point.domain.PointAmount;
import am.ik.blog.point.domain.Username;

public class ConsumeBody {
	@JsonUnwrapped
	public EntryId entryId;
	@JsonUnwrapped
	public Username username;
	@JsonUnwrapped
	public PointAmount amount;

	@Override
	public String toString() {
		return "ConsumeBody{" + "entryId=" + entryId + ", username=" + username
				+ ", amount=" + amount + '}';
	}
}
