package kr.hhplus.be.server.domain.point;

public interface PointRepository {

    MemberPoint getById(long memberId);
    void updatePoint(long memberId, long point);
}
