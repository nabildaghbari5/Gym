package com.beprocces.auth.serviceImpl;
import com.beprocces.auth.controller.EmailExpirationRequest;
import com.beprocces.auth.email.EmailService;
import com.beprocces.auth.email.EmailTemplateName;
import com.beprocces.auth.model.Adherent;
import com.beprocces.auth.model.Groups;
import com.beprocces.auth.model.User;
import com.beprocces.auth.repository.AdherentRepository;
import com.beprocces.auth.repository.GroupsRepository;
import com.beprocces.auth.service.AdherentService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdherentServiceImpl  implements AdherentService {

    private final AdherentRepository adherentRepository ;
    private final GroupsRepository groupsRepository ;
    private final EmailService emailService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Adherent create(Adherent adherent) {
        adherent.setDateInscription(LocalDate.now());
        // calcule la date d'expération a partir de date d'inscription et nombre de mois
        if(adherent.getMonths() !=null){
            LocalDate dateExpiration = adherent.getDateInscription().plusMonths(adherent.getMonths());
            adherent.setDateExpiration(dateExpiration);
        }
        //calcule d'age
        if(adherent.getDateDeNaissance() !=null){
          int age = Period.between(adherent.getDateDeNaissance() , LocalDate.now()).getYears();
          adherent.setAge(age);
        }
        adherent.setStatus("Actif");
        if (adherent.getActivite() != null && !adherent.getActivite().isEmpty()) {
            Set<Groups> validatedActivites = new HashSet<>();
            for (Groups activite : adherent.getActivite()) {
                Groups existingActivite = groupsRepository.findById(activite.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Groups not found by id: " + activite.getId()));

                // Ajout de l'adherent dans les groupes pour maintenir la cohérence bidirectionnelle
                existingActivite.getAdherents().add(adherent);

                validatedActivites.add(existingActivite);
            }
            adherent.setActivite(validatedActivites);
            System.out.println("Activités associées avant la sauvegarde : " + adherent.getActivite());
        }


        return adherentRepository.save(adherent);
    }

    @Override
    public List<Adherent> findAll() {
        return null;
    }

    @Override
    public Page<Adherent> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Adherent findById(Integer id) {
        return adherentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adherent  not found bu id : " + id ));
    }

    @Override
    public Adherent update(Integer id, Adherent adherent) {
        // Récupération de l'adhérent existant
        Adherent adherentExist = adherentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adherent not found by id : " + id));

        // Mise à jour des champs simples
        adherentExist.setFirstname(adherent.getFirstname());
        adherentExist.setLastname(adherent.getLastname());
        adherentExist.setDateDeNaissance(adherent.getDateDeNaissance());
        adherentExist.setTelephone(adherent.getTelephone());
        adherentExist.setAbonnement(adherent.getAbonnement());
        adherentExist.setMonths(adherent.getMonths());
        adherentExist.setPrice(adherent.getPrice());
        adherentExist.setMontantAPayer(adherent.getMontantAPayer());
        //calcule date
        if(adherent.getMonths() !=null){
            LocalDate dateExpiration = adherentExist.getDateInscription().plusMonths(adherent.getMonths());
            adherentExist.setDateExpiration(dateExpiration);
        }
        //calcule d'age
        if(adherent.getDateDeNaissance() !=null){
            int age = Period.between(adherent.getDateDeNaissance() , LocalDate.now()).getYears();
            adherent.setAge(age);
        }
        // Gestion des activités
        if (adherent.getActivite() != null) {
            // Vérifier si les activités sont identiques
            if (!adherent.getActivite().equals(adherentExist.getActivite())) {
                // Supprimer l'adhérent des anciennes activités
                if (adherentExist.getActivite() != null) {
                    for (Groups oldGroup : adherentExist.getActivite()) {
                        oldGroup.getAdherents().remove(adherentExist);
                    }
                }
                // Associer les nouvelles activités
                Set<Groups> validatedActivites = new HashSet<>();
                for (Groups activite : adherent.getActivite()) {
                    Groups existingActivite = groupsRepository.findById(activite.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Groups not found by id: " + activite.getId()));

                    // Mise à jour des relations bidirectionnelles
                    existingActivite.getAdherents().add(adherentExist);
                    validatedActivites.add(existingActivite);
                }

                adherentExist.setActivite(validatedActivites);
            }
        } else {
            // Si aucune activité n'est fournie, dissocier les anciennes activités
            if (adherentExist.getActivite() != null) {
                for (Groups oldGroup : adherentExist.getActivite()) {
                    oldGroup.getAdherents().remove(adherentExist);
                }
            }
            adherentExist.setActivite(null);
        }

        // Sauvegarde de l'adhérent mis à jour
        return adherentRepository.save(adherentExist);
    }




    @Override
    public void deleteById(Integer id) {
        Adherent adherent = adherentRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Adherent not found"));
        for(Groups groups:adherent.getActivite()){
          groups.getAdherents().remove(adherent);
        }
        adherent.getActivite().clear();

        adherentRepository.deleteById(id);
    }

    @Scheduled(cron = "0 43 18 * * ?") // Exécuté tous les jours à minuit
  // @Scheduled(cron = "0 34 13 * * ?")
    public void updateExpiredAdherents() throws MessagingException {
        System.out.println("La tâche s'est exécutée à 01:45");
        List<Adherent> adherents = adherentRepository.findExpiredAdherents(LocalDate.now());
        List<Adherent> updatedAdherents = new ArrayList<>();
        for (Adherent adherent : adherents) {
            if (adherent.getDateExpiration() != null && adherent.getDateExpiration().isBefore(LocalDate.now())) {
                adherent.setStatus("Expiré");
                updatedAdherents.add(adherent);
                sendEmail(adherent);
            }

        }
        if (!updatedAdherents.isEmpty()) {
            adherentRepository.saveAll(updatedAdherents);
        }
    }



    public void sendEmail(Adherent adherent) throws MessagingException {
        emailService.sendEmailCalifornia(
                "nabil.daghbari5@gmail.com" ,
                adherent.getFirstname(),
                adherent.getLastname() ,
                adherent.getTelephone() ,
                adherent.getDateInscription() ,
                adherent.getDateExpiration() ,
                EmailTemplateName.GYM ,
                "Abonnement expiré. "
        );
    }

    @Override
    public Page<Adherent> findByStatus(String status, Pageable pageable) {
        Pageable sortedPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return adherentRepository.findByStatus(status, sortedPage);
    }

    @Override
    public Adherent updateAbonnement(Integer idAdherent, Adherent adherent) {
        Adherent adherentExist = this.adherentRepository.findById(idAdherent)
                .orElseThrow(()-> new IllegalArgumentException("Adherent not found by Id : " + idAdherent));

        adherentExist.setDateInscription(LocalDate.now());
        if(adherent.getMonths() !=null){
            LocalDate dateExpiration = adherentExist.getDateInscription().plusMonths(adherent.getMonths());
            adherentExist.setDateExpiration(dateExpiration);
        }
        adherentExist.setPrice(adherent.getPrice());
        adherentExist.setMonths(adherent.getMonths());
        adherentExist.setMontantAPayer(adherent.getMontantAPayer());
        adherentExist.setStatus("Actif");
        return adherentRepository.save(adherentExist);
    }

    @Override
    public Page<Adherent> search(String columnName, String value, PageRequest page) {
        // Créer une requête de base
        String queryString = "SELECT a FROM Adherent a WHERE 1=1";

        // Ajouter les conditions dynamiques en fonction de la colonne
        if ("firstname".equals(columnName)) {
            queryString += " AND LOWER(a.firstname) LIKE LOWER(CONCAT('%', :value, '%'))";
        } else if ("lastname".equals(columnName)) {
            queryString += " AND LOWER(a.lastname) LIKE LOWER(CONCAT('%', :value, '%'))";
        } else if ("telephone".equals(columnName)) {
            queryString += " AND a.telephone LIKE CONCAT('%', :value, '%')";
        }

        // Créer une requête triée et paginée
        Pageable sortedPage = PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by(Sort.Direction.ASC, "id"));
        TypedQuery<Adherent> query = entityManager.createQuery(queryString, Adherent.class);
        query.setParameter("value", value);

        // Récupérer la liste des résultats
        List<Adherent> results = query.setFirstResult((int) sortedPage.getOffset())
                .setMaxResults(sortedPage.getPageSize())
                .getResultList();

        // Compter le nombre total de résultats (sans pagination)
        String countQueryString = queryString.replace("SELECT a", "SELECT COUNT(a)");
        TypedQuery<Long> countQuery = entityManager.createQuery(countQueryString, Long.class);
        countQuery.setParameter("value", value);
        long total = countQuery.getSingleResult();

        // Retourner la page
        return new PageImpl<>(results, sortedPage, total);
    }


}
