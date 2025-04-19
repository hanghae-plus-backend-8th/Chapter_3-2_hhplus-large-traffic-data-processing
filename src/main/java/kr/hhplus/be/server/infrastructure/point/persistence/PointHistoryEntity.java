package kr.hhplus.be.server.infrastructure.point.persistence;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.TransactionType;
import kr.hhplus.be.server.infrastructure.member.persistence.MemberEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "POINT_HISTORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("포인트 히스토리 테이블")
public class PointHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_history_id", nullable = false, updatable = false)
    @Comment("포인트 히스토리 PK")
    private Long pointHistoryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Comment("회원 PK")
    private MemberEntity member;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Comment("타입")
    private TransactionType type;

    @Column(name = "amount", nullable = false)
    @Comment("금액")
    private Long amount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, insertable = true, updatable = false)
    @Comment("생성일")
    private LocalDateTime createdAt;

    @Builder
    private PointHistoryEntity(Long pointHistoryId, MemberEntity member, TransactionType type, Long amount) {
        this.pointHistoryId = pointHistoryId;
        this.member = member;
        this.type = type;
        this.amount = amount;
    }

    public PointHistory toDomain() {
        return PointHistory.of(pointHistoryId, member.getMemberId(), type, amount, createdAt);
    }
}
