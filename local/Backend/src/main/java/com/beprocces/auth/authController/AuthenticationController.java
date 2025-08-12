package com.beprocces.auth.authController;

import com.beprocces.auth.model.User;
import com.beprocces.auth.reset_password.PasswordResetRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

     private final AuthenticationService service;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException , RoleNotFoundException {
       User registeredUser = service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PostMapping("/send-email")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> sendEmail(@RequestBody User user) throws MessagingException {
        service.sendValidationEmail(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody @Valid AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
    }

   @PostMapping("/password-reset-request")
   @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> resetPasswordRequest(@RequestBody PasswordResetRequest passwordResetRequest) throws MessagingException {
           service.resetPasswordRequest(passwordResetRequest);
          return ResponseEntity.accepted().build();
   }








}
