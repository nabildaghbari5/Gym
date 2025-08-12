package com.beprocces.auth.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY) //  pour contrôler la sérialisation JSON des objets. Elle spécifie quand une propriété d'un objet doit être incluse dans la sortie JSON.
public class ExceptionResponse {

    private Integer businessErrorCode ;
    private String businessErrorDescription ;
    private String error ;
    private Set<String> validationErrors ;
    private Map<String , String> errors ;

}
