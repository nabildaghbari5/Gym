import { HttpClient, HttpParams } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_CONFIG, ApiConfig } from './apiConfig';

@Injectable({
  providedIn: 'root'
})
export class BaseService<T> {
  private apiUrl!: string;

  constructor(protected http: HttpClient, @Inject(API_CONFIG) private config: ApiConfig) {}

  protected setPath(path: string): void {
    this.apiUrl = `${this.config.apiUrl}${path}`;
  }

  protected getUrl(): string {
    return `${this.apiUrl}`;
  }
  
  findAll(): Observable<T[]> {
    return this.http.get<T[]>(this.getUrl());
  }

  findById( id: number): Observable<T> {
    return this.http.get<T>(`${this.getUrl()}/${id}`);
  }

  delete( id: number): Observable<void> {
    return this.http.delete<void>(`${this.getUrl()}/${id}`);
  }

  create( entity: T): Observable<T> {
    return this.http.post<T>(this.getUrl(), entity);
  }

  update( id:any  , entity: T): Observable<T> {
    return this.http.put<T>(`${this.getUrl()}/${id}`, entity);
  }  
  
  patch( id:any  , entity: T): Observable<T> {
    return this.http.patch<T>(`${this.getUrl()}/${id}`, entity);
  }  
   
  findPage(pageNumber:number , pageSize: number): Observable<any> {
    let url = this.getUrl()+ '/page';
    let params = new HttpParams();
    params = params.append('pageNumber', pageNumber);
    params = params.append('pageSize', pageSize);
    return this.http.get<any>(url, {params});
  }

  searchInTable(columnName: string, value: string ): Observable<any> {
    let url = this.getUrl()+ '/search';
    const params = new HttpParams()
      .set('columnName', columnName)
      .set('value', value)
    return this.http.get<any>(url, { params });  
  }

  deleteAll(entity:any[]):Observable<any>{
    let url =`${this.getUrl()}/deleteAll`
    return this.http.request<any>('delete' , url , {
      body:entity
    });
  }

}