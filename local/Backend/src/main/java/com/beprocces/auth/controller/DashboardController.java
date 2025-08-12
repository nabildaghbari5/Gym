package com.beprocces.auth.controller;

import com.beprocces.auth.model.Adherent;
import com.beprocces.auth.serviceImpl.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard/api")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService ;

    // Endpoint pour récupérer le nombre d'adhérents actifs
    @GetMapping("/active-count")
    public ResponseEntity<Long> getActiveAdherentsCount() {
        long count = dashboardService.getActiveAdherentsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/inactive-count")
    public ResponseEntity<Long> getInactifAdherent(){
     return  ResponseEntity.ok(dashboardService.getInactifAdherent());
    }

    @GetMapping("/weekly-variation")
    public ResponseEntity<Map<String, Long>> getWeeklyVariation() {
        Map<String, Long> variation = dashboardService.getWeeklyVariation();
        return ResponseEntity.ok(variation);
    }

    // Endpoint pour récupérer la variation mensuelle des adhérents actifs
    @GetMapping("/monthly-variation")
    public ResponseEntity<Map<String, Long>> getMonthlyVariation() {
        Map<String, Long> variation = dashboardService.getMonthlyVariation();
        return ResponseEntity.ok(variation);
    }

    // Endpoint pour récupérer le taux de croissance/diminution des adhérents
    @GetMapping("/growth-rate")
    public ResponseEntity<Map<String, String>> getGrowthRate() {
        Map<String, Long> variation = dashboardService.getMonthlyVariation();
        String growthRate = dashboardService.calculateGrowthRate(variation);
        Map<String, String> response = new HashMap<>();
        response.put("growthRate", growthRate);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/historique/{selectedMonth}/{selectedYear}")
    public ResponseEntity<List<Adherent>> findHistoriquePaiement(
            @PathVariable String selectedMonth,
            @PathVariable Integer selectedYear
    ) {
        int monthNumber = convertMonthNameToNumber(selectedMonth);
        return ResponseEntity.ok(dashboardService.findHistoriquePaiement(monthNumber, selectedYear));
    }
    private int convertMonthNameToNumber(String monthName) {
        List<String> months = List.of(
                "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        );
        return months.indexOf(monthName) + 1; // Renvoie l'index + 1 (car janvier = 1)
    }

    @GetMapping("/budget/{selectedMonth}/{selectedYear}")
    public ResponseEntity<Double> CalculBudgetMois(@PathVariable String selectedMonth ,@PathVariable Integer selectedYear ){
        int monthNumber = convertMonthNameToNumber(selectedMonth);
        return ResponseEntity.ok(dashboardService.calculBudgetMois(monthNumber,selectedYear));
    }

    @GetMapping("/montant-total")
    public ResponseEntity<Double> getMontantTotalAPayer() {
        Double montantTotal = dashboardService.getMontantTotalAPayer();
        return ResponseEntity.ok(montantTotal);
    }

}
