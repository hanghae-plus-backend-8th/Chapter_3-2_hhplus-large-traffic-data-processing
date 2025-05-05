package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Service
@NoArgsConstructor(access = PROTECTED)
public class OrderManager {

    public Order createOrder(@NonNull List<Product> products, @NonNull Cart cart, long memberId) {
        if (products.isEmpty() || products.size() != cart.getCartProductCount()) {
            throw new IllegalArgumentException("일부 상품을 찾을 수 없습니다. 다시 확인해주세요.");
        }
        Order order = Order.create(memberId);

        for (CartProduct cartProduct : cart.getCartProducts()) {
            Product product = products.stream()
                    .filter(productInfo -> productInfo.getId().equals(cartProduct.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("일부 상품을 찾을 수 없습니다. 다시 확인해주세요."));

            order.addOrderProduct(product, cartProduct.getQuantity());
        }
        return order;
    }
}
