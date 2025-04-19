package kr.hhplus.be.server.shared.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = true, insertable = false, updatable = true)
    @Comment("최근 수정일")
    private LocalDateTime lastModifiedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, insertable = true, updatable = false)
    @Comment("생성일")
    private LocalDateTime createdAt;
}
