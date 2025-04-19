package kr.hhplus.be.server.infrastructure.coupon.persistence;

import kr.hhplus.be.server.shared.dto.ListDto;

public interface MemberCouponJpaRepositoryCustom {

    ListDto<MemberCouponEntity> list(long memberId, int start, int limit);
}
