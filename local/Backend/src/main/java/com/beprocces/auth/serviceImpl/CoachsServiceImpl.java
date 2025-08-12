package com.beprocces.auth.serviceImpl;

import com.beprocces.auth.model.CoachRequest;
import com.beprocces.auth.model.Coachs;
import com.beprocces.auth.model.Groups;
import com.beprocces.auth.model.User;
import com.beprocces.auth.repository.CoachsRepository;
import com.beprocces.auth.repository.GroupsRepository;
import com.beprocces.auth.service.CoachsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoachsServiceImpl implements CoachsService {

    private final CoachsRepository coachsRepository ;
    private final GroupsRepository groupsRepository ;

    @Override
    public Coachs add(CoachRequest coachRequest) {
        Coachs newCoachs = new Coachs();
        newCoachs.setFirstname(coachRequest.getFirstname());
        newCoachs.setLastname(coachRequest.getLastname());
        newCoachs.setStatus(coachRequest.getStatus());
        newCoachs.setTelephone(coachRequest.getTelephone());
        newCoachs.setEmail(coachRequest.getEmail());
        List<Groups> groups = groupsRepository.findAllById(coachRequest.getGroupIds());
        newCoachs.setGroups(groups.isEmpty() ? Collections.emptyList():groups);
        return coachsRepository.save(newCoachs);
    }

    @Override
    public List<Coachs> findAll() {
        return null;
    }

    @Override
    public Page<Coachs> findAll(Pageable pageable) {
        Pageable sortedPage = PageRequest.of(pageable.getPageNumber() , pageable.getPageSize() , Sort.by(Sort.Direction.DESC , "id"));
        return coachsRepository.findAll(sortedPage);
    }

    @Override
    public Coachs findById(Integer id) {
        return null;
    }

    @Override
    public Coachs update(Integer id, CoachRequest coachs) {
        Coachs coachsExist =this.coachsRepository.findById(id).
                orElseThrow();
        coachsExist.setFirstname(coachs.getFirstname());
        coachsExist.setLastname(coachs.getLastname());
        coachsExist.setStatus(coachs.getStatus());
        coachsExist.setEmail(coachs.getEmail());
        coachsExist.setTelephone(coachs.getTelephone());
        // Mettre à jour les groupes associés
        List<Groups> groups = groupsRepository.findAllById(coachs.getGroupIds());
        coachsExist.setGroups(groups.isEmpty() ? Collections.emptyList() : groups);
        // Sauvegarder le coach mis à jour dans la base de données
        return coachsRepository.save(coachsExist);
    }

    @Override
    public void deleteById(Integer id) {
       Coachs coachs = coachsRepository.findById(id)
                       .orElseThrow(()-> new IllegalArgumentException("Coach not found by id : " + id));
       coachs.getGroups().clear();
       coachsRepository.save(coachs);
       coachsRepository.deleteById(id);
    }

    @Override
    public Page<Coachs> getCoachsByGroupId(Integer groupId, PageRequest pageRequest) {
        Pageable sortedPage = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return coachsRepository.findCoachByGroupId(groupId , sortedPage);
    }


    // remove groups from user
    @Override
    public void removeGroupFromUser(Integer userId, Integer groupId) {
        Coachs coachs = coachsRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("User not found with id " + userId));
        Groups groups = groupsRepository.findById(groupId)
                .orElseThrow(()-> new IllegalArgumentException("Groups not found with  " + groupId));
        coachs.getGroups().remove(groups);
        coachsRepository.save(coachs);
    }

}
