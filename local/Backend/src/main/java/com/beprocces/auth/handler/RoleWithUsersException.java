package com.beprocces.auth.handler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleWithUsersException extends  RuntimeException {
     static String roleName ;
    public RoleWithUsersException(String roleName) {
       super();
       RoleWithUsersException.roleName =roleName ;
    }
}
