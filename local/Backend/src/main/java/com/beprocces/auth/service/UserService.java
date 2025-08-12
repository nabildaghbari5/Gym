package com.beprocces.auth.service;

import com.beprocces.auth.authController.ChangePasswordRequest;
import com.beprocces.auth.model.User;
import com.beprocces.auth.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface UserService {
    User addUser(UserDTO user);

    List<User> findAll();


    User findById(Integer id);

    void deleteById(Integer id);

    User update(Integer id, User user);

    void changePassword(ChangePasswordRequest request, Principal connectedUser);

    void createPasswordResetTokenForUser(User user, String passwordResetToken);

    Page<User> findByTypeUser(String typeUser, Pageable pageable);

    User accepterOrRefuserAccount(Integer userId, String etat);

    void removeRoleFromUser(Integer userId, Integer roleId);

    void removeGroupFromUser(Integer userId, Integer groupId);

    Page<User> search(String columnName, String value, String typeUser, Pageable pageable);
}
