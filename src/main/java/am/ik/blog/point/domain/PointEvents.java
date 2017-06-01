package am.ik.blog.point.domain;

import java.time.Instant;
import java.util.Collection;
import java.util.TreeSet;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PointEvents {
	private final Collection<PointEvent> pointEvents;
	@JsonIgnore
	private final boolean given;

	public PointEvents() {
		this.pointEvents = new TreeSet<>();
		this.given = false;
	}

	public PointEvents(Collection<PointEvent> pointEvents) {
		this.pointEvents = new TreeSet<>(pointEvents);
		this.given = true;
	}

	public int totalPoint() {
		if (!this.given) {
			throw new IllegalStateException(
					"Events are not given. Total point will be wrong.");
		}
		return pointEvents.stream().mapToInt(x -> x.pointAmount.value).sum();
	}

	public static int totalPoint(Username username,
			PointEventRepository pointEventRepository) {
		return pointEventRepository.countTotalPoint(username);
	}

	public static PointEvents fromUsername(Username username,
			PointEventRepository pointEventRepository) {
		return pointEventRepository.findByUsername(username);
	}

	public PointEvents add(Username username, PointAmount pointAmount,
			PointEventRepository pointEventRepository) {
		Assert.isTrue(pointAmount.value >= 0,
				"'amount' must be greater than or equals to 0.");

		this.addEvent(EventType.ADD, username, pointAmount, pointEventRepository);
		return this;
	}

	public PointEvents consume(EntryId entryId, Username username,
			PointAmount pointAmount, PointEventRepository pointEventRepository,
			PaidEntryRepository paidEntryRepository) {
		PointEvents.checkConsumption(entryId, username, pointAmount, pointEventRepository,
				paidEntryRepository);
		PointEvent pointEvent = this.addEvent(EventType.CONSUME, username, pointAmount,
				pointEventRepository);
		PointEvents.pay(entryId, pointEvent.eventId, paidEntryRepository);
		return this;
	}

	private PointEvent addEvent(EventType eventType, Username username,
			PointAmount pointAmount, PointEventRepository pointEventRepository) {
		PointEvent pointEvent = new PointEvent(EventId.random(), eventType, pointAmount,
				username, Instant.now());
		pointEventRepository.store(pointEvent);
		this.pointEvents.add(pointEvent);
		return pointEvent;
	}

	// static methods

	public static void checkConsumption(EntryId entryId, Username username,
			PointAmount pointAmount, PointEventRepository pointEventRepository,
			PaidEntryRepository paidEntryRepository) {
		Assert.isTrue(pointAmount.value <= 0,
				"'amount' must be less than or equals to 0.");
		PointEvents.checkIfAlreadyPaid(entryId, username, paidEntryRepository);
		PointEvents.checkPoint(username, pointAmount, pointEventRepository);
	}

	private static void checkIfAlreadyPaid(EntryId entryId, Username username,
			PaidEntryRepository paidEntryRepository) {
		if (paidEntryRepository.existsByEntryIdAndUsername(entryId, username)) {
			throw new AlreadyPaidException(
					"You have already paid for entryId = " + entryId);
		}
	}

	private static void checkPoint(Username username, PointAmount pointAmount,
			PointEventRepository pointEventRepository) {
		int totalPoint = PointEvents.totalPoint(username, pointEventRepository);
		if (!pointAmount.canPay(totalPoint)) {
			throw new PointIsNotEnoughException("Point is not enough! "
					+ (pointAmount.abs() - totalPoint) + " points is required.");
		}
	}

	private static void pay(EntryId entryId, EventId eventId,
			PaidEntryRepository paidEntryRepository) {
		PaidEntry paidEntry = new PaidEntry(entryId, eventId);
		paidEntryRepository.store(paidEntry);
	}

	public static class AlreadyPaidException extends DomainException {
		public AlreadyPaidException(String message) {
			super(message);
		}
	}

	public static class PointIsNotEnoughException extends DomainException {
		public PointIsNotEnoughException(String message) {
			super(message);
		}
	}
}
