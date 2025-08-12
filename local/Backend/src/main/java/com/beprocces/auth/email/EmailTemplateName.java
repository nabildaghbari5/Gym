package com.beprocces.auth.email;

import lombok.Getter;

@Getter

public enum EmailTemplateName {
  ACTIVATE_ACCOUNT("activate_account"),
  RESET_PASSWORD("reset_password") ,
  GYM("gym");

    private final String name ;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
