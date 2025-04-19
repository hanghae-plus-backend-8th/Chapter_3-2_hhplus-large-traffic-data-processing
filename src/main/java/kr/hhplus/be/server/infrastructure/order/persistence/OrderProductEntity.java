package kr.hhplus.be.server.infrastructure.order.persistence;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.infrastructure.product.persistence.ProductEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "ORDER_PRODUCT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("주문상품 테이블")
public class OrderProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id", nullable = false, updatable = false)
    @Comment("주문상품 PK")
    private Long orderProductId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @Comment("주문 PK")
    private OrderEntity order;

    @Column(name = "product_id", nullable = false)
    @Comment("상품 PK")
    private Long productId;

    @Column(name = "price", nullable = false)
    @Comment("가격")
    private Long price;

    @Column(name = "quantity", nullable = false)
    @Comment("수량")
    private Integer quantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, insertable = true, updatable = false)
    @Comment("생성일")
    private LocalDateTime createdAt;

    @Builder
    private OrderProductEntity(Long orderProductId, OrderEntity order, Long productId, Long price, Integer quantity) {
        this.orderProductId = orderProductId;
        this.order = order;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }
}
