package am.ik.blog.point.app;

import static java.util.stream.Collectors.toList;

import java.security.Principal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import am.ik.blog.point.domain.*;
import am.ik.blog.point.exception.SimpleExceptionMessage;

@RestController
public class PointApiController {
	private final PointEventRepository pointEventRepository;
	private final PaidEntryRepository paidEntryRepository;

	public PointApiController(PointEventRepository pointEventRepository,
			PaidEntryRepository paidEntryRepository) {
		this.pointEventRepository = pointEventRepository;
		this.paidEntryRepository = paidEntryRepository;
	}

	@GetMapping("v1/point_events")
	public PointEvents getPointEvents(Principal principal) {
		return PointEvents.fromUsername(Username.of(principal),
				this.pointEventRepository);
	}

	@GetMapping("v1/user")
	public Map<String, Object> getUser(Principal principal) {
		Username username = Username.of(principal);
		Map<String, Object> user = new LinkedHashMap<>();
		user.put("point", PointEvents.totalPoint(username, pointEventRepository));
		user.put("entryIds", this.paidEntryRepository.entryIdsByUsername(username)
				.stream().map(EntryId::value).collect(toList()));
		return user;
	}

	@PostMapping("v1/check_consumption")
	public Object checkConsumption(@RequestBody ConsumeBody payload,
			Principal principal) {
		Username username = Username.of(principal);
		PointEvents.checkConsumption(payload.entryId, username, payload.amount,
				this.pointEventRepository, this.paidEntryRepository);
		return Collections.singletonMap("message", "OK");
	}

	@ExceptionHandler(PointEvents.PointIsNotEnoughException.class)
	@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
	public SimpleExceptionMessage paymentError(PointEvents.PointIsNotEnoughException e) {
		return new SimpleExceptionMessage(e.getMessage());
	}
}
