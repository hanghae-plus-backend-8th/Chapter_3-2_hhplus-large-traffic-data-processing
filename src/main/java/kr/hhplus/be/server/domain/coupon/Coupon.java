package kr.hhplus.be.server.domain.coupon;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

import static kr.hhplus.be.server.domain.coupon.CouponType.PERCENT;

@Getter
public class Coupon {

    private Long id;
    private String name;
    private CouponType type;
    private int discountValue;
    private int initialQuantity;
    private int remainingQuantity;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    public static final int MAX_PERCENT_DISCOUNT = 80;

    private Coupon(
            @Nullable Long id,
            @NonNull String name,
            @NonNull CouponType type,
            int discountValue,
            int initialQuantity,
            int remainingQuantity,
            @NonNull LocalDateTime startDateTime,
            @NonNull LocalDateTime endDateTime
    ) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("쿠폰 식별자가 유효하지 않습니다.");
        }
        if (type == PERCENT && discountValue > MAX_PERCENT_DISCOUNT) {
            throw new IllegalArgumentException("퍼센트 할인은 최대 80%까지 가능합니다.");
        }
        if (discountValue <= 0) {
            throw new IllegalArgumentException("할인율이 유효하지 않습니다.");
        }
        if (initialQuantity <= 0) {
            throw new IllegalArgumentException("초기 수량이 유효하지 않습니다.");
        }
        if (id == null && (remainingQuantity < 0 || remainingQuantity != initialQuantity)) {
            throw new IllegalArgumentException("쿠폰 생성 시, 초기 수량과 잔여 수량은 일치해야 합니다.");
        }
        if (id != null && remainingQuantity <= 0) {
            throw new IllegalArgumentException("잔여 수량이 유효하지 않습니다.");
        }
        if (startDateTime.isEqual(endDateTime) || startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("유효기간 종료일이 시작일보다 빠를 수 없습니다.");
        }
        this.id = id;
        this.name = name;
        this.type = type;
        this.discountValue = discountValue;
        this.initialQuantity = initialQuantity;
        this.remainingQuantity = remainingQuantity;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public static Coupon create(
            @NonNull String name,
            @NonNull CouponType type,
            int discountValue,
            int initialQuantity,
            @NonNull LocalDateTime startDateTime,
            @NonNull LocalDateTime endDateTime
    ) {
        return new Coupon(null, name, type, discountValue, initialQuantity, initialQuantity, startDateTime, endDateTime);
    }

    public static Coupon of(
            long id,
            @NonNull String name,
            @NonNull CouponType type,
            int discountValue,
            int initialQuantity,
            int remainingQuantity,
            @NonNull LocalDateTime startDateTime,
            @NonNull LocalDateTime endDateTime
    ) {
        return new Coupon(id, name, type, discountValue, initialQuantity, remainingQuantity, startDateTime, endDateTime);
    }

    public long calculateDiscount(long price) {
        if (price <= 0) {
            throw new IllegalArgumentException("상품 금액이 유효하지 않습니다.");
        }

        // 퍼센트 할인
        if (type == PERCENT) {
            return (long) (price * (discountValue / 100.0));
        }

        // 정률 할인
        else {
            return (price <= discountValue) ? price : discountValue;
        }
    }

    public boolean isNotAvailable(@NonNull LocalDateTime now) {
        return now.isBefore(startDateTime);
    }

    public boolean isExpired(@NonNull LocalDateTime now) {
        return now.isAfter(endDateTime);
    }

    public MemberCoupon giveCoupon(long memberId, @NonNull LocalDateTime now) {
        if (isExpired(now)) {
            throw new IllegalArgumentException("유효 기간이 만료된 쿠폰입니다.");
        }
        if (remainingQuantity == 0) {
            throw new IllegalArgumentException("쿠폰 잔여 수량이 부족합니다.");
        }
        remainingQuantity--;
        return MemberCoupon.create(this, UUID.randomUUID().toString(), memberId);
    }
}
