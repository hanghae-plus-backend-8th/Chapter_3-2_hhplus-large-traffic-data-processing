package kr.hhplus.be.server.domain.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderProduct {

    private long productId;
    private long price;
    private int quantity;

    public static OrderProduct of(long productId, long price, int quantity) {
        if (productId <= 0) {
            throw new IllegalArgumentException("상품 식별자가 유효하지 않습니다.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 금액입니다. 최소 금액을 확인해주세요.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("구매 수량이 유효하지 않습니다.");
        }
        return new OrderProduct(productId, price, quantity);
    }
}
