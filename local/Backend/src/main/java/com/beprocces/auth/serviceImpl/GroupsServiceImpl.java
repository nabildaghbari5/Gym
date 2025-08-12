package com.beprocces.auth.serviceImpl;

import com.beprocces.auth.model.Groups;
import com.beprocces.auth.model.User;
import com.beprocces.auth.repository.GroupsRepository;
import com.beprocces.auth.repository.UserRepository;
import com.beprocces.auth.service.GroupsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupsServiceImpl implements GroupsService {
      private final GroupsRepository groupsRepository ;
      private final UserRepository userRepository ;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Groups addGroups(Groups Groups) {
        return groupsRepository.save(Groups);
    }

    @Override
    public Groups update(Integer id, Groups Groups) {
        Groups groupsExisting = groupsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Groups not found by id "  + id ));
       groupsExisting.setName(Groups.getName());
       groupsExisting.setDescription(Groups.getDescription());
        return null;
    }


    @Override
    public List<Groups> findAll() {
        return groupsRepository.findAll();
    }

    @Override
    public Page<Groups> findAll(Pageable page) {
        Pageable sortedPage = PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return groupsRepository.findAll(sortedPage);
    }

    @Override
    public Groups findById(Integer id) {
        return groupsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Groups not found by id " + id));
    }
    @Override
    public void deleteById(Integer id) {
            if(!groupsRepository.existsById(id)){
                throw new IllegalArgumentException("Groups not found By Id "  + id) ;
            }
            // Récupérer le groupe a supprimer
           Groups groupToDelete=groupsRepository.findById(id)
                        .orElseThrow(()-> new IllegalArgumentException("Groups not found"));
           List<User>  users =userRepository.findAll();
           for(User user:users){
              if(user.getGroups().contains(groupToDelete)) {
                user.getGroups().remove(groupToDelete);
                userRepository.save(user);
              }
           }

            groupsRepository.deleteById(id);
    }

    @Override
    public Page<User> getUsersByGroupId(Integer groupId, Pageable page) {
        Pageable sortedPage = PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return groupsRepository.findUserByGroupId(groupId , sortedPage);
    }

    @Override
    public Page<User> search(String columnName, String value, Integer groupId, Pageable page) {
        Pageable sortedPage=PageRequest.of(page.getPageNumber(), page.getPageSize() , Sort.by(Sort.Direction.DESC , "id"));
        String queryString = "SELECT u FROM User u JOIN u.groups g WHERE g.id = :groupId";

        if ("firstname".equals(columnName)) { // Par exemple, si vous recherchez par 'name'
            queryString += " AND LOWER(u.firstname) LIKE LOWER(CONCAT('%', :value, '%'))";
        }else if("lastname".equals(columnName)){
            queryString += " AND LOWER(u.lastname) LIKE LOWER(CONCAT('%', :value, '%'))";
        } else if ("email".equals(columnName)) { // Autre exemple pour l'email
            queryString += " AND LOWER(u.email) LIKE LOWER(CONCAT('%', :value, '%'))";
        }else if ("roles".equals(columnName)) {
            // Jointure supplémentaire avec les rôles et filtrage par le nom du rôle
            queryString += " AND EXISTS (SELECT r FROM u.roles r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :value, '%')))";
        }
        // Ajoutez d'autres conditions pour d'autres colonnes si nécessaire.
        TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
        query.setParameter("groupId", groupId);
        query.setParameter("value", value);
        // Créez la requête paginée
        return new PageImpl<>(query.getResultList(), sortedPage, query.getResultList().size());
    }

    /*
    @Override
    public Page<Groups> search(String columnName, String value, Integer groupId, Pageable pageable) {
        return groupsRepository.searchUser(columnName , value  , groupId , pageable);
    }*/

}
