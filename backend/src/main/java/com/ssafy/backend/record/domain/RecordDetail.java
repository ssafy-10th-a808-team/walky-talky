package com.ssafy.backend.record.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "record")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "record_seq", nullable = false)
    private Long recordSeq;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "point_x", nullable = false)),
            @AttributeOverride(name = "y", column = @Column(name = "point_y", nullable = false))
    })
    private Point point;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    @Column(name = "url", length = 2083)
    private String url;

    @Column(name = "point_comment", length = 255)
    private String pointComment;
}

@Setter
@Getter
@Embeddable
class Point {
    private Double x;
    private Double y;
}
