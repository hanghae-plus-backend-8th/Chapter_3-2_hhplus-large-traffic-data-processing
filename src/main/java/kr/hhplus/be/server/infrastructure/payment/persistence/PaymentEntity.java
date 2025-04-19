package kr.hhplus.be.server.infrastructure.payment.persistence;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.MemberCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.infrastructure.coupon.persistence.MemberCouponEntity;
import kr.hhplus.be.server.infrastructure.order.persistence.OrderEntity;
import kr.hhplus.be.server.shared.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "PAYMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("결제 테이블")
public class PaymentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, updatable = false)
    @Comment("결제 PK")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Comment("주문 PK")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "member_coupon_id", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Comment("쿠폰번호 PK")
    private MemberCouponEntity memberCoupon;

    @Column(name = "total_price", nullable = false)
    @Comment("총 금액")
    private Long totalPrice;

    @Column(name = "discount_price", nullable = false)
    @Comment("할인 금액")
    private Long discountPrice;

    @Column(name = "pay_price", nullable = false)
    @Comment("실 결제 금액")
    private Long payPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Comment("상태")
    private PaymentStatus status;

    @Builder
    private PaymentEntity(Long paymentId, OrderEntity order, MemberCouponEntity memberCoupon, Long totalPrice, Long discountPrice, Long payPrice, PaymentStatus status) {
        this.paymentId = paymentId;
        this.order = order;
        this.memberCoupon = memberCoupon;
        this.totalPrice = totalPrice;
        this.discountPrice = discountPrice;
        this.payPrice = payPrice;
        this.status = status;
    }

    public Payment toDomain() {
        Order orderDomain = order.toDomain();
        MemberCoupon memberCouponDomain = memberCoupon != null ? memberCoupon.toDomain() : null;

        return Payment.of(paymentId, orderDomain, memberCouponDomain, totalPrice, discountPrice, payPrice, status);
    }
}
