package com.beprocces.auth.service;

import com.beprocces.auth.model.Adherent;
import com.beprocces.auth.model.Groups;
import com.beprocces.auth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdherentService {
    Adherent create(Adherent adherent);
    List<Adherent> findAll();
    Page<Adherent> findAll(Pageable pageable);
    Adherent findById(Integer id);
    Adherent update(Integer id,Adherent adherent);
    void deleteById(Integer id);
    Page<Adherent> findByStatus(String status, Pageable pageable);

    Adherent updateAbonnement(Integer idAdherent, Adherent adherent);

    Page<Adherent> search(String columnName, String value, PageRequest pageable);
}
