package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.order.OrderResult.OrderCaptureResult;
import lombok.Getter;

public class OrderResponse {

    @Getter
    public static class OrderCaptureResponse {

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

        public OrderCaptureResponse(OrderCaptureResult orderCaptureResult) {
            this.paymentId = orderCaptureResult.getPaymentId();
            this.orderId = orderCaptureResult.getOrderId();
            this.totalPrice = orderCaptureResult.getTotalPrice();
            this.discountPrice = orderCaptureResult.getDiscountPrice();
            this.payPrice = orderCaptureResult.getPayPrice();
        }
    }
}
