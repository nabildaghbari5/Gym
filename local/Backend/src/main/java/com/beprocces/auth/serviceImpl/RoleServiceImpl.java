package com.beprocces.auth.serviceImpl;

import com.beprocces.auth.handler.RoleWithUsersException;
import com.beprocces.auth.model.Role;
import com.beprocces.auth.model.User;
import com.beprocces.auth.repository.RoleRepository;
import com.beprocces.auth.repository.UserRepository;
import com.beprocces.auth.service.RoleService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository ;
    private final UserRepository userRepository ;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
      User user = userRepository.findByEmail(username)
              .orElseThrow(() -> new IllegalArgumentException("User not found bu username  " +  username) );
      Role role = roleRepository.findByName(roleName)
              .orElseThrow(() -> new IllegalArgumentException("Role not found By name" +  roleName));
      user.getRoles().add(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    @Override
    public Page<Role> findAll(Pageable page) {
        Pageable sortedPage = PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return roleRepository.findAll(sortedPage);

    }

    @Override
    public Role findById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found bu id : " + id ));
    }

    @Override
    public Role update(Integer id, Role role) {
        Role  roleExisting = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found by id"));
        roleExisting.setName(role.getName());
        return   roleRepository.save(roleExisting);
    }

    @Override
    public void deleteById(Integer id) {
        if(!roleRepository.existsById(id)){
          throw new IllegalArgumentException("Role not found By Id "  + id) ;
        }

         roleRepository.deleteById(id);
    }

    @Override
    public Page<User> getUsersByRoleId(Integer roleId, Pageable page) {
        return roleRepository.findUserByRoleId(roleId , page);
    }

    @Override
    public Page<User> search(String columnName, String value, Integer roleId, Pageable page) {
        String queryString = "SELECT u FROM User u JOIN u.roles r WHERE r.id = :roleId";

        if ("firstname".equals(columnName)) { // Par exemple, si vous recherchez par 'name'
            queryString += " AND LOWER(u.firstname) LIKE LOWER(CONCAT('%', :value, '%'))";
        }else if("lastname".equals(columnName)){
            queryString += " AND LOWER(u.lastname) LIKE LOWER(CONCAT('%', :value, '%'))";
        } else if ("email".equals(columnName)) { // Autre exemple pour l'email
            queryString += " AND LOWER(u.email) LIKE LOWER(CONCAT('%', :value, '%'))";
        }else if ("groups".equals(columnName)) {
            // Jointure supplémentaire avec les rôles et filtrage par le nom du rôle
            queryString += " AND EXISTS (SELECT r FROM u.groups r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :value, '%')))";
        }
        // Ajoutez d'autres conditions pour d'autres colonnes si nécessaire.

        TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
        query.setParameter("roleId", roleId);
        query.setParameter("value", value);

        // Créez la requête paginée
        return new PageImpl<>(query.getResultList(), page, query.getResultList().size());
    }

    @Override
    public void deleteAllRoles(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("La liste des rôles est vide ou null");
        }

        // Parcourir les rôles et vérifier si l'un d'eux contient des utilisateurs
        for (Role role : roles) {
            List<User> users = roleRepository.findUserByRoleId(role.getId(), null)
                    .stream().toList();
            if (!users.isEmpty()) {
                // Si un rôle contient des utilisateurs, lancer une exception
                throw new RoleWithUsersException(role.getName());
            }
        }

        // Si aucun rôle ne contient d'utilisateurs, procéder à la suppression
        roleRepository.deleteAll(roles);
    }

}

