package com.beprocces.auth.authController;

import com.beprocces.auth.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token ;
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private List<Role> roles ;




}
