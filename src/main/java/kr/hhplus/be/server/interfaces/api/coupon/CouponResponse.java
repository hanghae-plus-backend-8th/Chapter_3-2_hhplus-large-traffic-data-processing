package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.coupon.CouponResult;
import kr.hhplus.be.server.application.coupon.CouponResult.CouponDownloadResult;
import kr.hhplus.be.server.application.coupon.CouponResult.CouponListResult;
import kr.hhplus.be.server.domain.coupon.CouponType;
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

    @Getter
    @ArraySchema(schema = @Schema(type = "array", description = "보유 쿠폰 목록 리스트 응답"))
    public static class CouponListResponse {

        @Schema(description = "쿠폰명", type = "string", example = "스타벅스 30% 할인 쿠폰")
        private String name;

        @Schema(description = "쿠폰 타입", type = "string", example = "FIXED")
        private CouponType type;

        @Schema(description = "할인값", type = "integer", example = "30")
        private Integer discountValue;

        @Schema(description = "쿠폰번호", type = "string", example = "3f8c9d22-7a61-4d75-b8d7-5f0b3b58a4d3")
        private String couponNumber;

        public CouponListResponse(CouponListResult couponListResult) {
            this.name = couponListResult.getName();
            this.type = couponListResult.getType();
            this.discountValue = couponListResult.getDiscountValue();
            this.couponNumber = couponListResult.getCouponNumber();
        }
    }
}
