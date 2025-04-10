package kr.hhplus.be.server.infrastructure.payment.persistence;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DummyPaymentRepository implements PaymentRepository {

    @Override
    public Payment save(Payment payment) {
        return null;
    }
}
