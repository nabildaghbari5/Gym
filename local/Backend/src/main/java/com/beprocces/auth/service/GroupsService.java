package com.beprocces.auth.service;

import com.beprocces.auth.model.Groups;
import com.beprocces.auth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupsService {
    Groups addGroups(Groups Groups);
    List<Groups> findAll();
    Page<Groups> findAll(Pageable pageable);
    Groups findById(Integer id);
    Groups update(Integer id, Groups Groups);
    void deleteById(Integer id);

    Page<User> getUsersByGroupId(Integer groupId, Pageable pageable);

    Page<User> search(String columnName, String value, Integer groupId, Pageable pageable);
}




