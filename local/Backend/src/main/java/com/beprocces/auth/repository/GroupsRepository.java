package com.beprocces.auth.repository;

import com.beprocces.auth.model.Groups;
import com.beprocces.auth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupsRepository extends JpaRepository<Groups , Integer> {
    Optional<Groups> findByName(String groups); 

    Page<Groups> findAll(Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.groups g WHERE g.id=:groupId")
    Page<User> findUserByGroupId(@Param("groupId") Integer groupId, Pageable pageable);


}
