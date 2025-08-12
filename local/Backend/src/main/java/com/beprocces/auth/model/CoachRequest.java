package com.beprocces.auth.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoachRequest {
    private String lastname ;
    private String firstname ;
    private String email ;
    private String telephone ;
    private String status ;
    private Set<Integer> groupIds;
}
