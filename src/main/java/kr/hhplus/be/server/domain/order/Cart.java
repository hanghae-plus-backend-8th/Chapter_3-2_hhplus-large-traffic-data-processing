package kr.hhplus.be.server.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class Cart {

    List<CartProduct> cartProducts;

    public static Cart create() {
        return new Cart(new ArrayList<>());
    }

    public void addCartProduct(long productId, int quantity) {
        cartProducts.add(CartProduct.of(productId, quantity));
    }

    public void addCartProducts(@NonNull List<Long> productIds, @NonNull List<Integer> quantities) {
        if (productIds.isEmpty()) {
            throw new IllegalArgumentException("상품 식별자 목록이 비어있습니다.");
        }
        if (quantities.isEmpty()) {
            throw new IllegalArgumentException("상품 수량 목록이 비어있습니다.");
        }
        if (productIds.size() != quantities.size()) {
            throw new IllegalArgumentException("상품 식별자 목록과 수량 목록 개수가 일치하지 않습니다.");
        }
        for (int i = 0; i < productIds.size(); i++) {
            addCartProduct(productIds.get(i), quantities.get(i));
        }
    }

    public int getCartProductCount() {
        return cartProducts.size();
    }
}
