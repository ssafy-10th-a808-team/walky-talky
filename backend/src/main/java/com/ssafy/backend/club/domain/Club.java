package com.ssafy.backend.club.domain;

import com.ssafy.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "club")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Club extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String name;

    @Column(length = 2083)
    private String url;

    private String introduce;

    @Column(name = "region_cd")
    private String regionCd;

    @Column(name = "young_birth")
    private String youngBirth;

    @Column(name = "old_birth")
    private String oldBirth;

    @Column(name = "gender_type")
    private String genderType;

    @Column(name = "now_capacity")
    private int nowCapacity;

    @Column(name = "max_capacity")
    private int maxCapacity;

    @Column(name = "is_auto_recruit")
    private boolean isAutoRecruit;

    @Column(name = "is_open_recruit")
    private boolean isOpenRecruit;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
