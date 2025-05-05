package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.order.OrderCommand.OrderCaptureCommand;
import kr.hhplus.be.server.application.order.OrderResult.OrderCaptureResult;
import kr.hhplus.be.server.domain.coupon.MemberCoupon;
import kr.hhplus.be.server.domain.coupon.MemberCouponRepository;
import kr.hhplus.be.server.domain.order.Cart;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderManager;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.point.*;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
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

    // 주문 관련
    private final OrderManager orderManager;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // 결제 관련 (추후, 분리 시)
    private final PaymentRepository paymentRepository;
    private final PointRepository pointRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public OrderCaptureResult capture(OrderCaptureCommand orderCaptureCommand) {
        // 요청 파라미터
        long memberId = orderCaptureCommand.getMemberId();
        String couponNumber = orderCaptureCommand.getCouponNumber();
        List<Long> productIds = orderCaptureCommand.getProductIds();
        List<Integer> quantities = orderCaptureCommand.getQuantities();

        // 카트 생성
        Cart cart = Cart.create();
        cart.addCartProducts(productIds, quantities);

        // 주문 처리
        List<Product> products = productRepository.findAllByIdsLocking(productIds);
        Order order = orderManager.createOrder(products, cart, memberId);
        Order savedOrder = orderRepository.save(order);

        // 결제 처리
        MemberPoint memberPoint = pointRepository.getByIdLocking(memberId);
        MemberCoupon memberCoupon = null;
        if (couponNumber != null) {
            memberCoupon = memberCouponRepository.getByCouponNumberLocking(couponNumber);
        }
        Payment payment = Payment.processPayment(savedOrder, memberCoupon, memberPoint, LocalDateTime.now());

        // 도메인 레이어 작업 끝난 이후, 영속화 작업 진행
        Payment savedPayment = paymentRepository.save(payment);

        if (savedPayment.getPayPrice() != 0L) {
            PointHistory pointHistory = PointHistory.create(memberId, TransactionType.USE, savedPayment.getPayPrice());
            pointHistoryRepository.save(pointHistory);
            pointRepository.update(memberPoint);
        }
        if (memberCoupon != null) {
            memberCouponRepository.update(memberCoupon);
        }
        for (Product product : products) {
            productRepository.update(product);
        }
        return new OrderCaptureResult(savedPayment, savedOrder);
    }
}
