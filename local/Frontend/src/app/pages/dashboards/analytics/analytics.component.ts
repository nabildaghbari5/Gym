import { Component, EventEmitter, OnInit } from '@angular/core';
import { DashboardService } from '../service/dashboard.service';
import { forkJoin } from 'rxjs';
import { number } from 'echarts';

@Component({
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.scss'],
 
})

// Analytics Component
export class AnalyticsComponent implements OnInit {
  breadCrumbItems!: Array<{}>;
  statlist: any[] = [];
  num: number = 0;
  numInactif:number = 0;
  montantTotal:number = 0;
  nombreTotale:number = 0;
  years: number[] = [];
  selectedMonth: string;
  selectedYear: number;
  budget:any
  months = [
    'Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin',
    'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'
  ];
  historiquePaiement:any;
  onFilterChange: EventEmitter<string> = new EventEmitter<string>();

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    // Initialisation des breadcrumbs
    this.breadCrumbItems = [
      { label: 'Dashboards', active: true },
      { label: 'Analytics', active: true }
    ];
    this.initializeYears();
    // Charger les statistiques dynamiquement
   // this.loadStats();
   this.CountNumAdherent();
   this.getMontantTotale();
   this.onFilterChange.subscribe(() => this.findHistorique());  
  }

  CountNumAdherent(){
    forkJoin({
      activeCount:this.dashboardService.getActiveAdherentsCount(),
      InactifCount:this.dashboardService.getInactifAdherentsCount()
    }).subscribe(({activeCount , InactifCount}) => {
      this.num =activeCount
      this.numInactif =InactifCount
      this.nombreTotale = this.num + this.numInactif ;
    })
   }

   getMontantTotale(){
    this.dashboardService.getMontantCaisse().subscribe({
      next:(data)=>{
        this.montantTotal=data;
      }
    })
   }

   onMonthChange(event: any): void {
    this.selectedMonth = event;
    this.onFilterChange.emit('')
  }

  onYearChange(event: any): void {
    this.selectedYear = event;
    this.onFilterChange.emit('')
  }

  initializeYears(): void {
    const currentYear = new Date().getFullYear();
    const startYear = 2020; // Par exemple, commencer à partir de l'année 2000
    for (let year = startYear; year <= currentYear; year++) {
      this.years.push(year);
    }
  }
 

  findHistorique(): void {
    if (this.selectedMonth && this.selectedYear) {
        this.dashboardService.findHistoriquePaiement(this.selectedMonth, this.selectedYear).subscribe( 
          data => {
             this.historiquePaiement=data ;
        }, error => {
          console.error('Erreur lors de la récupération des timesheets:', error);
        });
        this.getBudget(this.selectedMonth);
    }
  }
  
  getBudget(selectMonths:any){
      this.dashboardService.getBudget(selectMonths , this.selectedYear).subscribe({
        next:(data)=>{
          this.budget=data;
        }
      })
  }








   // state avec gourbe 

  private loadStats(): void {
    // Appel à l'API pour récupérer les données et les structurer
    this.dashboardService.getActiveAdherentsCount().subscribe((activeCount: number) => {
      this.dashboardService.getMonthlyVariation().subscribe((monthlyVariation: Record<string, number>) => {
        this.dashboardService.getWeeklyVariation().subscribe((weeklyVariation: Record<string, number>) =>{
          this.dashboardService.getGrowthRate().subscribe((growthRate: string) => {
            this.statlist = [
              {
                title: "Nombre d'adhérents actifs",
                count: activeCount.toString(),
                counttyyp: '',
                avg: `${growthRate} Last Month`,
                icon: activeCount > 0 ? 'bi bi-arrow-up' : 'bi bi-arrow-down',
                color: activeCount > 0 ? 'success' : 'danger',
                chart: {
                  series: [{
                    name: "Adhérents",
                    data: this.mapWeeklyDataToDays(weeklyVariation)
                  }],
                  chart: {
                    type: 'line',
                    height: 124 , 
                    width:300 ,
                  },
                  stroke: { width: 2, curve: 'smooth' },
                  colors: ['#3762ea'],
                  xaxis: {
                    categories: ['Semaine 1', 'Semaine 2', 'Semaine 3', 'Semaine 4'], // Jours complets
                    labels: {
                      style: {
                        fontSize: '10px',
                      }
                    }
                  }, 
                  yaxis: {
                    min: 0, // Début de l'axe Y à 0
                    forceNiceScale: true, // Force l'échelle agréable
                    tickAmount: 5, // Nombre de graduations
                    labels: {
                      formatter: function (val: number) {
                        return Math.round(val).toString(); // Affiche uniquement les entiers
                      }
                    }
                  }
                }
              },
              // Ajoutez d'autres objets pour d'autres statistiques si nécessaire
            ];
  
            // Ajouter des options pour les animations
            for (const dataItem of this.statlist) {
              if (dataItem.count.includes('.')) {
                dataItem.option = {
                  startVal: this.num,
                  useEasing: true,
                  duration: 2,
                  decimalPlaces: 2
                };
              } else {
                dataItem.option = {
                  startVal: this.num,
                  useEasing: true,
                  duration: 2
                };
              }
            }
          });
        })
      });
    });
  }

  mapWeeklyDataToDays(weeklyVariation: { [key: string]: number }): number[] {
    // Ordre des jours attendu : Dimanche à Samedi
    const daysOrder = ['Semaine 1', 'Semaine 2', 'Semaine 3', 'Semaine 4'];
  
    // Crée un tableau avec des valeurs par défaut de 0 pour chaque jour
    const alignedData = daysOrder.map(day => weeklyVariation[day] || 0);
  
    return alignedData;
  }
}