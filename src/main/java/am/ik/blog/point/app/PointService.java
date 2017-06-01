package am.ik.blog.point.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import am.ik.blog.point.domain.*;

@Service
public class PointService {
	private final PointEventRepository pointEventRepository;
	private final PaidEntryRepository paidEntryRepository;

	public PointService(PointEventRepository pointEventRepository,
			PaidEntryRepository paidEntryRepository) {
		this.pointEventRepository = pointEventRepository;
		this.paidEntryRepository = paidEntryRepository;
	}

	@Transactional
	public void addPoint(Username username, PointAmount pointAmount) {
		new PointEvents().add(username, pointAmount, pointEventRepository);
	}

	@Transactional
	public void consumePoint(EntryId entryId, Username username,
			PointAmount pointAmount) {
		new PointEvents().consume(entryId, username, pointAmount, pointEventRepository,
				paidEntryRepository);
	}
}
