package am.ik.blog.point.app;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import am.ik.blog.point.domain.PointAmount;
import am.ik.blog.point.domain.Username;

public class AddBody {
	@JsonUnwrapped
	public Username username;
	@JsonUnwrapped
	public PointAmount amount;

	@Override
	public String toString() {
		return "AddBody{" + "username=" + username + ", amount=" + amount + '}';
	}
}
