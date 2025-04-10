package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface MemberCouponRepository {

    MemberCoupon save(MemberCoupon memberCoupon);
    Optional<MemberCoupon> findByCouponNumber(String couponNumber);
    void update(MemberCoupon memberCoupon);
}
