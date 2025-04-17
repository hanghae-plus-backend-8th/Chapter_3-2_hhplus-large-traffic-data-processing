package kr.hhplus.be.server.application.order;

import lombok.Getter;

import java.util.List;

public class OrderCommand {

    @Getter
    public static class OrderCaptureCommand {
        private long memberId;
        private String couponNumber;
        private List<Long> productIds;
        private List<Integer> quantities;

        public OrderCaptureCommand(long memberId, String couponNumber, List<Long> productIds, List<Integer> quantities) {
            this.memberId = memberId;
            this.couponNumber = couponNumber;
            this.productIds = productIds;
            this.quantities = quantities;
        }
    }
}
