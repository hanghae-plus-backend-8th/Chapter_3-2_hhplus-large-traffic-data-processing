package kr.hhplus.be.server.infrastructure.order.persistence;

import kr.hhplus.be.server.domain.order.OrderStatistics;
import kr.hhplus.be.server.domain.order.OrderStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderStatisticsRepositoryImpl implements OrderStatisticsRepository {

    private final OrderStatisticsJpaRepository orderStatisticsJpaRepository;

    @Override
    public OrderStatistics save(OrderStatistics orderStatistics, LocalDateTime syncTime) {
        OrderStatisticsEntity orderStatisticsEntity = OrderStatisticsEntity.builder()
                .orderId(orderStatistics.getOrderId())
                .productId(orderStatistics.getProductId())
                .quantity(orderStatistics.getQuantity())
                .createdAt(syncTime)
                .build();

        return orderStatisticsJpaRepository.save(orderStatisticsEntity)
                .toDomain();
    }
}
