package com.ssafy.backend.shareBoard.domain;

import com.ssafy.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "share_board")
@Getter
@NoArgsConstructor
@Builder
public class ShareBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "member_seq", nullable = false)
    private Long memberSeq;

    @Column(name = "record_seq", nullable = false)
    private Long recordSeq;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "hit")
    @ColumnDefault("0")
    private int hit;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private boolean isDeleted;

    @Builder
    public ShareBoard(Long seq, Long memberSeq, Long recordSeq, String title, String content, int hit, boolean isDeleted) {
        this.seq = seq;
        this.memberSeq = memberSeq;
        this.recordSeq = recordSeq;
        this.title = title;
        this.content = content;
        this.hit = hit;
        this.isDeleted = isDeleted;
    }


}
