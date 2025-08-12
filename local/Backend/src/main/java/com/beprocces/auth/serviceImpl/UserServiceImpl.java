package com.beprocces.auth.serviceImpl;

import com.beprocces.auth.authController.ChangePasswordRequest;
import com.beprocces.auth.model.Groups;
import com.beprocces.auth.model.Role;
import com.beprocces.auth.model.User;
import com.beprocces.auth.model.UserDTO;
import com.beprocces.auth.repository.GroupsRepository;
import com.beprocces.auth.repository.RoleRepository;
import com.beprocces.auth.repository.UserRepository;
import com.beprocces.auth.reset_password.PasswordResetTokenService;
import com.beprocces.auth.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository ;
    private final GroupsRepository groupsRepository ;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenService passwordresetTokenService ;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public User addUser(UserDTO userDTO) {
        User user = new User();
        if (userDTO.getId() != null) {
            Optional<User> existingUser = userRepository.findById(userDTO.getId());
            if (existingUser.isPresent()) {
                user = existingUser.get();
            }
        }
        user.setLastname(userDTO.getLastname());
        user.setFirstname(userDTO.getFirstname());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setFonction(userDTO.getFonction());
        user.setTelephone(userDTO.getTelephone());
        user.setTypeUser(userDTO.getTypeUser());
        user.setStatus(userDTO.getStatus());
        List<Role> roles = new ArrayList<>();
        for (String roleName : userDTO.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow();
            roles.add(role);
        }
        user.setRoles(roles);
        List<Groups> groups = new ArrayList<>();
        for (String groupName : userDTO.getGroups()) {
            Groups group = groupsRepository.findByName(groupName).
                    orElseThrow();
            groups.add(group);
        }
        user.setGroups(groups);
        return userRepository.save(user);
    }


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found by id  " + id));
    }

    @Override
    public void deleteById(Integer id) {
        if(!userRepository.existsById(id)){
          throw new IllegalArgumentException("User not found by id  " + id);
        }
         User userToDelete = userRepository.findById(id)
                         .orElseThrow();
        List<Groups> groups = userToDelete.getGroups();
        List<Role> roles = userToDelete.getRoles();
        for(Groups group:groups){
            groups.remove(group);
            groupsRepository.save(group);
        }
        for(Role role:roles){
              roles.remove(role);
              roleRepository.save(role);
        }

        userRepository.deleteById(id);
    }

    @Override
    public User update(Integer id, User user) {
        User userExisting = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("USer not found by id  "  + id));

       user.setFirstname(user.getFirstname());
        /// implementation rest de code
        return userRepository.save(userExisting);
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if(!passwordEncoder.matches(request.getCurrentPassword() , user.getPassword())){
            throw new IllegalArgumentException("Wrong password ")   ;
        }
        if(!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new IllegalArgumentException("Password are not the same ");
        }
        // update the new  password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user) ;

    }

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
        passwordresetTokenService.createPasswordResetTokenForUser(user, passwordResetToken);
    }

    @Override
    public Page<User> findByTypeUser(String typeUser, Pageable page) {
        Pageable sortedPage= PageRequest.of(page.getPageNumber(), page.getPageSize() , Sort.by(Sort.Direction.DESC , "id"));
        return userRepository.findUsersByTypeUser(typeUser , sortedPage);
    }

    @Override
    public User accepterOrRefuserAccount(Integer idUser, String etat) {
        User user = userRepository.findById(idUser).orElse(null);
        if (user == null) {
            return null;
        }
        if (etat.equals("Actif")) {
            user.setStatus("Actif");
        }else {
            user.setStatus("Inactif");
        }
        return userRepository.save(user);
    }
    // remove role from user
    @Override
    public void removeRoleFromUser(Integer userId, Integer roleId) {
        User user= userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("User not found with id " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(()->new IllegalArgumentException("Role not found with id " +  roleId));
        user.getRoles().remove(role);
        userRepository.save(user);
     }

  // remove groups from user
    @Override
    public void removeGroupFromUser(Integer userId, Integer groupId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("User not found with id " + userId));
        Groups groups = groupsRepository.findById(groupId)
                .orElseThrow(()-> new IllegalArgumentException("Groups not found with  " + groupId));
        user.getGroups().remove(groups);
        userRepository.save(user);
    }

    @Override
    public Page<User> search(String columnName, String value, String typeUser, Pageable page) {
        Pageable sortedPage= PageRequest.of(page.getPageNumber(), page.getPageSize() , Sort.by(Sort.Direction.DESC , "id"));
        String queryString = "SELECT u FROM User u  WHERE u.typeUser = :typeUser";
        if ("firstname".equals(columnName)) { // Par exemple, si vous recherchez par 'name'
            queryString += " AND LOWER(u.firstname) LIKE LOWER(CONCAT('%', :value, '%'))";
        }else if("lastname".equals(columnName)){
            queryString += " AND LOWER(u.lastname) LIKE LOWER(CONCAT('%', :value, '%'))";
        } else if ("email".equals(columnName)) { // Autre exemple pour l'email
            queryString += " AND LOWER(u.email) LIKE LOWER(CONCAT('%', :value, '%'))";
        }else if ("groups".equals(columnName)) {
            // Jointure supplémentaire avec les rôles et filtrage par le nom du rôle
            queryString += " AND EXISTS (SELECT r FROM u.groups r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :value, '%')))";
        }else if ("lastModifiedDate".equals(columnName)) {
            queryString += " AND LOWER(u.lastModifiedDate) LIKE LOWER(CONCAT('%', :value, '%'))";
        }

        TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
        query.setParameter("typeUser", typeUser);
        query.setParameter("value", value);

        // Créez la requête paginée
        return new PageImpl<>(query.getResultList(), sortedPage, query.getResultList().size());
    }


}