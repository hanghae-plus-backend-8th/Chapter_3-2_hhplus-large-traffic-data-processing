package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.order.OrderCommand.OrderCaptureCommand;
import kr.hhplus.be.server.application.order.OrderResult.OrderCaptureResult;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.MemberCouponRepository;
import kr.hhplus.be.server.domain.member.Member;
import kr.hhplus.be.server.domain.member.MemberRepository;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.point.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.LocalDateTime;
import java.util.List;

import static kr.hhplus.be.server.domain.coupon.CouponType.FIXED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@SpringBootTest
class OrderServiceTest {

    @MockitoSpyBean
    private ProductRepository productRepository;

    @MockitoSpyBean
    private PointRepository pointRepository;

    @MockitoSpyBean
    private CouponRepository couponRepository;

    @MockitoSpyBean
    private MemberCouponRepository memberCouponRepository;

    @MockitoSpyBean
    private OrderRepository orderRepository;

    @MockitoSpyBean
    private PaymentRepository paymentRepository;

    @MockitoSpyBean
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("사용자는 상품을 주문하고 결제할 수 있다.")
    @Test
    void order() {
        // given
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(5);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(5);
        Coupon coupon = Coupon.create("5000원 할인 쿠폰", FIXED, 5000, 50, startDateTime, endDateTime);
        Member member = Member.create("hanghae", "password1234", "김항해", "M", "01012341234");
        Product product = Product.create("상품명", 3000L, 50);
        member = memberRepository.save(member);
        product = productRepository.save(product);
        coupon = couponRepository.save(coupon);
        String couponNumber = couponService.download(coupon.getId(), member.getMemberId())
                .getCouponNumber();
        OrderCaptureCommand orderCaptureCommand = new OrderCaptureCommand(
                member.getMemberId(),
                couponNumber,
                List.of(product.getId()),
                List.of(1)
        );

        // when
        OrderCaptureResult orderCaptureResult = orderService.capture(orderCaptureCommand);

        // then
        InOrder inOrder = BDDMockito.inOrder(
                memberCouponRepository, productRepository, pointRepository,
                couponRepository, orderRepository, paymentRepository,
                pointHistoryRepository
        );

        assertThat(orderCaptureResult).extracting("totalPrice", "discountPrice", "payPrice")
                .containsExactlyInAnyOrder(3000L, 3000L, 0L);
        inOrder.verify(memberCouponRepository, times(1)).getByCouponNumber(couponNumber);
        inOrder.verify(pointRepository, times(1)).getById(member.getMemberId());
        inOrder.verify(productRepository, times(1)).findAllByIds(List.of(product.getId()));
        inOrder.verify(orderRepository, times(1)).save(any());
        inOrder.verify(paymentRepository, times(1)).save(any());
        inOrder.verify(productRepository, times(1)).updateQuantity(any());
        inOrder.verify(memberCouponRepository, times(1)).updateStatus(any());
    }
}