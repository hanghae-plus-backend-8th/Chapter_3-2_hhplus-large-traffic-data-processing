package kr.hhplus.be.server.infrastructure.member.persistence;

import kr.hhplus.be.server.domain.member.Member;
import kr.hhplus.be.server.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DummyMemberRepository implements MemberRepository {

    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.empty();
    }
}
