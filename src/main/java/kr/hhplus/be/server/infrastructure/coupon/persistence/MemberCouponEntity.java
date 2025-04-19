package kr.hhplus.be.server.infrastructure.coupon.persistence;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.MemberCoupon;
import kr.hhplus.be.server.domain.coupon.MemberCouponStatus;
import kr.hhplus.be.server.domain.member.Member;
import kr.hhplus.be.server.infrastructure.member.persistence.MemberEntity;
import kr.hhplus.be.server.shared.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "MEMBER_COUPON")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("회원쿠폰 테이블")
public class MemberCouponEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_coupon_id", nullable = false, updatable = false)
    @Comment("쿠폰번호 PK")
    private Long memberCouponId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coupon_id", nullable = false)
    @Comment("쿠폰 PK")
    private CouponEntity coupon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("회원 PK")
    private MemberEntity member;

    @Column(name = "coupon_number", length = 36, nullable = false, unique = true)
    @Comment("쿠폰번호")
    private String couponNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Comment("상태")
    private MemberCouponStatus status;

    @Builder
    private MemberCouponEntity(Long memberCouponId, CouponEntity coupon, MemberEntity member, String couponNumber, MemberCouponStatus status) {
        this.memberCouponId = memberCouponId;
        this.coupon = coupon;
        this.member = member;
        this.couponNumber = couponNumber;
        this.status = status;
    }

    public MemberCoupon toDomain() {
        Coupon couponDomain = coupon.toDomain();
        Member memberDomain = member.toMemberDomain();

        return MemberCoupon.of(memberCouponId, couponDomain, couponNumber, memberDomain.getMemberId(), status);
    }

    public void updateStatus(MemberCouponStatus status) {
        this.status = status;
    }
}
