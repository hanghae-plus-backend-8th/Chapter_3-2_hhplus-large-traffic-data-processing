package kr.hhplus.be.server.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponResponse {

    @Schema(description = "사용자 식별자", type = "integer", format = "int64", example = "1")
    private long userId;

    @Schema(description = "쿠폰 식별자", type = "integer", format = "int64", example = "1")
    private long couponId;

    @Schema(description = "쿠폰번호", type = "string", example = "14fb077d99e1418f841c")
    private String couponNumber;
}
