package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.MemberPoint;
import lombok.Getter;

public class PointResult {

    @Getter
    public static class PointChargeResult {
        private long memberId;
        private long point;

        public PointChargeResult(MemberPoint memberPoint) {
            this.memberId = memberPoint.getMemberId();
            this.point = memberPoint.getPoint();
        }
    }
}
