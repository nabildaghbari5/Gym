import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../service/dashboard.service';

@Component({
  selector: 'app-historique',
  templateUrl: './historique.component.html',
  styleUrl: './historique.component.scss'
})
export class HistoriqueComponent implements OnInit {
  months = [
    { name: 'Janvier', value: 1 },
    { name: 'Février', value: 2 },
    { name: 'Mars', value: 3 },
    { name: 'Avril', value: 4 },
    { name: 'Mai', value: 5 },
    { name: 'Juin', value: 6 },
    { name: 'Juillet', value: 7 },
    { name: 'Août', value: 8 },
    { name: 'Septembre', value: 9 },
    { name: 'Octobre', value: 10 },
    { name: 'Novembre', value: 11 },
    { name: 'Décembre', value: 12 }
  ];
  years = [2023, 2024 ,2025];
  selectedMonth: string | null = null;
  selectedYear: number | null = null;
  historiquePaiement: any;
  budget: number = 0;

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    // Initialisation si nécessaire
  }

  onFilterChange(): void {
    if (this.selectedMonth && this.selectedYear) {
      this.findHistorique();
    }
  }

  findHistorique(): void {
    this.dashboardService
      .findHistoriquePaiement(this.selectedMonth, this.selectedYear)
      .subscribe({
        next: (data) => {
          this.historiquePaiement = data;
          this.getBudget(this.selectedMonth);
        },
        error: (error) => {
          console.error('Erreur lors de la récupération des historiques :', error);
        }
      });
  }

  getBudget(month: string): void {
    this.dashboardService.getBudget(month, this.selectedYear).subscribe({
      next: (data) => {
        this.budget = data;
      },
      error: (error) => {
        console.error('Erreur lors de la récupération du budget :', error);
      }
    });
  }
}