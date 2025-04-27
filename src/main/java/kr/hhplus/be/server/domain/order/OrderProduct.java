package kr.hhplus.be.server.domain.order;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class OrderProduct {

    private Long id;
    private long productId;
    private long price;
    private int quantity;

    private OrderProduct(@Nullable Long id, long productId, long price, int quantity) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("주문상품 식별자가 유효하지 않습니다.");
        }
        if (productId <= 0) {
            throw new IllegalArgumentException("상품 식별자가 유효하지 않습니다.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 금액입니다. 최소 금액을 확인해주세요.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("구매 수량이 유효하지 않습니다.");
        }
        this.id = id;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderProduct create(long productId, long price, int quantity) {
        return new OrderProduct(null, productId, price, quantity);
    }

    public static OrderProduct of(long id, long productId, long price, int quantity) {
        return new OrderProduct(id, productId, price, quantity);
    }
}
