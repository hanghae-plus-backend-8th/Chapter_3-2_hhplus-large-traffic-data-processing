package kr.hhplus.be.server.domain.point;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberPoint {

    private long memberId;
    private long point;

    public static MemberPoint of(long memberId, long point) {
        if (memberId <= 0) {
            throw new IllegalArgumentException("사용자 식별자가 유효하지 않습니다.");
        }
        if (point < 0) {
            throw new IllegalArgumentException("사용자 포인트가 유효하지 않습니다.");
        }
        return new MemberPoint(memberId, point);
    }

    public void charge(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("포인트 충전 금액이 유효하지 않습니다.");
        }
        this.point += amount;
    }

    public void use(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("사용할 포인트 금액이 유효하지 않습니다.");
        }
        if (point - amount < 0) {
            throw new IllegalArgumentException("보유 포인트가 부족합니다.");
        }
        this.point -= amount;
    }
}
