package com.beprocces.auth.repository;

import com.beprocces.auth.model.Adherent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdherentRepository extends JpaRepository<Adherent,Integer> {
    @Query("SELECT a FROM Adherent a WHERE a.status = :status")
    Page<Adherent> findByStatus(@Param("status") String status, Pageable sortedPage);

    @Query("SELECT a FROM Adherent a WHERE a.dateExpiration < :currentDate AND a.status != 'Expiré'")
    List<Adherent> findExpiredAdherents(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT SUM(a.montantAPayer) FROM Adherent a")
    Double calculerMontantTotalAPayer();
    //dashboard
    // Compter les adhérents ayant un statut spécifique
    long countByStatus(String status);

    @Query("SELECT a FROM Adherent a WHERE a.status = :status")
    List<Adherent> findByStatusList(@Param("status") String status);

    @Query("SELECT a FROM Adherent a WHERE FUNCTION('MONTH', a.dateInscription) = :selectedMonth AND FUNCTION('YEAR', a.dateInscription) = :selectedYear")
    List<Adherent> findHistoriquePaiement(@Param("selectedMonth") Integer selectedMonth, @Param("selectedYear") Integer selectedYear);


    @Query("SELECT COALESCE(SUM(a.montantAPayer), 0) FROM Adherent a WHERE FUNCTION('MONTH', a.dateInscription) = :monthNumber AND FUNCTION('YEAR', a.dateInscription) = :selectedYear")
    Double calculBudgetMois(@Param("monthNumber") int monthNumber , @Param("selectedYear") Integer selectedYear);
}
