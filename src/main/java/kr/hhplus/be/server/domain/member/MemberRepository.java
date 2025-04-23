package kr.hhplus.be.server.domain.member;

public interface MemberRepository {

    Member save(Member member);
    Member getById(Long memberId);
}
