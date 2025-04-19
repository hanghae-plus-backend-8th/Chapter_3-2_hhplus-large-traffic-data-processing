package kr.hhplus.be.server.infrastructure.order.persistence;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.OrderStatistics;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "ORDER_STATISTICS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("주문통계 테이블")
public class OrderStatisticsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_staitstics_id", nullable = false, updatable = false)
    @Comment("주문통계 PK")
    private Long orderStatisticsId;

    @Column(name = "order_id", nullable = false)
    @Comment("주문 PK")
    private Long orderId;

    @Column(name = "product_id", nullable = false)
    @Comment("상품 PK")
    private Long productId;

    @Column(name = "quantity", nullable = false)
    @Comment("수량")
    private Integer quantity;

    @Column(name = "created_at", nullable = false, insertable = true, updatable = false)
    @Comment("생성일")
    private LocalDateTime createdAt;

    @Builder
    private OrderStatisticsEntity(Long orderStatisticsId, Long orderId, Long productId, Integer quantity, LocalDateTime createdAt) {
        this.orderStatisticsId = orderStatisticsId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public OrderStatistics toDomain() {
        return OrderStatistics.of(orderStatisticsId, orderId, productId, quantity);
    }
}
