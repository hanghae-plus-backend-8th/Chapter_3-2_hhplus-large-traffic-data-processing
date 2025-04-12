package kr.hhplus.be.server.application.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.application.order.OrderCommand.OrderCaptureCommand;
import kr.hhplus.be.server.application.order.OrderResult.OrderCaptureResult;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.MemberCoupon;
import kr.hhplus.be.server.domain.coupon.MemberCouponRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.point.MemberPoint;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.interfaces.api.order.OrderRequest;
import kr.hhplus.be.server.interfaces.api.order.OrderRequest.OrderCaptureRequest;
import kr.hhplus.be.server.shared.exception.NotFoundResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final PointRepository pointRepository;
    private final ProductRepository productRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public OrderCaptureResult capture(OrderCaptureCommand orderCaptureCommand) {
        // 요청 파라미터
        long memberId = orderCaptureCommand.getMemberId();
        String couponNumber = orderCaptureCommand.getCouponNumber();
        List<Long> productIds = orderCaptureCommand.getProductIds();
        List<Integer> quantities = orderCaptureCommand.getQuantities();

        // 사용자 쿠폰, 포인트, 상품 조회
        MemberCoupon memberCoupon = null;
        if (couponNumber != null) {
            memberCoupon = memberCouponRepository.findByCouponNumber(couponNumber)
                    .orElseThrow(() -> new NotFoundResourceException("유효하지 않은 쿠폰번호입니다."));
        }
        MemberPoint memberPoint = pointRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundResourceException("유효하지 않은 사용자 식별자입니다."));
        List<Product> products = productRepository.findAllByIds(productIds);

        // 쿠폰, 상품 검증
        if (memberCoupon != null && memberCoupon.isNotMine(memberId)) {
            throw new IllegalArgumentException("해당 쿠폰에 대한 접근 권한이 없습니다.");
        }
        if (products.isEmpty() || products.size() != productIds.size()) {
            throw new IllegalArgumentException("일부 상품을 찾을 수 없습니다. 다시 확인해 주세요.");
        }

        // 주문 및 결제 처리
        Order order = Order.create(memberId);
        for (int i=0; i<products.size(); i++) {
            order.addOrderProduct(products.get(i), quantities.get(i));
        }
        Payment payment = Payment.processPayment(order, memberCoupon, memberPoint, LocalDateTime.now());

        // 도메인 레이어 작업 끝난 이후, 영속화 작업 진행
        // --> (TO DO) 도메인 서비스로 잘게 분리해볼 것!
        productRepository.updateQuantity(order.getOrderProducts());
        Order savedOrder = orderRepository.save(order);
        Payment savedPayment = paymentRepository.save(payment);
        memberCouponRepository.update(memberCoupon);
        pointRepository.update(memberPoint.getMemberId(), memberPoint.getPoint());

        return new OrderCaptureResult(savedPayment, savedOrder);
    }
}
