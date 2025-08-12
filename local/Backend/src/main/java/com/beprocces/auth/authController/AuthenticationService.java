package com.beprocces.auth.authController;


import com.beprocces.auth.email.EmailService;
import com.beprocces.auth.email.EmailTemplateName;
import com.beprocces.auth.model.Groups;
import com.beprocces.auth.model.Role;
import com.beprocces.auth.model.Token;
import com.beprocces.auth.model.User;
import com.beprocces.auth.repository.GroupsRepository;
import com.beprocces.auth.repository.RoleRepository;
import com.beprocces.auth.repository.TokenRepository;
import com.beprocces.auth.repository.UserRepository;
import com.beprocces.auth.reset_password.PasswordResetRequest;
import com.beprocces.auth.security.JwtService;
import com.beprocces.auth.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserService userService ;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    private final GroupsRepository groupsRepository ;
    @Value("${application.mailing.frontend.activation-url}")
    private  String activationUrl;
    @Value("${application.mailing.frontend.resetPassword-url}")
    private  String resetPasswordUrl;


    public User register(RegistrationRequest request) throws MessagingException, RoleNotFoundException {
        List<Role> roles = new ArrayList<>();
        List<Groups> groups = new ArrayList<>() ;
        if(request.getTypeUser().equals("UserExterne")){
            roles = List.of(roleRepository.findByName("USER")
                    .orElseThrow(() -> new RoleNotFoundException("ROLE USER was not initiated")));
        }else {

          for(String rolename:request.getRoles()){
              Role role = this.roleRepository.findByName(rolename)
                      .orElseThrow(() -> new RoleNotFoundException("ROLE" + rolename + "not found"));

              roles.add(role);
          }
          for(String groupsName:request.getGroups()){
            Groups group = this.groupsRepository.findByName(groupsName)
                     .orElseThrow(() -> new IllegalArgumentException("Groups" + groupsName + "not found"));
            groups.add(group);
          }
        }
        // Creation de l'utilisateur
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(request.getPassword())
                .telephone(request.getTelephone())
                .fonction(request.getFonction())
                .accountLocked(false)
                .typeUser(request.getTypeUser())
                .enabled(false)
                .roles(roles)
                .groups(groups)
                .status(request.getStatus())
                .build();
        userRepository.save(user);
       sendValidationEmail(user);
        return user;
    }
    public void sendValidationEmail(User user) throws MessagingException {
      var newToken = generateAndSaveActivationToken(user);
      // send email
        emailService.sendEmail(
          user.getEmail() ,
          user.fullName(),
          user.getPassword(),
          user.getTelephone(),
          user.getFonction(),
          user.getRoles(),
          EmailTemplateName.ACTIVATE_ACCOUNT ,
          activationUrl ,
          newToken ,
          "Account activation"
        );
    }
    private String generateAndSaveActivationToken(User user) {
     // generate a token
     String    generatedToken = generateActivationCode(6);
     var token = Token.builder()
             .token(generatedToken)
             .createdAt(LocalDateTime.now())
             .expiresAt(LocalDateTime.now().plusMinutes(15))
             .user(user)
             .build();
       tokenRepository.save(token);
        return generatedToken ;
    }
    private String generateActivationCode(int length) {
    String  characters = "0123456789";
    StringBuilder codeBuilder = new StringBuilder();
    SecureRandom secureRandom = new SecureRandom();
     for(int i=0 ; i< length ; i++){
         int randomIndex = secureRandom.nextInt(characters.length()); // 0...9
         codeBuilder.append(characters.charAt(randomIndex));
     }
    return codeBuilder.toString() ;
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
          )
        ) ;
        var claims = new HashMap<String , Object>();
         var user = ((User) auth.getPrincipal());
         claims.put("fullName" , user.fullName());
         var jwtToken = jwtService.generateToken(claims , user ) ;
     return AuthenticationResponse.builder()
             .token(jwtToken)
             .id(user.getId())
             .email(user.getEmail())
             .firstname(user.getFirstname())
             .lastname(user.getLastname())
             .roles(user.getRoles())
             .build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }


    public void resetPasswordRequest(PasswordResetRequest passwordResetRequest) throws MessagingException {
       User user = userRepository.findByEmail(passwordResetRequest.getEmail())
               .orElseThrow(() -> new IllegalArgumentException("User not found by email" + passwordResetRequest.getEmail()));
        sendResetPasswordEmail(user);
    }

    private void sendResetPasswordEmail(User user) throws MessagingException {
        String passwordResetUrl = "" ;
        // generer le token de reset password
        String passwordResetToken = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user , passwordResetToken);
        // construire l'uel qui contient le path de angular + le parametre token
        passwordResetUrl = this.resetPasswordUrl + "?token=" + passwordResetToken;;

        emailService.sendEmailResetPassword(
                user.getEmail() ,
                user.fullName(),
                EmailTemplateName.RESET_PASSWORD ,
                passwordResetUrl ,
                "Vérification de la demande de réinitialisation de mot de passe"
        );

    }
}
