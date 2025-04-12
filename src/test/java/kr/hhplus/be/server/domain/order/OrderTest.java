package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OrderTest {

    @DisplayName("상품을 주문할 수 있다.")
    @Test
    void order() {
        // given
        Product product = Product.of(1L, "상품명", 2000L, 1000);
        int purchaseQuantity = 100;
        Order order = Order.create(1L);

        // when
        order.addOrderProduct(product, purchaseQuantity);

        // then
        assertThat(order.getOrderProducts()).hasSize(1)
                .extracting("productId", "price", "quantity")
                .containsExactlyInAnyOrder(
                        tuple(product.getId(), product.getPrice(), purchaseQuantity)
                );
    }

    @DisplayName("상품 재고가 부족하면 주문할 수 없다.")
    @Test
    void inSufficientStock() {
        // given
        Product product = Product.of(1L, "상품명", 2000L, 50);
        int purchaseQuantity = 100;
        Order order = Order.create(1L);

        // when, then
        assertThatThrownBy(() -> order.addOrderProduct(product, purchaseQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 재고가 부족합니다.");
    }

    @DisplayName("최소 주문 수량 미달 시, 주문할 수 없다.")
    @Test
    void inValidPurchaseQuantity() {
        // given
        Product product = Product.of(1L, "상품명", 2000L, 1000);
        int purchaseQuantity = 0;
        Order order = Order.create(1L);

        // when
        assertThatThrownBy(() -> order.addOrderProduct(product, purchaseQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구매 수량이 유효하지 않습니다. 최소 수량을 확인해주세요.");
    }
}