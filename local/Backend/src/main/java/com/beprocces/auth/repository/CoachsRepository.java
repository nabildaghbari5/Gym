package com.beprocces.auth.repository;

import com.beprocces.auth.model.Coachs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachsRepository extends JpaRepository<Coachs , Integer> {

    @Query("SELECT c FROM Coachs c JOIN c.groups g WHERE g.id = :groupId")
    Page<Coachs> findCoachByGroupId(@Param("groupId") Integer groupId, Pageable pageable);

}
