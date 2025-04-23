package kr.hhplus.be.server.application.point;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.point.PointResult.PointChargeResult;
import kr.hhplus.be.server.application.point.PointResult.PointInfoResult;
import kr.hhplus.be.server.domain.member.Member;
import kr.hhplus.be.server.domain.member.MemberRepository;
import kr.hhplus.be.server.domain.point.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.PointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class PointServiceTest {

    @MockitoSpyBean
    private PointService pointService;

    @MockitoSpyBean
    private PointRepository pointRepository;

    @MockitoSpyBean
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("사용자는 포인트 잔액을 조회할 수 있다.")
    @Test
    void getPoint() {
        // given
        Member member = Member.create("hanghae", "password1234", "김항해", "M", "01012341234");
        member = memberRepository.save(member);

        // when
        PointInfoResult pointInfoResult = pointService.info(member.getMemberId());

        // then
        assertThat(pointInfoResult.getPoint()).isEqualTo(0L);
        verify(pointRepository, times(1)).getById(member.getMemberId());
    }

    @DisplayName("사용자는 포인트를 충전할 수 있다.")
    @Test
    void chargePoint() {
        // given
        long point = 3000L;
        Member member = Member.create("hanghae", "password1234", "김항해", "M", "01012341234");
        member = memberRepository.save(member);

        // when
        PointChargeResult pointChargeResult = pointService.charge(member.getMemberId(), point);

        // then
        InOrder inOrder = inOrder(pointRepository, pointHistoryRepository);

        assertThat(pointChargeResult.getPoint()).isEqualTo(point);
        inOrder.verify(pointRepository, times(1)).getById(member.getMemberId());
        inOrder.verify(pointRepository, times(1)).updatePoint(member.getMemberId(), point);
        inOrder.verify(pointHistoryRepository, times(1)).save(any());
    }
}