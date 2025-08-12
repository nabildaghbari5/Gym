package com.beprocces.auth.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum BusinessErrorCode {
    NO_CODE(0 , NOT_IMPLEMENTED , "No code "),
    INCORRECT_CURRENT_PASSWORD(300 , BAD_REQUEST , "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301 , BAD_REQUEST , "Then new password does not match"),
    ACCOUNT_LOCKED(302 , FORBIDDEN , "User account is locked"),
    ACCOUNT_DISABLED(303 , FORBIDDEN , "User account is disabled"),
    BAD_CREDENTIALS(304 , FORBIDDEN , "L'adresse e-mail et / ou le mot de passe sont incorrects."),

    ROLE_HAS_USERS(305, BAD_REQUEST, "Le rôle "+ RoleWithUsersException.roleName + " contient des utilisateurs et ne peut pas être supprimé"),
;
    @Getter
    private final int code ;
    @Getter
    private final String description ;
    @Getter
    private final HttpStatus httpStatus ;


    BusinessErrorCode(int code, HttpStatus httpStatus , String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
