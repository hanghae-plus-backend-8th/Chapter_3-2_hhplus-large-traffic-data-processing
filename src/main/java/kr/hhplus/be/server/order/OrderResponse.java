package kr.hhplus.be.server.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponse {

    @Schema(description = "결제 식별자", type = "integer", format = "int64", example = "1")
    private long paymentId;

    @Schema(description = "주문 식별자", type = "integer", format = "int64", example = "2")
    private long orderId;

    @Schema(description = "총 결제금액", type = "integer", format = "int64", example = "3000")
    private long totalPrice;

    @Schema(description = "할인 금액", type = "integer", format = "int64", example = "2000")
    private long discountPrice;

    @Schema(description = "실 결제금액", type = "integer", format = "int64", example = "1000")
    private long payPrice;
}
