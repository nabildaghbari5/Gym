package com.beprocces.auth.service;

import com.beprocces.auth.model.CoachRequest;
import com.beprocces.auth.model.Coachs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CoachsService {
    Coachs add(CoachRequest coachs);
    List<Coachs> findAll();
    Page<Coachs> findAll(Pageable pageable);
    Coachs findById(Integer id);
    Coachs update(Integer id, CoachRequest coachs);
    void deleteById(Integer id);

    Page<Coachs> getCoachsByGroupId(Integer groupId, PageRequest pageRequest);

    void removeGroupFromUser(Integer userId, Integer groupId);
}
