import { Component } from '@angular/core';
import { FormBuilder, FormGroup, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { AuthService } from '../authentification/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

// Login Component
export class LoginComponent {

  // Login Form
  loginForm!: FormGroup;
  submitted = false;
  fieldTextType!: boolean;
  error = '';
  returnUrl!: string;
  a: any = 10;
  b: any = 20;
  toast!: false;
  userConnected:any ; 
  messageError='';
  // set the current year
  year: number = new Date().getFullYear();

  // tslint:disable-next-line: max-line-length
  constructor(
    private formBuilder: FormBuilder,
    private authEntreprise:AuthService,
    private router: Router,
    public toastService: ToastrService,
    ) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required , Validators.minLength(8)]], 
    });
  }

  // convenience getter for easy access to form fields
  get f() { return this.loginForm.controls; }  

  /**
   * Form submit
   */
  onSubmit() {
    this.submitted = true; 
    const loginData = this.loginForm.value ;
     this.authEntreprise.login(loginData).subscribe({
      next: (data) => {
        this.userConnected=data 
        localStorage.setItem('currentUser', JSON.stringify(this.userConnected));  
        localStorage.setItem('token',this.userConnected.token as string);
        this.router.navigate(['/analytics']);
        this.toastService.success('Votre opération a réussi.', 'Succès', {  
          timeOut: 3000,
        });

    },
      error: (err) => {
        console.log(err)
        this.messageError = err.error.error;
        this.toastService.error(this.messageError, 'Error', { 
          timeOut: 3000,
        });
      }

  });
     
    
  
  }

  /**
   * Password Hide/Show
   */
  toggleFieldTextType() {
    this.fieldTextType = !this.fieldTextType;
  }


  isControlValid(controlName:string): boolean {
    const control = this.loginForm.controls[controlName];
    return control.valid && (control.dirty || control.touched);
  }
  
  isControlInvalid(controlName:string): boolean {
    const control = this.loginForm.controls[controlName];
    return control.invalid && (control.dirty || control.touched);
  }
  
  isControlTouched(controlName:string): boolean {
    const control = this.loginForm.controls[controlName];
    return control.dirty || control.touched;
  }

}
