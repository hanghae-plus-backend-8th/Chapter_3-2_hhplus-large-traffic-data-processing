package kr.hhplus.be.server.domain.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

    private long memberId;
    private String name;
    private String gender;
    private String phone;
}
