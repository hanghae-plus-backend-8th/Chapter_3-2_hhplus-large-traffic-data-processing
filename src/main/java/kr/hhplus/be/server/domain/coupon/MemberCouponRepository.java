package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.shared.dto.ListDto;

public interface MemberCouponRepository {

    MemberCoupon save(MemberCoupon memberCoupon);
    MemberCoupon getByCouponNumberLocking(String couponNumber);
    void update(MemberCoupon memberCoupon);
    ListDto<MemberCoupon> list(long memberId, int start, int limit);
}
