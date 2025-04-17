package kr.hhplus.be.server.application.coupon;

import lombok.Getter;

public class CouponCommand {

    @Getter
    public static class CouponListCommand {
        private long memberId;
        private int start;
        private int limit;

        public CouponListCommand(long memberId, int start, int limit) {
            this.memberId = memberId;
            this.start = start;
            this.limit = limit;
        }
    }
}
