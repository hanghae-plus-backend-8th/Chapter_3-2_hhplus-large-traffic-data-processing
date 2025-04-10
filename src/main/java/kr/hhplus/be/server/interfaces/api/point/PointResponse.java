package kr.hhplus.be.server.interfaces.api.point;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.point.PointResult.PointChargeResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PointResponse {

    @Getter
    @AllArgsConstructor
    public static class PointChargeResponse {

        @Schema(description = "사용자 식별자", type = "integer", format = "int64", example = "1")
        private long memberId;

        @Schema(description = "포인트", type = "integer", format = "int64", example = "5000")
        private long point;

        public PointChargeResponse(PointChargeResult pointChargeResult) {
            this.memberId = pointChargeResult.getMemberId();
            this.point = pointChargeResult.getPoint();
        }
    }
}
