package com.beprocces.auth.reset_password;

import java.util.Calendar;
import java.util.Optional;

import com.beprocces.auth.model.User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    public final PasswordResetTokenRepository passwordResetTokenRepository ;


    public void createPasswordResetTokenForUser(User user , String passwordToken ) {
        // Vérifie si un token existe déjà pour cet utilisateur
        PasswordResetToken existingToken = passwordResetTokenRepository.findByUser(user);

        if (existingToken != null) {
            // Supprime le token existant
            passwordResetTokenRepository.delete(existingToken);
            System.out.println("***************** supprime le token précédent.*****************************");
        }

        // Enregistre le nouveau token
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken , user);
        passwordResetTokenRepository.save(passwordResetToken);

    }

    public String validatePasswordResetToken(String passwordResetToken) {
        PasswordResetToken passwordToken = passwordResetTokenRepository.findByToken(passwordResetToken);
        if(passwordToken == null){
            return "Invalid verification token";
        }
        User user = passwordToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((passwordToken.getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
            return "Link already expired, resend link";
        }
        return "valid";
    }
    public Optional<User> findUserByPasswordToken(String passwordResetToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(passwordResetToken).getUser());
    }

    public PasswordResetToken findPasswordResetToken(String token){
        return passwordResetTokenRepository.findByToken(token);
    }

}
