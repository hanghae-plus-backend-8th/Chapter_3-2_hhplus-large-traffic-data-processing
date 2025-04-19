package kr.hhplus.be.server.infrastructure.product.persistence;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.shared.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "PRODUCT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("상품 테이블")
public class ProductEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false, updatable = false)
    @Comment("상품 PK")
    private Long productId;

    @Column(name = "name", length = 100, nullable = false)
    @Comment("상품명")
    private String name;

    @Column(name = "price", nullable = false)
    @Comment("가격")
    private Long price;

    @Column(name = "quantity", nullable = false)
    @Comment("수량")
    private Integer quantity;

    @Builder
    private ProductEntity(Long productId, String name, Long price, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Product toDomain() {
        return Product.of(productId, name, price, quantity);
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
