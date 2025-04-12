package kr.hhplus.be.server.domain.point;

import lombok.Getter;

/**
 * # 트랜잭션 타입
 * - CHARGE : 충전
 * - USE    : 사용
 */
@Getter
public enum TransactionType {
    CHARGE,
    USE,
}
