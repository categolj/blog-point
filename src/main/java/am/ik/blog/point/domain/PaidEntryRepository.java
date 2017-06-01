package am.ik.blog.point.domain;

import java.util.List;

public interface PaidEntryRepository {
	List<EntryId> entryIdsByUsername(Username username);

	int store(PaidEntry paidEntry);

	boolean existsByEntryIdAndUsername(EntryId entryId, Username username);
}
