package kr.hhplus.be.server.infrastructure.coupon.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberCouponJpaRepository extends JpaRepository<MemberCouponEntity, Long>, MemberCouponJpaRepositoryCustom {

    Optional<MemberCouponEntity> findByCouponNumber(String couponNumber);
}
