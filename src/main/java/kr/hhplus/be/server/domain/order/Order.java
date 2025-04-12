package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

import static kr.hhplus.be.server.domain.order.OrderStatus.CREATED;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order {

    private Long id;
    private long memberId;
    private List<OrderProduct> orderProducts;
    private long totalPrice;
    private OrderStatus status;

    public static Order create(long memberId) {
        if (memberId <= 0) {
            throw new IllegalArgumentException("사용자 식별자가 유효하지 않습니다.");
        }
        return new Order(null, memberId, new ArrayList<>(), 0L, CREATED);
    }

    public static Order of(
            long id,
            long memberId,
            @NonNull List<OrderProduct> orderProducts,
            @NonNull OrderStatus status
    ) {
        if (id <= 0) {
            throw new IllegalArgumentException("주문 식별자가 유효하지 않습니다.");
        }
        if (memberId <= 0) {
            throw new IllegalArgumentException("사용자 식별자가 유효하지 않습니다.");
        }
        if (orderProducts.isEmpty()) {
            throw new IllegalArgumentException("주문 상품 목록이 비어있습니다.");
        }
        long totalPrice = orderProducts.stream()
                .mapToLong(orderProduct -> orderProduct.getPrice() * orderProduct.getQuantity())
                .sum();

        return new Order(id, memberId, orderProducts, totalPrice, status);
    }

    public void addOrderProduct(@NonNull Product product, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("구매 수량이 유효하지 않습니다. 최소 수량을 확인해주세요.");
        }
        if (product.isNotSufficient(amount)) {
            throw new IllegalArgumentException("상품 재고가 부족합니다.");
        }
        this.totalPrice += product.getPrice() * amount;
        this.orderProducts.add(OrderProduct.of(product.getId(), product.getPrice(), amount));
    }

    public void complete() {
        this.status = OrderStatus.COMPLETED;
    }
}
