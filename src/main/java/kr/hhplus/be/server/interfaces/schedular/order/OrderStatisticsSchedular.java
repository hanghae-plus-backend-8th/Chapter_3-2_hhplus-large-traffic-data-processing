package kr.hhplus.be.server.interfaces.schedular.order;

import kr.hhplus.be.server.application.order.OrderStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
//@Component
@RequiredArgsConstructor
public class OrderStatisticsSchedular {

    private final OrderStatisticsService orderStatisticsService;

    // 매일 새벽 1시에 배치 실행!
    @Scheduled(cron = "0 0 1 * * *")
    public void orderStatistics() {
        LocalDate yesterday = LocalDate.now()
                .minusDays(1L);

        orderStatisticsService.sync(yesterday.atStartOfDay(), yesterday.atTime(23, 59, 59));
    }
}
