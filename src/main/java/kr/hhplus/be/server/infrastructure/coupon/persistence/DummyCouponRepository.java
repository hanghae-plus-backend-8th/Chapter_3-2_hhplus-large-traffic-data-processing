package kr.hhplus.be.server.infrastructure.coupon.persistence;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DummyCouponRepository implements CouponRepository {

    @Override
    public Optional<Coupon> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void update(long couponId, int remainingQuantity) {

    }
}
