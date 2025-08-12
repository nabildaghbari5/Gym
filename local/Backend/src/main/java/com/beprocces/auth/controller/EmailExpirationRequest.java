package com.beprocces.auth.controller;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmailExpirationRequest {

    private String firstname ;
    private String lastname ;
    private String telephone ;
    private LocalDate dateInscription ;
    private LocalDate dateExpiration ;


}
