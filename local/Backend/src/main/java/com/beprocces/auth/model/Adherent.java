package com.beprocces.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Adherent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String lastname;
    private String firstname;
    private String telephone;
    private LocalDate dateDeNaissance;
    private long age ;
    private Integer months;
    private String status ;
    private LocalDate dateInscription;
    private LocalDate dateExpiration;
    private Double price ;
    private Double montantAPayer;
    @Enumerated(EnumType.STRING)
    private TypeAbonnement abonnement;

    @ManyToMany(mappedBy = "adherents")
    private Set<Groups> activite = new HashSet<>();

}
