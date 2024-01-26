package com.ssafy.backend.group.repository;

import com.ssafy.backend.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Boolean existsByName(String name);

}
