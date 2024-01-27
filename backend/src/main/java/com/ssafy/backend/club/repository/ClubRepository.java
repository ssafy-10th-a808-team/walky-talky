package com.ssafy.backend.club.repository;

import com.ssafy.backend.club.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Boolean existsByName(String name);

}
