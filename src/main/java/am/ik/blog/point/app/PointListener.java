package am.ik.blog.point.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import am.ik.blog.point.domain.DomainException;

@Component
public class PointListener {
	private static final Logger log = LoggerFactory.getLogger(PointListener.class);
	private final PointService pointService;

	public PointListener(PointService pointService) {
		this.pointService = pointService;
	}

	@StreamListener(target = Sink.INPUT, condition = "headers['eventType']=='add'")
	public void add(@Payload AddBody payload) {
		log.info("Add {}", payload);
		try {
			this.pointService.addPoint(payload.username, payload.amount);
		}
		catch (DomainException e) {
			log.warn("Cannot add " + payload, e);
		}
	}

	@StreamListener(target = Sink.INPUT, condition = "headers['eventType']=='consume'")
	public void consume(@Payload ConsumeBody payload) {
		log.info("Consume {}", payload);
		try {
			this.pointService.consumePoint(payload.entryId, payload.username,
					payload.amount);
		}
		catch (DomainException e) {
			log.warn("Cannot consume " + payload, e);
		}
	}

}
