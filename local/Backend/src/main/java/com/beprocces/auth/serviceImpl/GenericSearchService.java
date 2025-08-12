package com.beprocces.auth.serviceImpl;

import com.beprocces.auth.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenericSearchService {
    @Autowired
    private EntityManager entityManager;

    public <T> List<T> search( String columnName, String value, Class<T> clazz ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(clazz);
        Root<T> root = query.from(clazz);
        // Créer une condition de filtre basée sur l'attribut dynamique
        Predicate predicate = cb.like(root.get(columnName).as(String.class), "%" + value + "%");
        // Pour toutes les autres entités (ex: Role), ignorer typeUser et utiliser seulement le filtre de base
        query.select(root).where(predicate);
        // Exécuter la requête
        return entityManager.createQuery(query).getResultList();
    }
}
