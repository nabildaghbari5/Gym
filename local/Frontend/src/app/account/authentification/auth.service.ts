import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PasswordReset } from '../model/passwordReset';
import { AuthenticationResponse } from '../model/authentication-response';
import { RegisterRequest } from '../model/register-request';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private http :HttpClient , 
  ) { 
  
  }

  private baseUrl = 'http://localhost:8060/api/auth' 

  private userIdFromLocalStorage: string | null = null;
  private fullNameFromLocalStorage: string | null = null;


  login(loginResuest:any) {
   let url = `${this.baseUrl}/authenticate`;  
   console.log("this url ::::>" , url) ; 
   return this.http.post(url,loginResuest ) ;
   
  } 


  resetPassword(passwordResetRequest: PasswordReset, token: string): Observable<string> {
    const endpoint = '/reset-password?token=' + token;
    return this.http.post<string>(`${this.baseUrl}${endpoint}`, passwordResetRequest);
  }


  resetPasswordRequest(passwordResetRequest:PasswordReset):Observable<string>{
    const endpoint = '/password-reset-request';
    return this.http.post<string>(`${this.baseUrl}${endpoint}`, passwordResetRequest);
  }


  //signup/please-verify
  verifyEmail(token: string): Observable<string> {
    const url = `${this.baseUrl}/verifyEmail?token=${token}`;
    return this.http.get<string>(url);
  }

   register(registerRequest: any): Observable<any> {
    return this.http.post<any>
    (`${this.baseUrl}/register`, registerRequest); 
  }

  

  confirm(token: string) {
    const url = `${this.baseUrl}/activate-account`;
    return this.http.get(url, { params: { token } });
  }



  public retrieveUserFromLocalStorage(): void {
    const currentUserString = localStorage.getItem('currentUser');
    if (currentUserString) {
      const currentUser = JSON.parse(currentUserString);
      const userId = currentUser.id;
      const fullName = `${currentUser.firstname} ${currentUser.lastname}`;
      
      console.log("ID de l'utilisateur récupéré depuis le localStorage :", userId);
      console.log("Full Name :", fullName); 
      
      this.userIdFromLocalStorage = userId;
      this.fullNameFromLocalStorage = fullName;
    } else {    
      console.log("Aucun utilisateur n'est actuellement connecté");
    }
  }

  getUserId(): string | null {
    return this.userIdFromLocalStorage;
  }

  getFullName(): string | null {
    return this.fullNameFromLocalStorage;
  }


}
