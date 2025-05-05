package kr.hhplus.be.server.domain.order;

import lombok.Getter;

@Getter
public class CartProduct {

    private long productId;
    private int quantity;

    private CartProduct(long productId, int quantity) {
        if (productId <= 0) {
            throw new IllegalArgumentException("상품 식별자가 유효하지 않습니다.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량이 유효하지 않습니다.");
        }
        this.productId = productId;
        this.quantity = quantity;
    }

    public static CartProduct of(long productId, int quantity) {
        return new CartProduct(productId, quantity);
    }
}
