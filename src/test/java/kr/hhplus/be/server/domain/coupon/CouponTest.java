package kr.hhplus.be.server.domain.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static kr.hhplus.be.server.domain.coupon.CouponType.FIXED;
import static kr.hhplus.be.server.domain.coupon.CouponType.PERCENT;
import static kr.hhplus.be.server.domain.coupon.MemberCouponStatus.UN_USED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponTest {

    @DisplayName("쿠폰의 유효기간 종료일은 유효기간 시작일 이후여야 한다. ")
    @Test
    void endDateTimeBeforeStartDateTime() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 1, 0, 0);

        // when, then
        assertThatThrownBy(() -> Coupon.create("쿠폰명", PERCENT, 10, 10, startDateTime, endDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효기간 종료일이 시작일보다 빠를 수 없습니다.");
    }

    @DisplayName("2000원에 1000원 할인 쿠폰을 적용하면 1000원을 할인받을 수 있다.")
    @Test
    void calculateFixedCouponV1() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        long price = 2000L;
        Coupon coupon = Coupon.create("1000원 할인 쿠폰", FIXED, 1000, 10, startDateTime, endDateTime);

        // when
        long discountPrice = coupon.calculateDiscount(price);

        // then
        assertThat(coupon.getDiscountValue()).isEqualTo(discountPrice);
    }

    @DisplayName("500원에 1000원 할인 쿠폰을 적용하면 500원을 할인받을 수 있다.")
    @Test
    void calculateFixedCouponV2() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        long price = 500L;
        Coupon coupon = Coupon.create("1000원 할인 쿠폰", FIXED, 1000, 10, startDateTime, endDateTime);

        // when
        long discountPrice = coupon.calculateDiscount(price);

        // then
        assertThat(price).isEqualTo(discountPrice);
    }

    @DisplayName("2000원에 10% 할인 쿠폰을 적용하면 200원을 할인받을 수 있다.")
    @Test
    void calculatePercentCoupon() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        long price = 2000L;
        Coupon coupon = Coupon.create("10% 할인 쿠폰", PERCENT, 10, 10, startDateTime, endDateTime);

        // when
        long discountPrice = coupon.calculateDiscount(price);

        // then
        assertThat(discountPrice).isEqualTo(200L);
    }

    @DisplayName("퍼센트 쿠폰은 정책상 최대 80%까지 허용된다.")
    @Test
    void couponAllows80Percent() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);

        // when
        assertThatThrownBy(() -> Coupon.create("쿠폰명", PERCENT, 81, 10, startDateTime, endDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("퍼센트 할인은 최대 80%까지 가능합니다.");
    }

    @DisplayName("사용자는 쿠폰을 발급할 수 있다.")
    @Test
    void giveCoupon() {
        // given
        long memberId = 1L;
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 3, 1, 0, 0, 0);
        LocalDateTime now = LocalDateTime.of(2024, 2, 1, 0, 0, 0);
        Coupon coupon = Coupon.create("30% 할인 쿠폰", PERCENT, 30, 30, startDateTime, endDateTime);

        // when
        MemberCoupon memberCoupon = coupon.giveCoupon(memberId, now);

        // then
        assertThat(memberCoupon.getMemberId()).isEqualTo(memberId);
        assertThat(memberCoupon.getStatus()).isEqualTo(UN_USED);
    }

    @DisplayName("유효기간이 만료된 쿠폰은 발급할 수 없다.")
    @Test
    void giveExpiredCoupon() {
        // given
        long memberId = 1L;
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 3, 1, 0, 0, 0);
        LocalDateTime now = LocalDateTime.of(2024, 4, 1, 0, 0, 0);
        Coupon coupon = Coupon.create("30% 할인 쿠폰", PERCENT, 30, 30, startDateTime, endDateTime);

        // when, then
        assertThatThrownBy(() -> coupon.giveCoupon(memberId, now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효 기간이 만료된 쿠폰입니다.");
    }
}