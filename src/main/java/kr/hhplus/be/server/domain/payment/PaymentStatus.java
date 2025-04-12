package kr.hhplus.be.server.domain.payment;

import lombok.Getter;

/**
 * # 결제 상태
 * - PENDING    : 결제대기
 * - COMPLETED  : 결제완료
 * - CANCELLED  : 결제취소
 */
@Getter
public enum PaymentStatus {
    PENDING,
    COMPLETED,
    CANCELLED,
}
