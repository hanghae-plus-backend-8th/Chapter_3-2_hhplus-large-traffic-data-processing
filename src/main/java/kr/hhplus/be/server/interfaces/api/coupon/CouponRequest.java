package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.application.coupon.CouponCommand.CouponListCommand;
import lombok.Getter;

public class CouponRequest {

    @Getter
    public static class CouponListRequest {

        @Schema(
                description = "사용자 식별자",
                type = "integer",
                format = "int64",
                example = "1"
        )
        @NotNull
        @Min(value = 1)
        private long memberId;

        @Schema(
            description = "리스트 시작번호",
            type = "integer",
            example = "0"
        )
        @Min(value = 0)
        private int start;

        @Schema(
            description = "리스트 종료번호",
            type = "integer",
            example = "1"
        )
        @Min(value = 1)
        private int limit;

        public CouponListRequest(long memberId, Integer start, Integer limit) {
            this.memberId = memberId;
            this.start = start == null ? 0 : start;
            this.limit = limit == null ? 20 : limit;
        }

        public CouponListCommand toCommand() {
            return new CouponListCommand(memberId, start, limit);
        }
    }
}
