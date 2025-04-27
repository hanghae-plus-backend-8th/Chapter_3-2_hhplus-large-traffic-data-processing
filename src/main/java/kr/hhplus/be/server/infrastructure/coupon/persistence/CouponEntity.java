package kr.hhplus.be.server.infrastructure.coupon.persistence;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.shared.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "COUPON")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("쿠폰 테이블")
public class CouponEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id", nullable = false, updatable = false)
    @Comment("쿠폰 PK")
    private Long couponId;

    @Column(name = "name", length = 100, nullable = false)
    @Comment("쿠폰명")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Comment("쿠폰 타입 (퍼센트, 정률)")
    private CouponType type;

    @Column(name = "discount_value", nullable = false)
    @Comment("할인값")
    private Integer discountValue;

    @Column(name = "initial_quantity", nullable = false)
    @Comment("초기 수량")
    private Integer initialQuantity;

    @Column(name = "remaining_quantity", nullable = false)
    @Comment("잔여 수량")
    private Integer remainingQuantity;

    @Column(name = "start_date", nullable = false)
    @Comment("유효기간 시작일")
    private LocalDateTime startDateTime;

    @Column(name = "end_date", nullable = false)
    @Comment("유효기간 종료일")
    private LocalDateTime endDateTime;

    @Builder
    private CouponEntity(Long couponId, String name, CouponType type, Integer discountValue, Integer initialQuantity, Integer remainingQuantity, LocalDateTime startDate, LocalDateTime endDate) {
        this.couponId = couponId;
        this.name = name;
        this.type = type;
        this.discountValue = discountValue;
        this.initialQuantity = initialQuantity;
        this.remainingQuantity = remainingQuantity;
        this.startDateTime = startDate;
        this.endDateTime = endDate;
    }

    public Coupon toDomain() {
        return Coupon.of(couponId, name, type, discountValue, initialQuantity, remainingQuantity, startDateTime, endDateTime);
    }

    public void update(Coupon coupon) {
        this.name = coupon.getName();
        this.type = coupon.getType();
        this.discountValue = coupon.getDiscountValue();
        this.initialQuantity = coupon.getInitialQuantity();
        this.remainingQuantity = coupon.getRemainingQuantity();
        this.startDateTime = coupon.getStartDateTime();
        this.endDateTime = coupon.getEndDateTime();
    }
}
