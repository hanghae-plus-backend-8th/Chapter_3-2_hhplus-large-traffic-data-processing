package kr.hhplus.be.server.application.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.domain.coupon.CouponType;
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

    @Getter
    public static class CouponListResult {
        private String name;
        private CouponType type;
        private Integer discountValue;
        private String couponNumber;

        public CouponListResult(MemberCoupon memberCoupon) {
            this.name = memberCoupon.getCoupon().getName();
            this.type = memberCoupon.getCoupon().getType();
            this.discountValue = memberCoupon.getCoupon().getDiscountValue();
            this.couponNumber = memberCoupon.getCouponNumber();
        }
    }
}
