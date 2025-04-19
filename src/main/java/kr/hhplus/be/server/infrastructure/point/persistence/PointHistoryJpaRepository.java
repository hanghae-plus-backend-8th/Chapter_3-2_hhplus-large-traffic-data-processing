package kr.hhplus.be.server.infrastructure.point.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistoryEntity, Long> {
}
