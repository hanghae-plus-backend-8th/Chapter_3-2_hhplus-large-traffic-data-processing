package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

/**
 * # 쿠폰 타입
 * - PERCENT : 퍼센트
 * - FIXED   : 정률
 */
@Getter
public enum CouponType {
    PERCENT,
    FIXED,
}
