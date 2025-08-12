package com.beprocces.auth.serviceImpl;
import com.beprocces.auth.model.Adherent;
import com.beprocces.auth.repository.AdherentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AdherentRepository adherentRepository ;
    // Récupérer le nombre d'adhérents actifs
    public long getActiveAdherentsCount() {
        return adherentRepository.countByStatus("actif");
    }


    public Map<String, Long> getWeeklyVariation() {
        // Récupérer les adhérents actifs
        List<Adherent> adherents = adherentRepository.findByStatusList("Actif");

        // Initialiser une map pour compter les adhérents par semaine du mois
        Map<String, Long> weeklyVariation = new HashMap<>();

        // Récupérer la date actuelle
        LocalDate currentDate = LocalDate.now();

        // Calculer la première semaine du mois actuel
        int firstDayOfMonthWeek = currentDate.withDayOfMonth(1).get(WeekFields.of(Locale.FRANCE).weekOfYear());

        // Filtrer les adhérents inscrits dans le mois actuel
        for (Adherent adherent : adherents) {
            LocalDate dateInscription = adherent.getDateInscription();

            // Vérifier si l'inscription est dans le mois actuel
            if (dateInscription.getMonthValue() == currentDate.getMonthValue() &&
                    dateInscription.getYear() == currentDate.getYear()) {

                // Calculer le numéro de la semaine de l'année pour l'adhérent
                int weekOfYear = dateInscription.get(WeekFields.of(Locale.FRANCE).weekOfYear());

                // Créer une clé de semaine sous forme de "Semaine 1", "Semaine 2", etc.
                String weekKey = "Semaine " + (weekOfYear - firstDayOfMonthWeek + 1);

                // Incrémenter le compteur pour cette semaine
                weeklyVariation.put(weekKey, weeklyVariation.getOrDefault(weekKey, 0L) + 1);
            }
        }

        // Si une semaine n'a pas d'adhérent, on l'ajoute avec la valeur 0
        for (int i = 1; i <= 4; i++) { // Nombre maximum de semaines dans un mois
            String weekKey = "Semaine " + i;
            weeklyVariation.putIfAbsent(weekKey, 0L);
        }

        return weeklyVariation;
    }



    // Calculer la variation des adhérents actifs par mois
    public Map<String, Long> getMonthlyVariation() {
        // Récupérer les adhérents et leur date d'inscription
        List<Adherent> adherents = adherentRepository.findByStatusList("Actif");
        Map<String, Long> monthlyVariation = new HashMap<>();

        for (Adherent adherent : adherents) {
            // On peut extraire le mois et l'année de la date d'inscription pour calculer la variation
            String month = adherent.getDateInscription().getMonth().toString() + "-" + adherent.getDateInscription().getYear();
            monthlyVariation.put(month, monthlyVariation.getOrDefault(month, 0L) + 1);
        }

        return monthlyVariation;
    }

    // Calculer le taux d'augmentation/diminution par mois
    public String calculateGrowthRate(Map<String, Long> monthlyVariation) {
        List<Long> values = new ArrayList<>(monthlyVariation.values());

        if (values.size() < 2) return "0%"; // Si on a moins de 2 mois de données, on ne peut pas calculer le taux de croissance

        // Calculer la variation entre les mois
        long previous = values.get(values.size() - 2);
        long current = values.get(values.size() - 1);
        double growthRate = ((double) (current - previous) / previous) * 100;

        return String.format("%.2f%%", growthRate);
    }

    public Long getInactifAdherent() {
        return adherentRepository.countByStatus("Expiré");
    }

    public List<Adherent> findHistoriquePaiement(Integer selectedMonth, Integer selectedYear ) {
    return adherentRepository.findHistoriquePaiement(selectedMonth ,selectedYear ) ;
    }

    public Double calculBudgetMois(int monthNumber , Integer selectedYear) {
        return adherentRepository.calculBudgetMois(monthNumber ,selectedYear);
    }

    public Double getMontantTotalAPayer() {
        return adherentRepository.calculerMontantTotalAPayer();
    }
}
