package kr.hhplus.be.server.domain.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberPointTest {

    @DisplayName("충전할 포인트는 0원 이하일 수 없다.")
    @Test
    void chargeInvalidPoint() {
        // given
        MemberPoint memberPoint = MemberPoint.of(1L, 2000L);

        // when, then
        assertThatThrownBy(() -> memberPoint.charge(0L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("포인트를 충전할 수 있다.")
    @Test
    void chargePoint() {
        // given
        MemberPoint memberPoint = MemberPoint.of(1L, 2000L);

        // when
        memberPoint.charge(1000L);

        // then
        assertThat(memberPoint.getPoint()).isEqualTo(3000L);
    }

    @DisplayName("사용할 포인트는 0원 이하일 수 없다.")
    @Test
    void useInvalidPoint() {
        // given
        MemberPoint memberPoint = MemberPoint.of(1L, 2000L);

        // when, then
        assertThatThrownBy(() -> memberPoint.use(0L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("포인트를 사용할 수 있다.")
    @Test
    void usePoint() {
        // given
        MemberPoint memberPoint = MemberPoint.of(1L, 2000L);

        // when
        memberPoint.use(1000L);

        // then
        assertThat(memberPoint.getPoint()).isEqualTo(1000L);
    }

    @DisplayName("잔여 포인트를 초과하여 사용할 수 없다.")
    @Test
    void useExceedsPoint() {
        // given
        MemberPoint memberPoint = MemberPoint.of(1L, 2000L);

        // when, then
        assertThatThrownBy(() -> memberPoint.use(3000L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}