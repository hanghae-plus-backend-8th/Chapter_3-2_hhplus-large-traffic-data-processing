package kr.hhplus.be.server.domain.order;

import java.time.LocalDateTime;

public interface OrderStatisticsRepository {

    OrderStatistics save(OrderStatistics orderStatistics, LocalDateTime syncTime);
}
