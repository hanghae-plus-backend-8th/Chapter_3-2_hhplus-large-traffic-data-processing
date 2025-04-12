package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

/**
 * # 회원 쿠폰 상태
 * - USED    : 사용
 * - UN_USED : 미사용
 */
@Getter
public enum MemberCouponStatus {

    USED,
    UN_USED,
}
