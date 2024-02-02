package com.ssafy.backend.record.domain;

import com.ssafy.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dislike")
@Getter
@NoArgsConstructor
@Builder
public class Dislike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "record_seq", nullable = false)
    private Long recordSeq;

    @Column(name = "member_seq", nullable = false)
    private Long memberSeq;

    public Dislike(Long seq, Long recordSeq, Long memberSeq) {
        this.seq = seq;
        this.recordSeq = recordSeq;
        this.memberSeq = memberSeq;
    }
}
