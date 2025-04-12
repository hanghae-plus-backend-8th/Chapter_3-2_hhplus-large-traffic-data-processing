package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.application.coupon.CouponResult.CouponDownloadResult;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.MemberCoupon;
import kr.hhplus.be.server.domain.coupon.MemberCouponRepository;
import kr.hhplus.be.server.domain.member.Member;
import kr.hhplus.be.server.domain.member.MemberRepository;
import kr.hhplus.be.server.shared.exception.NotFoundResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public CouponDownloadResult download(long couponId, long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundResourceException("유효하지 않은 사용자 식별자입니다."));
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundResourceException("유효하지 않은 쿠폰 식별자입니다."));

        MemberCoupon memberCoupon = coupon.giveCoupon(member.getMemberId(), LocalDateTime.now());
        memberCouponRepository.save(memberCoupon);
        couponRepository.update(coupon.getId(), coupon.getRemainingQuantity());

        return new CouponDownloadResult(memberCoupon);
    }
}
