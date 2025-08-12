package com.beprocces.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private String lastname;
    private String firstname;
    private String email;
    private String password;
    private String fonction;
    private String telephone;
    private String typeUser;
    private String status;
    private List<String> roles;
    private List<String> groups;

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fonction='" + fonction + '\'' +
                ", telephone='" + telephone + '\'' +
                ", TypeUser='" + typeUser + '\'' +
                ", status='" + status + '\'' +
                ", roles=" + roles +
                ", groups=" + groups +
                '}';
    }
}
