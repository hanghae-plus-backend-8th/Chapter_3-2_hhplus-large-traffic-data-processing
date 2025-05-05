package kr.hhplus.be.server.application.order;

import jakarta.persistence.EntityManagerFactory;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatistics;
import kr.hhplus.be.server.infrastructure.order.persistence.OrderStatisticsEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStatisticsService {

    private final OrderRepository orderRepository;
    private final EntityManagerFactory entityManagerFactory;

    public void sync(LocalDateTime statDateTime, LocalDateTime endDateTime) {

        // 추후, 페이징 처리 필요
        List<Order> orderList = orderRepository.findAllBy(statDateTime, endDateTime);

        // JPA Stateless Session 시작
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        StatelessSession statelessSession = sessionFactory.openStatelessSession();

        // 트랜잭션 시작
        Transaction tx = statelessSession.beginTransaction();

        try {
            for (Order order : orderList) {
                List<OrderStatistics> orderStatisticsList = OrderStatistics.create(order);

                for (OrderStatistics orderStatistics : orderStatisticsList) {
                    OrderStatisticsEntity orderStatisticsEntity = OrderStatisticsEntity.builder()
                            .orderId(orderStatistics.getOrderId())
                            .productId(orderStatistics.getProductId())
                            .quantity(orderStatistics.getQuantity())
                            .build();

                    statelessSession.insert(orderStatisticsEntity);
                }
            }

            // 트랜잭션 커밋
            tx.commit();
        } catch (Exception e) {

            // 트랜잭션 롤백
            tx.rollback();

            throw e;
        } finally {
            statelessSession.close();
        }
    }
}
