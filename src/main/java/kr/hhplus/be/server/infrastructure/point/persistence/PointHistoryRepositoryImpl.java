package kr.hhplus.be.server.infrastructure.point.persistence;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointHistoryRepository;
import kr.hhplus.be.server.infrastructure.member.persistence.MemberEntity;
import kr.hhplus.be.server.infrastructure.member.persistence.MemberJpaRepository;
import kr.hhplus.be.server.shared.exception.NotFoundResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final PointHistoryJpaRepository pointHistoryJpaRepository;

    @Override
    public PointHistory save(PointHistory pointHistory) {
        MemberEntity memberEntity = memberJpaRepository.findById(pointHistory.getMemberId())
                .orElseThrow(() -> new NotFoundResourceException("유효하지 않은 사용자 식별자입니다."));
        PointHistoryEntity pointHistoryEntity = PointHistoryEntity.builder()
                .member(memberEntity)
                .type(pointHistory.getType())
                .amount(pointHistory.getAmount())
                .build();

        return pointHistoryJpaRepository.save(pointHistoryEntity)
                .toDomain();
    }
}
