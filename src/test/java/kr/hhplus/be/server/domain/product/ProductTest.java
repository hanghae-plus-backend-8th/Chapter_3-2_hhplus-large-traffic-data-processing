package kr.hhplus.be.server.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    // 상품 수량이 음수인 경우 + 상품 아이디가 음수인 경우는 상식적으로 맞지 않기 때문에 테스트를 작성하지 않았습니다.
    // 이러한 부분도 테스트해야 하는지 궁금합니다.
    @DisplayName("무료 상품은 등록할 수 없다.")
    @Test
    void registerFreeProduct() {
        // given, when, then
        assertThatThrownBy(() -> Product.create("에어맥스", 0L, 50))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 재고를 추가할 수 있다.")
    @Test
    void increaseStock() {
        // given
        Product product = Product.of(3L, "에어맥스", 5000L, 30);

        // when
        product.increaseStock(5);

        // then
        assertThat(product.getQuantity()).isEqualTo(35);
    }

    @DisplayName("재고 추가 수량은 1개 이상이어야 한다.")
    @Test
    void increaseInvalidStock() {
        // given
        Product product = Product.of(3L, "에어맥스", 5000L, 30);

        // when, then
        assertThatThrownBy(() -> product.increaseStock(-50))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("보유 수량만큼 상품의 재고를 차감할 수 있다.")
    @Test
    void decreaseStock() {
        // given
        Product product = Product.of(3L, "에어맥스", 5000L, 30);

        // when
        product.decreaseStock(30);

        // then
        assertThat(product.getQuantity()).isEqualTo(0);
    }

    @DisplayName("재고 차감 수량은 0 이하일 수 없다.")
    @Test
    void decreaseInvalidStock() {
        // given
        Product product = Product.of(3L, "에어맥스", 5000L, 30);

        // when, then
        assertThatThrownBy(() -> product.decreaseStock(-50))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("재고보다 많은 수량을 차감할 수 없다.")
    @Test
    void decreaseExceedsStock() {
        // given
        Product product = Product.of(3L, "에어맥스", 5000L, 30);

        // when, then
        assertThatThrownBy(() -> product.decreaseStock(50))
                .isInstanceOf(IllegalArgumentException.class);
    }
}