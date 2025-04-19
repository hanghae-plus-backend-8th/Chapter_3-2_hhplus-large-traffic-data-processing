package kr.hhplus.be.server.infrastructure.member.persistence;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.member.Member;
import kr.hhplus.be.server.domain.point.MemberPoint;
import kr.hhplus.be.server.shared.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Table(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("회원 테이블")
public class MemberEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id", nullable = false, updatable = false)
    @Comment("회원 PK")
    private Long memberId;

    @Column(name = "id", length = 30, nullable = false, unique = true)
    @Comment("아이디")
    private String id;

    @Column(name = "password", length = 60, nullable = false, columnDefinition = "char(60)")
    @Comment("비밀번호")
    private String password;

    @Column(name = "name", length = 10, nullable = false)
    @Comment("이름")
    private String name;

    @Column(name = "gender", length = 1, nullable = false)
    @Comment("성별")
    private String gender;

    @Column(name = "phone", length = 11, nullable = false, unique = true)
    @Comment("휴대폰번호")
    private String phone;

    @Column(name = "point", nullable = false, columnDefinition = "int unsigned")
    @Comment("포인트")
    private Long point = 0L;

    @Builder
    private MemberEntity(Long memberId, String id, String password, String name, String gender, String phone, Long point) {
        this.memberId = memberId;
        this.id = id;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.point = point;
    }

    public Member toMemberDomain() {
        return Member.of(memberId, id, password, name, gender, phone);
    }

    public MemberPoint toMemberPointDomain() {
        return MemberPoint.of(memberId, point);
    }

    public void updatePoint(Long point) {
        this.point = point;
    }
}
