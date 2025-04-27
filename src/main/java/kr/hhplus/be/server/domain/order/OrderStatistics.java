package kr.hhplus.be.server.domain.order;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderStatistics {

    private Long id;
    private long orderId;
    private long productId;
    private int quantity;

    private OrderStatistics(Long id, long orderId, long productId, int quantity) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("주문통계 식별자가 유효하지 않습니다.");
        }
        if (orderId <= 0) {
            throw new IllegalArgumentException("주문 식별자가 유효하지 않습니다.");
        }
        if (productId <= 0) {
            throw new IllegalArgumentException("상품 식별자가 유효하지 않습니다.");
        }
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<OrderStatistics> create(Order order) {
        List<OrderStatistics> orderStatisticsList = new ArrayList<>();

        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Long orderId = order.getId();
            long productId = orderProduct.getProductId();
            int quantity = orderProduct.getQuantity();

            orderStatisticsList.add(new OrderStatistics(null, orderId, productId, quantity));
        }
        return orderStatisticsList;
    }

    public static OrderStatistics of(long id, long orderId, long productId, int quantity) {
        return new OrderStatistics(id, orderId, productId, quantity);
    }
}
