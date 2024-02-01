package com.ssafy.backend.shareBoardCommet.domain;

import com.ssafy.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "share_board_comment")
@Getter
@NoArgsConstructor
@Builder
public class ShareBoardComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "share_board_seq", nullable = false)
    private Long shareBoardSeq;

    @Column(name = "member_seq", nullable = false)
    private Long memberSeq;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private boolean isDeleted;

    @Builder
    public ShareBoardComment(Long seq, Long shareBoardSeq, Long memberSeq, String content, boolean isDeleted) {
        this.seq = seq;
        this.shareBoardSeq = shareBoardSeq;
        this.memberSeq = memberSeq;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public ShareBoardComment update(String content) {
        this.content = content;
        return this;
    }

    public ShareBoardComment delete(ShareBoardComment shareBoardComment) {
        this.seq = shareBoardComment.getSeq();
        this.shareBoardSeq = shareBoardComment.getShareBoardSeq();
        this.memberSeq = shareBoardComment.getMemberSeq();
        this.content = shareBoardComment.content;
        this.isDeleted = true;
        return this;
    }

}
