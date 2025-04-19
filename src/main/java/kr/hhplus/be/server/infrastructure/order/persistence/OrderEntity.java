package kr.hhplus.be.server.infrastructure.order.persistence;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderProduct;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.infrastructure.member.persistence.MemberEntity;
import kr.hhplus.be.server.shared.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "ORDERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("주문 테이블")
public class OrderEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false, updatable = false)
    @Comment("주문 PK")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("회원 PK")
    private MemberEntity member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProductEntity> orderProducts = new ArrayList<>();

    @Column(name = "total_price", nullable = false)
    @Comment("주문 금액")
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Comment("주문 상태")
    private OrderStatus status;

    @Builder
    private OrderEntity(Long orderId, MemberEntity member, List<OrderProductEntity> orderProducts, Long totalPrice, OrderStatus status) {
        this.orderId = orderId;
        this.member = member;
        this.orderProducts = orderProducts;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Order toDomain() {
        List<OrderProduct> orderProductList = orderProducts.stream()
                .map(orderProduct -> OrderProduct.of(orderProduct.getProductId(), orderProduct.getPrice(), orderProduct.getQuantity()))
                .toList();

        return Order.of(orderId, member.getMemberId(), orderProductList, status);
    }

    public void addOrderProduct(OrderProductEntity orderProduct) {
        if (orderProducts == null) {
            this.orderProducts = new ArrayList<>(List.of(orderProduct));
        }
        else {
            this.orderProducts.add(orderProduct);
        }
    }
}
