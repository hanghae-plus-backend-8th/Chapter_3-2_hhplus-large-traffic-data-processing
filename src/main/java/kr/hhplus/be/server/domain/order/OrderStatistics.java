package kr.hhplus.be.server.domain.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderStatistics {

    private Long id;
    private long orderId;
    private long productId;
    private int quantity;

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
