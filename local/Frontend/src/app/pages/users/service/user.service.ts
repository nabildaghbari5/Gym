import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Inject, Injectable } from '@angular/core';
import { API_CONFIG, ApiConfig } from 'src/app/shared/service/apiConfig';
import { BaseService } from 'src/app/shared/service/base.service';

@Injectable({
  providedIn: 'root'
})
export class UserService  extends BaseService<any>  {
 

  constructor(http: HttpClient, @Inject(API_CONFIG) config: ApiConfig) {
    super(http, config);
    this.setPath('api/user'); 
  }


  findUsersByType(pageNumber:number , pageSize: number , typeUser: string): Observable<any> { 
    const url = `${this.getUrl()}/getByType/${typeUser}`;
    let  params = new HttpParams();
    params =params.append('pageNumber',pageNumber);
    params=params.append('pageSize', pageSize);
    return this.http.get<any>(url , {params});
  }

  // Méthode pour changer le statut d'un utilisateur
  putStatusUsers(idUser: number, idStatus: string, data: any): Observable<any> {
    const url = `${this.getUrl()}/accepterOrRefuser/${idUser}/${idStatus}`;
    return this.http.put(url, data); 
  }

  removeRoleFromUser(userId:number , roleId:number):Observable<void>{
    const url =`${this.getUrl()}/roles/${userId}/${roleId}`;
    return this.http.delete<void>(url);
  } 

  removeGroupFromUser(userId:number , groupId:number):Observable<any>{
    const url =`${this.getUrl()}/groups/${userId}/${groupId}`;
    return this.http.delete<void>(url);
  }

  searchInTableUser(
    columnName: string, 
    value: string , 
    typeUser:string ,    
    pageNumber:number , 
    pageSize: number
   ): Observable<any> {
   const url = `${this.getUrl()}/search/typeUser/${typeUser}`; 
   const params = new HttpParams()
     .set('columnName', columnName)
     .set('value', value)
     .set('pageNumber', pageNumber)
     .set('pageSize', pageSize)
   return this.http.get<any>(url, { params });   
 }
            
  
}

