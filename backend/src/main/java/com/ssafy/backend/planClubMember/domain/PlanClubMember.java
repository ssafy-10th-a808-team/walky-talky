package com.ssafy.backend.planClubMember.domain;


import com.ssafy.backend.clubMember.domain.ClubMember;
import com.ssafy.backend.global.domain.BaseEntity;
import com.ssafy.backend.plan.domain.Plan;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plan_club_member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanClubMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "plan_seq")
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "club_member_seq")
    private ClubMember clubMember;
}
