package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.point.MemberPoint;
import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentTest {

    @DisplayName("보유 포인트가 충분하면 결제 성공한다.")
    @Test
    void processPayment() {
        // given
        long memberId = 1L;
        int purchaseQuantity = 1;
        MemberPoint memberPoint = MemberPoint.of(memberId, 1000L);
        Product product = Product.of(1L, "상품명", 1000L, 10);

        // when
        Order order = Order.create(memberId);
        order.addOrderProduct(product, purchaseQuantity);
        Payment payment = Payment.processPayment(order, null, memberPoint, LocalDateTime.now());

        // then
        assertThat(payment.getTotalPrice()).isEqualTo(product.getPrice());
        assertThat(payment.getDiscountPrice()).isZero();
        assertThat(payment.getPayPrice()).isEqualTo(product.getPrice());
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @DisplayName("보유 포인트가 부족하면 결제 실패한다.")
    @Test
    void processPaymentV2() {
        // given
        long memberId = 1L;
        int purchaseQuantity = 1;
        MemberPoint memberPoint = MemberPoint.of(memberId, 500L);
        Product product = Product.of(1L, "상품명", 1000L, 10);

        // when, then
        Order order = Order.create(memberId);
        order.addOrderProduct(product, purchaseQuantity);
        assertThatThrownBy(() -> Payment.processPayment(order, null, memberPoint, LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("보유 포인트가 부족합니다.");
    }
}