package kr.hhplus.be.server.domain.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

    private Long memberId;
    private String id;
    private String password;
    private String name;
    private String gender;
    private String phone;

    public static Member create(String id, String password, String name, String gender, String phone) {
        return new Member(null, id, password, name, gender, phone);
    }

    public static Member of(long memberId, String id, String password, String name, String gender, String phone) {
        if (memberId <= 0) {
            throw new IllegalArgumentException("사용자 식별자가 유효하지 않습니다.");
        }
        if (gender == null || !gender.matches("^[MF]$")) {
            throw new IllegalArgumentException("성별이 유효하지 않습니다.");
        }
        return new Member(memberId, id, password, name, gender, phone);
    }
}
