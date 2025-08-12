import { HttpClient, HttpParams } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_CONFIG, ApiConfig } from 'src/app/shared/service/apiConfig';
import { BaseService } from 'src/app/shared/service/base.service';

@Injectable({
  providedIn: 'root'
})
export class CoachService extends BaseService<any> {

   constructor(http: HttpClient, @Inject(API_CONFIG) config: ApiConfig) {
      super(http, config);
      this.setPath('api/coach');  
    }


     findCoachsPage(groupsId:number , pageNumber:number , pageSize: number): Observable<any> {
        const url = `${this.getUrl()}/coachs/${groupsId}`; 
        let params = new HttpParams();
        params = params.append('pageNumber', pageNumber);
        params = params.append('pageSize', pageSize);
        return this.http.get<any>(url, {params}); 
       }


      removeGroupFromUser(userId:number , groupId:number):Observable<any>{
        const url =`${this.getUrl()}/groups/${userId}/${groupId}`;
        return this.http.delete<void>(url);
      }

  }   