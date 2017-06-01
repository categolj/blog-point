package am.ik.blog.point.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PointAmount {
	private static final String AMOUNT = "amount";

	@JsonProperty(AMOUNT)
	final int value;

	@JsonCreator
	public PointAmount(@JsonProperty(AMOUNT) int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public int abs() {
		return Math.abs(this.value);
	}

	public boolean canPay(int totalPoint) {
		return abs() <= totalPoint;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
