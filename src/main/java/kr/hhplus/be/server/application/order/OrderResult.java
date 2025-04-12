package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import lombok.Getter;

public class OrderResult {

    @Getter
    public static class OrderCaptureResult {
        private long paymentId;
        private long orderId;
        private long totalPrice;
        private long discountPrice;
        private long payPrice;

        public OrderCaptureResult(Payment payment, Order order) {
            this.paymentId = payment.getId();
            this.orderId = order.getId();
            this.totalPrice = payment.getTotalPrice();
            this.discountPrice = payment.getDiscountPrice();
            this.payPrice = payment.getPayPrice();
        }
    }
}
