package com.beprocces.auth.service;

import com.beprocces.auth.model.Role;
import com.beprocces.auth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
     Role addRole(Role role);
     void addRoleToUser(String username , String roleName) ;
     List<Role> findAll();
     Page<Role> findAll(Pageable pageable);
     Role findById(Integer id);
    Role update(Integer id, Role role);

    void deleteById(Integer id);

    Page<User> getUsersByRoleId(Integer roleId, Pageable page);

    Page<User> search(String columnName, String value, Integer roleId, Pageable pageable);

    void deleteAllRoles(List<Role> roles);
}
