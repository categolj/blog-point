package am.ik.blog.point.infra;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import am.ik.blog.point.domain.EntryId;
import am.ik.blog.point.domain.PaidEntry;
import am.ik.blog.point.domain.PaidEntryRepository;
import am.ik.blog.point.domain.Username;

@Repository
class PaidEntryRepositoryImpl implements PaidEntryRepository {
	private final JdbcTemplate jdbcTemplate;
	private final RowMapper<EntryId> entryIdRowMapper = new RowMapper<EntryId>() {
		@Override
		public EntryId mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new EntryId(rs.getInt("entry_id"));
		}
	};

	public PaidEntryRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<EntryId> entryIdsByUsername(Username username) {
		return this.jdbcTemplate.query(
				"SELECT p.entry_id FROM paid_entry AS p JOIN point_event AS e ON p.event_id = e.event_id WHERE e.username = ? ORDER BY p.entry_id",
				entryIdRowMapper, username.toString());
	}

	@Override
	@Transactional
	public int store(PaidEntry paidEntry) {
		return this.jdbcTemplate.update(
				"INSERT INTO paid_entry(entry_id, event_id) VALUES (?, ?)",
				paidEntry.entryId().value(), paidEntry.eventId().toString());
	}

	@Override
	public boolean existsByEntryIdAndUsername(EntryId entryId, Username username) {
		return this.jdbcTemplate.queryForObject(
				"SELECT count(*) FROM paid_entry AS p JOIN point_event AS e ON p.event_id = e.event_id WHERE p.entry_id = ? AND e.username = ?",
				Integer.class, entryId.value(), username.toString()) > 0;
	}
}
