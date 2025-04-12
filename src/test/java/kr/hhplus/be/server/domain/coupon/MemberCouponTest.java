package kr.hhplus.be.server.domain.coupon;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static kr.hhplus.be.server.domain.coupon.CouponType.FIXED;
import static kr.hhplus.be.server.domain.coupon.CouponType.PERCENT;
import static kr.hhplus.be.server.domain.coupon.MemberCouponStatus.UN_USED;
import static kr.hhplus.be.server.domain.coupon.MemberCouponStatus.USED;
import static org.assertj.core.api.Assertions.assertThat;

class MemberCouponTest {

    @DisplayName("쿠폰을 사용하여 할인받을 수 있다.")
    @Test
    void useCoupon() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime now = LocalDateTime.of(2024, 5, 1, 0, 0);
        Coupon coupon = Coupon.create("쿠폰명", FIXED, 1000, 10, startDateTime, endDateTime);
        MemberCoupon memberCoupon = MemberCoupon.of(1L, coupon, UUID.randomUUID().toString(), 1L, UN_USED);

        // when
        long discountPrice = memberCoupon.use(2000L, now);

        // then
        assertThat(discountPrice).isEqualTo(1000L);
        assertThat(memberCoupon.getStatus()).isEqualTo(USED);
    }

    @DisplayName("유효기간이 만료된 쿠폰은 사용할 수 없다.")
    @Test
    void expiredCoupon() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime now = LocalDateTime.of(2026, 1, 1, 0, 0);
        Coupon coupon = Coupon.create("쿠폰명", PERCENT, 10, 10, startDateTime, endDateTime);
        MemberCoupon memberCoupon = MemberCoupon.of(1L, coupon, UUID.randomUUID().toString(), 1L, UN_USED);

        // when, then
        Assertions.assertThatThrownBy(() -> memberCoupon.use(2000L, now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("만료된 쿠폰입니다.");
    }

    @DisplayName("유효기간 시작일이 지나기 전까지 쿠폰은 사용할 수 없다.")
    @Test
    void unAvailableCoupon() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime now = LocalDateTime.of(2023, 1, 1, 0, 0);
        Coupon coupon = Coupon.create("쿠폰명", PERCENT, 10, 10, startDateTime, endDateTime);
        MemberCoupon memberCoupon = MemberCoupon.of(1L, coupon, UUID.randomUUID().toString(), 1L, UN_USED);

        // when, then
        Assertions.assertThatThrownBy(() -> memberCoupon.use(2000L, now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("쿠폰은 아직 사용하실 수 없습니다. 유효기간 시작일을 확인해주세요.");
    }

    @DisplayName("이미 사용한 쿠폰은 다시 사용할 수 없다.")
    @Test
    void usedCoupon() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime now = LocalDateTime.of(2024, 5, 1, 0, 0);
        Coupon coupon = Coupon.create("쿠폰명", PERCENT, 10, 10, startDateTime, endDateTime);
        MemberCoupon memberCoupon = MemberCoupon.of(1L, coupon, UUID.randomUUID().toString(), 1L, USED);

        // when, then
        Assertions.assertThatThrownBy(() -> memberCoupon.use(2000L, now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용한 쿠폰입니다.");
    }
}