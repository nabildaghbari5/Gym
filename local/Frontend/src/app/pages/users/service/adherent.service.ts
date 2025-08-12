import { HttpClient, HttpParams } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_CONFIG, ApiConfig } from 'src/app/shared/service/apiConfig';
import { BaseService } from 'src/app/shared/service/base.service';

@Injectable({
  providedIn: 'root'
})
export class AdherentService extends BaseService<any> {

   constructor(http: HttpClient, @Inject(API_CONFIG) config: ApiConfig) {
      super(http, config);
      this.setPath('api/adherent'); 
    }


   findUsersByStatus(pageNumber:number , pageSize: number , status: string): Observable<any> { 
        const url = `${this.getUrl()}/getByStatus/${status}`;
        let  params = new HttpParams();
        params =params.append('pageNumber',pageNumber);
        params=params.append('pageSize', pageSize);
        return this.http.get<any>(url , {params});
  }
   
  updateAbonnement(idAdherent:number , entity:any):Observable<any>{
    const url = `${this.getUrl()}/renouvellAbonnement/${idAdherent}`;
    return this.http.patch(url , entity);
  }


  
  searchInTableAdherent(
    columnName: string, 
    value: string , 
    pageNumber:number , 
    pageSize: number
   ): Observable<any> {
   const url = `${this.getUrl()}/search/adherent`; 
   const params = new HttpParams()
     .set('columnName', columnName)
     .set('value', value)
     .set('pageNumber', pageNumber)
     .set('pageSize', pageSize)
   return this.http.get<any>(url, { params });   
 }
}
