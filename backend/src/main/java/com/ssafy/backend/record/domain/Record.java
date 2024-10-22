package com.ssafy.backend.record.domain;

import com.ssafy.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "record")
@Getter
@NoArgsConstructor
@Builder
public class Record extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @NotNull
    @Column(name = "member_seq")
    private Long memberSeq;

    @Column(name = "group_seq")
    private Long groupSeq;

    private String title;

    @Column(name = "star_rating")
    private int starRating;

    private String comment;

    @Column(name = "used_count")
    @ColumnDefault("0")
    private int usedCount;

    @Column(name = "scraped_count")
    @ColumnDefault("0")
    private int scrapedCount; // 필요없을듯?

    private Double distance;

    private String duration;

    @Column(name = "region_cd", length = 255)
    private String regionCd;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private boolean isDeleted;

    public Record update(String comment, int starRating){
        this.comment = comment;
        this.starRating = starRating;
        return this;
    }

    public Record delete(){
        this.isDeleted = true;
        return this;
    }

    @Builder
    public Record(Long seq, Long memberSeq, Long groupSeq, String title, int starRating, String comment, int usedCount, int scrapedCount, Double distance, String duration, String regionCd, boolean isDeleted) {
        this.seq = seq;
        this.memberSeq = memberSeq;
        this.groupSeq = groupSeq;
        this.title = title;
        this.starRating = starRating;
        this.comment = comment;
        this.usedCount = usedCount;
        this.scrapedCount = scrapedCount;
        this.distance = distance;
        this.duration = duration;
        this.regionCd = regionCd;
        this.isDeleted = isDeleted;
    }
}
