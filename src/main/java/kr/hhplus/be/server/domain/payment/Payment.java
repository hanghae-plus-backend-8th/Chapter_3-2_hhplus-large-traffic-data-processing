package kr.hhplus.be.server.domain.payment;

import jakarta.annotation.Nullable;
import kr.hhplus.be.server.domain.coupon.MemberCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.point.MemberPoint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

import static kr.hhplus.be.server.domain.payment.PaymentStatus.COMPLETED;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Payment {

    private Long id;
    private Order order;
    private MemberCoupon memberCoupon;
    private long totalPrice;
    private long discountPrice;
    private long payPrice;
    private PaymentStatus status;

    public static Payment processPayment(
            @NonNull Order order,
            @Nullable MemberCoupon memberCoupon,
            @NonNull MemberPoint memberPoint,
            @NonNull LocalDateTime now
    ) {
        long totalPrice = order.getTotalPrice();
        long discountPrice = memberCoupon != null
                ? memberCoupon.use(order.getTotalPrice(), now)
                : 0L;
        long payPrice = totalPrice - discountPrice;

        memberPoint.use(payPrice);
        order.complete();

        return new Payment(null, order, memberCoupon, totalPrice, discountPrice, payPrice, COMPLETED);
    }
}
