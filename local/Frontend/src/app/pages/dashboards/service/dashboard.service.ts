import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
 
  private apiUrl = 'http://localhost:8060/dashboard/api';
  constructor(private http: HttpClient) { }
  getActiveAdherentsCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/active-count`);
  }

  getInactifAdherentsCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/inactive-count`);
  }


  findHistoriquePaiement(selectedMonth: string, selectedYear: number) {
    return this.http.get<number>(`${this.apiUrl}/historique/${selectedMonth}/${selectedYear}`);
  }

  getBudget(selectedMonth: string, selectedYear: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/budget/${selectedMonth}/${selectedYear}`);
  }


  getMonthlyVariation(): Observable<Record<string, number>> {
    return this.http.get<Record<string, number>>(`${this.apiUrl}/monthly-variation`);
  }

  getGrowthRate(): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/growth-rate`);
  }

  getWeeklyVariation(): Observable<Record<string, number>> {
    return this.http.get<Record<string, number>>(`${this.apiUrl}/weekly-variation`);
  }

  getMontantCaisse(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/montant-total`);
  }
  
}
