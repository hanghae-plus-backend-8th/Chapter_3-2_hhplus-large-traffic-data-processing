package kr.hhplus.be.server.infrastructure.coupon.persistence;

import kr.hhplus.be.server.domain.coupon.MemberCoupon;
import kr.hhplus.be.server.domain.coupon.MemberCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DummyMemberCouponRepository implements MemberCouponRepository {

    @Override
    public MemberCoupon save(MemberCoupon memberCoupon) {
        return null;
    }

    @Override
    public Optional<MemberCoupon> findByCouponNumber(String couponNumber) {
        return Optional.empty();
    }

    @Override
    public void update(MemberCoupon memberCoupon) {

    }
}
