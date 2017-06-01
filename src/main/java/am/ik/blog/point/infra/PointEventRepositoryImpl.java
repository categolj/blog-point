package am.ik.blog.point.infra;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import am.ik.blog.point.domain.*;

@Repository
public class PointEventRepositoryImpl implements PointEventRepository {
	private static final RowMapper<PointEvent> pointEventRowMapper = new RowMapper<PointEvent>() {
		@Override
		public PointEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new PointEvent(new EventId(UUID.fromString(rs.getString("event_id"))),
					EventType.valueOf(rs.getString("event_type")),
					new PointAmount(rs.getInt("amount")),
					new Username(rs.getString("username")),
					rs.getTimestamp("event_date").toInstant());
		}
	};

	private final JdbcTemplate jdbcTemplate;

	public PointEventRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int countTotalPoint(Username username) {
		return this.jdbcTemplate.queryForObject(
				"SELECT IFNULL(SUM(amount), 0) FROM point_event WHERE username = ?",
				Integer.class, username.toString());
	}

	@Override
	public PointEvents findByUsername(Username username) {
		return new PointEvents(this.jdbcTemplate.query(
				"SELECT event_id, event_type, amount, username, event_date FROM point_event WHERE username = ? ORDER BY event_date DESC",
				pointEventRowMapper, username.toString()));
	}

	@Override
	@Transactional
	public int store(PointEvent pointEvent) {
		return this.jdbcTemplate.update(
				"INSERT INTO point_event(event_id, event_type, amount, username, event_date) VALUES(?, ?, ?, ?, ?)",
				pointEvent.eventId().toString(), pointEvent.eventType().name(),
				pointEvent.pointAmount().value(), pointEvent.username().toString(),
				Timestamp.from(pointEvent.eventDate()));
	}
}
