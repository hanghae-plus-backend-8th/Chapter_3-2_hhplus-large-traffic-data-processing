package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.interfaces.api.order.OrderRequest.OrderCaptureRequest;
import lombok.Getter;

import java.util.List;

public class OrderCommand {

    @Getter
    public static class OrderCaptureCommand {
        private long memberId;
        private String couponNumber;
        private List<Long> productIds;
        private List<Integer> quantities;

        public OrderCaptureCommand(OrderCaptureRequest orderCaptureRequest) {
            this.memberId = orderCaptureRequest.getMemberId();
            this.couponNumber = orderCaptureRequest.getCouponNumber();
            this.productIds = orderCaptureRequest.getProductIds();
            this.quantities = orderCaptureRequest.getQuantities();
        }
    }
}
