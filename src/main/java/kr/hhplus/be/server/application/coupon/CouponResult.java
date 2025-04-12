package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.MemberCoupon;
import lombok.Getter;

public class CouponResult {

    @Getter
    public static class CouponDownloadResult {
        private String name;
        private String couponNumber;

        public CouponDownloadResult(MemberCoupon memberCoupon) {
            this.name = memberCoupon.getCoupon().getName();
            this.couponNumber = memberCoupon.getCouponNumber();
        }
    }
}
