package am.ik.blog.point.domain;

public interface PointEventRepository {
	int countTotalPoint(Username username);

	PointEvents findByUsername(Username username);

	int store(PointEvent pointEvent);
}
