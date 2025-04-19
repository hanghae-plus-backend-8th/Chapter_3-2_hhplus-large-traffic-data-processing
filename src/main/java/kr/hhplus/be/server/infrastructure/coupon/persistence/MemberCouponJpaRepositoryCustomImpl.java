package kr.hhplus.be.server.infrastructure.coupon.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.coupon.MemberCoupon;
import kr.hhplus.be.server.shared.dto.ListDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.hhplus.be.server.infrastructure.coupon.persistence.QMemberCouponEntity.memberCouponEntity;
import static kr.hhplus.be.server.infrastructure.member.persistence.QMemberEntity.memberEntity;

@RequiredArgsConstructor
public class MemberCouponJpaRepositoryCustomImpl implements MemberCouponJpaRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public ListDto<MemberCouponEntity> list(long memberId, int start, int limit) {
        List<MemberCouponEntity> list = query
                .select(memberCouponEntity)
                .from(memberCouponEntity)
                .innerJoin(memberCouponEntity.member, memberEntity).fetchJoin()
                .where(
                        memberIdEq(memberId)
                )
                .orderBy(memberCouponEntity.memberCouponId.desc())
                .offset(start)
                .limit(limit)
                .fetch();

        Long totalCount = query
                .select(memberCouponEntity.count())
                .from(memberCouponEntity)
                .where(
                        memberIdEq(memberId)
                )
                .fetchOne();

        return new ListDto<>(list, totalCount);
    }

    private BooleanExpression memberIdEq(long memberId) {
        return memberCouponEntity.member.memberId.eq(memberId);
    }
}
