package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.coupon.CouponResult.CouponDownloadResult;
import lombok.Getter;

public class CouponResponse {

    @Getter
    public static class CouponDownloadResponse {

        @Schema(description = "쿠폰명", type = "string", example = "신발 30% 할인 쿠폰")
        private String name;

        @Schema(description = "쿠폰번호", type = "string", example = "3f8c9d22-7a61-4d75-b8d7-5f0b3b58a4d3")
        private String couponNumber;

        public CouponDownloadResponse(CouponDownloadResult couponDownloadResult) {
            this.name = couponDownloadResult.getName();
            this.couponNumber = couponDownloadResult.getCouponNumber();
        }
    }
}
