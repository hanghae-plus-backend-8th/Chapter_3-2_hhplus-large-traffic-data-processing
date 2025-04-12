package kr.hhplus.be.server.domain.point;

import java.util.Optional;

public interface PointRepository {

    Optional<MemberPoint> findById(long memberId);
    MemberPoint update(long memberId, long amount);
}
