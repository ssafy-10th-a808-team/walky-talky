package com.ssafy.backend.record.domain;

import com.ssafy.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "record_detail")
@Getter
@NoArgsConstructor
@Builder
public class RecordDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "record_seq", nullable = false)
    private Long recordSeq;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "point_lat", nullable = false)),
            @AttributeOverride(name = "lng", column = @Column(name = "point_lng", nullable = false))
    })
    private Point point;

    @Column(name = "time")
    private String time;

    @Column(name = "url", length = 2083)
    private String url;

    @Column(name = "point_comment", length = 255)
    private String pointComment;

    public RecordDetail(Long seq, Long recordSeq, Point point, String time, String url, String pointComment) {
        this.seq = seq;
        this.recordSeq = recordSeq;
        this.point = point;
        this.time = time;
        this.url = url;
        this.pointComment = pointComment;
    }
}

