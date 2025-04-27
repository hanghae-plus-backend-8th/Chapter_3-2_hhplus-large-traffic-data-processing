package kr.hhplus.be.server.domain.member;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Member {

    private Long memberId;
    private String id;
    private String password;
    private String name;
    private String gender;
    private String phone;

    private Member(
            @Nullable Long memberId,
            @NonNull String id,
            @NonNull String password,
            @NonNull String name,
            @NonNull String gender,
            @NonNull String phone
    ) {
        if (memberId != null && memberId <= 0) {
            throw new IllegalArgumentException("사용자 식별자가 유효하지 않습니다.");
        }
        if (!gender.matches("^[MF]$")) {
            throw new IllegalArgumentException("성별이 유효하지 않습니다.");
        }
        this.memberId = memberId;
        this.id = id;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
    }

    public static Member create(
            @NonNull String id,
            @NonNull String password,
            @NonNull String name,
            @NonNull String gender,
            @NonNull String phone
    ) {
        return new Member(null, id, password, name, gender, phone);
    }

    public static Member of(
            long memberId,
            @NonNull String id,
            @NonNull String password,
            @NonNull String name,
            @NonNull String gender,
            @NonNull String phone
    ) {
        return new Member(memberId, id, password, name, gender, phone);
    }
}
