package kr.hhplus.be.server.infrastructure.payment.persistence;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.infrastructure.coupon.persistence.MemberCouponEntity;
import kr.hhplus.be.server.infrastructure.coupon.persistence.MemberCouponJpaRepository;
import kr.hhplus.be.server.infrastructure.order.persistence.OrderEntity;
import kr.hhplus.be.server.infrastructure.order.persistence.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final MemberCouponJpaRepository memberCouponJpaRepository;
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        OrderEntity orderEntity = orderJpaRepository.findById(payment.getOrder().getId())
                .orElseThrow();
        MemberCouponEntity memberCoupon = null;

        if (payment.getMemberCoupon() != null) {
            memberCoupon = memberCouponJpaRepository.findByCouponNumber(payment.getMemberCoupon().getCouponNumber())
                    .orElseThrow();
        }
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .order(orderEntity)
                .memberCoupon(memberCoupon)
                .totalPrice(payment.getTotalPrice())
                .discountPrice(payment.getDiscountPrice())
                .payPrice(payment.getPayPrice())
                .status(payment.getStatus())
                .build();

        return paymentJpaRepository.save(paymentEntity)
                .toDomain();
    }
}
