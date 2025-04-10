package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class OrderRequest {

    @Getter
    public static class OrderCaptureRequest {

        @Schema(description = "사용자 식별자",
                type = "integer",
                format = "int64",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Min(1)
        private long memberId;

        @Schema(description = "쿠폰번호",
                type = "string",
                example = "3f8c9d22-7a61-4d75-b8d7-5f0b3b58a4d3",
                requiredMode = Schema.RequiredMode.REQUIRED)
        private String couponNumber;

        @ArraySchema(
                arraySchema = @Schema(description = "상품 ID 목록", type = "array"),
                schema = @Schema(type = "int", format = "int64", example = "1")
        )
        @NotNull
        @NotEmpty
        private List<@NotNull @Min(1) Long> productIds;

        @ArraySchema(
                arraySchema = @Schema(description = "상품 수량 목록", type = "array"),
                schema = @Schema(type = "int", format = "int64", example = "1")
        )
        @NotNull
        @NotEmpty
        private List<@NotNull @Min(1) Integer> quantities;

        public boolean isNotValid() {
            return productIds.size() != quantities.size();
        }
    }
}
