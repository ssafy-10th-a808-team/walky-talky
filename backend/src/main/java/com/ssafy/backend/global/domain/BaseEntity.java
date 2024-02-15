package com.ssafy.backend.global.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        // 여기에서 createdBy를 설정하는 로직을 추가할 수 있습니다.
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
        // 여기에서 updatedBy를 설정하는 로직을 추가할 수 있습니다.
    }

}