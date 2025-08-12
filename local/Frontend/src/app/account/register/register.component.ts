import { AuthService } from 'src/app/account/authentification/auth.service';
import { Component } from '@angular/core';
import {  FormBuilder, UntypedFormGroup, Validators } from '@angular/forms';

import { Router, ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})

// Register Component
export class RegisterComponent {
  // Login Form
  signupForm!: UntypedFormGroup;
  submitted = false;
  successmsg = false;
  error = '';
  // set the current year
  year: number = new Date().getFullYear();

  fieldTextType!: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private authEntreprise:AuthService,
    private router: Router,
  ) { } 

  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      lastname: ['', Validators.required],
      firstname: ['', Validators.required],
      email: ['', [Validators.required]],
      password: ['', [Validators.required]],
      fonction: ['', [Validators.required]],
      telephone: ['', [Validators.required]],
      TypeUser: ['UserExterne', [Validators.required]], 
      status:['En attente'], 
  
    });

  }

    // convenience getter for easy access to form fields
    get f() { return this.signupForm.controls; }

  /**
   * Register submit form
   */
  signUp() {
    const formData = this.signupForm.value;
    this.authEntreprise.register(formData).subscribe({
      next: (data) => {
        console.log(data);
        this.router.navigate(["account/login"]);
      },
      error: (err) => {
        console.error('Erreur lors de l\'inscription :', err);
        // Vous pouvez également ajouter des actions pour gérer l'erreur ici
      }
    }); 
  }
  
  


 // Check if a control is invalid and has been touched
 isControlInvalid(controlName: string): boolean {
  const control = this.signupForm.controls[controlName];
  return control.invalid && control.touched;
}

// Check if a control is valid
isControlValid(controlName: string): boolean {
  const control = this.signupForm.controls[controlName];
  return control.valid;
}

// Check if a control has been touched
isControlTouched(controlName: string): boolean {
  const control = this.signupForm.controls[controlName];
  return control.touched;
}
   


  /**
 * Password Hide/Show
 */
  toggleFieldTextType() {
    this.fieldTextType = !this.fieldTextType;
  }

}
