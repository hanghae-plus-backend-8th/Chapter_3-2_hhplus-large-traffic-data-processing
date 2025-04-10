package kr.hhplus.be.server.domain.coupon;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

import static kr.hhplus.be.server.domain.coupon.MemberCouponStatus.UN_USED;
import static kr.hhplus.be.server.domain.coupon.MemberCouponStatus.USED;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberCoupon {

    private Long id;
    private Coupon coupon;
    private String couponNumber;
    private long memberId;
    private MemberCouponStatus status;

    public static MemberCoupon create(
            @NonNull Coupon coupon,
            @NonNull String couponNumber,
            long memberId
    ) {
        if (memberId <= 0) {
            throw new IllegalArgumentException("사용자 식별자가 유효하지 않습니다.");
        }
        return new MemberCoupon(null, coupon, couponNumber, memberId, UN_USED);
    }

    public static MemberCoupon of(
            long id,
            @NonNull Coupon coupon,
            @NonNull String couponNumber,
            long memberId,
            @NonNull MemberCouponStatus status
    ) {
        if (id <= 0) {
            throw new IllegalArgumentException("쿠폰번호 식별자가 유효하지 않습니다.");
        }
        if (memberId <= 0) {
            throw new IllegalArgumentException("사용자 식별자가 유효하지 않습니다.");
        }
        return new MemberCoupon(id, coupon, couponNumber, memberId, status);
    }

    public boolean isUsed() {
        return status == USED;
    }

    public boolean isNotMine(long memberId) {
        return this.memberId != memberId;
    }

    public long use(long price, @NonNull LocalDateTime now) {
        if (isUsed()) {
            throw new IllegalArgumentException("이미 사용한 쿠폰입니다.");
        }
        if (coupon.isExpired(now)) {
            throw new IllegalArgumentException("만료된 쿠폰입니다.");
        }
        if (coupon.isNotAvailable(now)) {
            throw new IllegalArgumentException("쿠폰은 아직 사용하실 수 없습니다. 유효기간 시작일을 확인해주세요.");
        }
        status = USED;
        return coupon.calculateDiscount(price);
    }
}
