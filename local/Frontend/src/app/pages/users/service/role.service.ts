import { HttpClient, HttpParams } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { List } from 'echarts';
import { Observable } from 'rxjs';
import { API_CONFIG, ApiConfig } from 'src/app/shared/service/apiConfig';
import { BaseService } from 'src/app/shared/service/base.service';

@Injectable({
  providedIn: 'root'
})
export class RoleService extends BaseService<any> {

  constructor(http: HttpClient, @Inject(API_CONFIG) config: ApiConfig) {
    super(http, config);
    this.setPath('api/role'); 
  }


  findUserPage(roleId:number , pageNumber:number , pageSize: number): Observable<any> {
    const url = `${this.getUrl()}/users/${roleId}`; 
    let params = new HttpParams();
    params = params.append('pageNumber', pageNumber);
    params = params.append('pageSize', pageSize);
    return this.http.get<any>(url, {params}); 
   }

   searchInTableRole(
    columnName: string, 
    value: string , 
    roleId:number ,    
    pageNumber:number , 
    pageSize: number
   ): Observable<any> {
   const url = `${this.getUrl()}/search/user/${roleId}`; 
   const params = new HttpParams()
     .set('columnName', columnName)
     .set('value', value)
     .set('pageNumber', pageNumber)
     .set('pageSize', pageSize)
    return this.http.get<any>(url, { params });  
  }  

 deleteAllRoles(roles:any[] ):Observable<any>{
  const url = `${this.getUrl()}/deleteAll`;  
   return this.http.request<any>('delete' , url , {
    body:roles
   }) ; 
 }
            
  
}
