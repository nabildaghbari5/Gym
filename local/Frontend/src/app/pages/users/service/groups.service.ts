import { HttpClient, HttpParams } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_CONFIG, ApiConfig } from 'src/app/shared/service/apiConfig';
import { BaseService } from 'src/app/shared/service/base.service';

@Injectable({
  providedIn: 'root'
})
export class GroupsService extends BaseService<any> {

  constructor(http:HttpClient , @Inject(API_CONFIG) config:ApiConfig) { 
    super(http,config);
    this.setPath('api/groups');
  }


  findUserPage(groupsId:number , pageNumber:number , pageSize: number): Observable<any> {
    const url = `${this.getUrl()}/users/${groupsId}`; 
    let params = new HttpParams();
    params = params.append('pageNumber', pageNumber);
    params = params.append('pageSize', pageSize);
    return this.http.get<any>(url, {params}); 
   }

   searchInTableGroups(
     columnName: string, 
     value: string , 
     groupId:number ,  
     pageNumber:number , 
     pageSize: number
    ): Observable<any> {
    const url = `${this.getUrl()}/search/user/${groupId}`; 
    const params = new HttpParams()
      .set('columnName', columnName)
      .set('value', value)
      .set('pageNumber', pageNumber)
      .set('pageSize', pageSize)
    return this.http.get<any>(url, { params });  
  }

}
