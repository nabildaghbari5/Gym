package com.beprocces.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails , Principal {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String lastname ;
    private String firstname ;
    @Column(unique = true)
    private String email ;
    private String password ;
    private String fonction;
    private String telephone ;
    private String typeUser ;
    private String status ;
    private boolean accountLocked;
    private boolean enabled ;
    // fetch = FetchType.EAGER : losque on récupére l'utilisateur que nous
    // souhaitons recupérer avec  la liste des roles
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles ;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Groups> groups ;

    @CreatedDate
    @Column(nullable = false , updatable = false)
    private LocalDate createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDate lastModifiedDate ;

    @Override
    public String getName() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
       }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String fullName(){
      return firstname + " " + lastname ;
    }
}
