package kr.hhplus.be.server.domain.order;

import lombok.Getter;

/**
 * # 주문 상태
 * - CREATED    : 주문 생성
 * - COMPLETED  : 주문 완료
 * - CANCELLED  : 주문 취소
 */
@Getter
public enum OrderStatus {
    CREATED,
    COMPLETED,
    CANCELLED,
}
