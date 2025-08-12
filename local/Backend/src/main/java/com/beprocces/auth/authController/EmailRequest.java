package com.beprocces.auth.authController;


import com.beprocces.auth.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {

    private String email ;
    private String fullName;
    private String password;
    private String telephone;
    private String fonction ;
    private List<Role> roles ;

}
