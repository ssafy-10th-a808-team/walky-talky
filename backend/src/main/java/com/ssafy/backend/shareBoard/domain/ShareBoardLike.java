package com.ssafy.backend.shareBoard.domain;

import com.ssafy.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "share_board_like")
@Getter
@NoArgsConstructor
@Builder
public class ShareBoardLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "share_board_seq", nullable = false)
    private Long shareBoardSeq;

    @Column(name = "member_seq", nullable = false)
    private Long memberSeq;

    @Builder
    public ShareBoardLike(Long seq, Long shareBoardSeq, Long memberSeq){
        this.seq = seq;
        this.shareBoardSeq = shareBoardSeq;
        this.memberSeq = memberSeq;
    }
}
