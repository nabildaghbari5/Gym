package com.beprocces.auth.reset_password;

import com.beprocces.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository  extends JpaRepository<PasswordResetToken, Integer> {

    PasswordResetToken findByToken(String passwordResetToken);
    PasswordResetToken findByUser(User user);


}
