package kr.hhplus.be.server.infrastructure.point.persistence;

import kr.hhplus.be.server.domain.point.MemberPoint;
import kr.hhplus.be.server.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DummyPointRepository implements PointRepository {

    @Override
    public Optional<MemberPoint> findById(long memberId) {
        return Optional.empty();
    }

    @Override
    public MemberPoint update(long memberId, long amount) {
        return null;
    }
}
