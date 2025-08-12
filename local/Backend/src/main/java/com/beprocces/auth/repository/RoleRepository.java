package com.beprocces.auth.repository;

import com.beprocces.auth.model.Role;
import com.beprocces.auth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role , Integer> {

    Optional<Role> findByName(String role);

    Page<Role> findAll(Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE  r.id= :roleId ")
    Page<User> findUserByRoleId(@Param("roleId") Integer roleId , Pageable pageable);
}
