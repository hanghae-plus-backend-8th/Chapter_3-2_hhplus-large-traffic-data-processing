package kr.hhplus.be.server.infrastructure.member.persistence;

import kr.hhplus.be.server.domain.member.Member;
import kr.hhplus.be.server.domain.member.MemberRepository;
import kr.hhplus.be.server.shared.exception.NotFoundResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member save(Member member) {
        MemberEntity memberEntity = MemberEntity.builder()
                .id(member.getId())
                .password(member.getPassword())
                .name(member.getName())
                .gender(member.getGender())
                .phone(member.getPhone())
                .point(0L)
                .build();

        return memberJpaRepository.save(memberEntity)
                .toMemberDomain();
    }

    @Override
    public Member getById(Long memberId) {
        return memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundResourceException("유효하지 않은 사용자 식별자입니다."))
                .toMemberDomain();
    }
}
