
import { GroupsService } from './../../service/groups.service';
import { Component, EventEmitter, TemplateRef, ViewChild } from '@angular/core';
import { Validators } from 'ngx-editor';
import { FormBuilder, UntypedFormGroup } from '@angular/forms';
import { AuthService } from 'src/app/account/authentification/auth.service';
import { UserService } from '../../service/user.service';
import { Column } from 'src/app/shared/service/column';
import { Action } from 'src/app/shared/service/action';
import { ModalComponent } from 'src/app/shared/modal/modal.component';
import { forkJoin } from 'rxjs';
import { initPage, Page } from 'src/app/shared/models/page';

@Component({
  selector: 'app-user-interne',
  templateUrl: './user-interne.component.html',
  styleUrls: ['./user-interne.component.scss'], 
})
export class UserInterneComponent {


  // Bread crumb items
  breadCrumbItems!: Array<{}>;
  users: any[] = []; 
  signupForm!: UntypedFormGroup;
  isUpdate!:boolean;
  roles: any[] = []; 
  groups:any[]=[];
  endItem: any;
  pageNumber = 0
  pageSize =10
  userPage: Page<any> = initPage
  statusOptions:string[] = [];
  columns: Column[] = [];
  actions: Action[] = [];
  onPaginationChange: EventEmitter<string> = new EventEmitter<string>();
  @ViewChild(ModalComponent) modalComponent!: ModalComponent;
  openModal():void {
    this.modalComponent.openModal(this.modalComponent.modal);
  }
  
  selectedStatus: any = 'Sélectionner status';
  StatusName = [  
    { name: 'Actif' },
    { name: 'Inactif' },
  ];


  constructor(   
    private formBuilder: FormBuilder,
    private authEntreprise:AuthService,
    private userService:UserService,
    private groupsService:GroupsService,
  ) {
  }

  ngOnInit(): void {
    this.initializeTable();
    this.findAllUserInterne();
    this.loadData();
    this.onPaginationChange.subscribe(()=>this.findAllUserInterne());
    this.breadCrumbItems = [
      { label: 'Users', active: true },
      { label: 'user-interne', active: true },
    ];
    this.signupForm = this.formBuilder.group({
      id:[''],
      lastname: ['', Validators.required],
      firstname: ['', Validators.required],
      email: ['', [Validators.required]],
      password: ['BeProcess2024', [Validators.required]],
      telephone: ['', [Validators.required]],
      typeUser: ["UserInterne", [Validators.required]],  
      groups:[[] ] 
    });
    
  }
  initializeTable(): void {
    this.columns = [
      { key: 'id', label: 'ID' },
      { key: 'lastname', label: 'Nom', sortable: true },
      { key: 'firstname', label: 'Prénom', sortable: true },
      { key: 'telephone', label: 'Télephone', sortable: true },
      { key: 'status', label: 'Status', sortable: false }
    ];
    this.actions = [
      { type: 'edit', buttonClass:"btn btn-subtle-success btn-icon btn-sm edit-item-btn",  iconClass: 'ph-pencil', visible:true },
      { type: 'delete', buttonClass:"btn btn-subtle-danger btn-icon btn-sm",  iconClass: 'bx bxs-trash-alt', visible:true },
      { type: 'control', buttonClass:"btn btn-subtle-primary btn-icon btn-sm",  iconClass: 'ri-user-settings-line', visible:true }
    ];
    // Initialisation des statuts
   this.statusOptions = ['Actif', 'Inactif'];  
  }
 // Gestion des actions sur les users interne 
  onAction(event: { id: number, action: string }): void {
    if (event.action === 'edit') {
      const user = this.users.find(r => r.id === event.id);
      this.isUpdate=true
      this.signupForm.patchValue(user);  
      this.signupForm.patchValue({
        roles: user.roles.map((role: any) => role.name), // Typage explicite
        groups: user.groups.map((group: any) => group.name) // Typage explicite
      });
      this.openModal();
    } else if(event.action=== 'delete'){
       this.userService.delete(event.id).subscribe({
          next:(data)=>{
             this.findAllUserInterne();
          } 
       })
    }
    else {
      this.updateStatus(event.id , event.action);
      this.findAllUserInterne();   
    }
  }
  findAllUserInterne(){
    this.userService.findUsersByType(this.pageNumber , this.pageSize ,"UserInterne" ).subscribe({ 
      next:(users) => {
        this.users=users.content; 
        this.userPage=users;
      }
      }
    )
  }
  onFilterChange(event: { key: string, searchValue: string }) {
    console.log('Recherche dans la colonne : ', event.key, ' avec la valeur : ', event.searchValue);
    this.userService.searchInTableUser(event.key, event.searchValue , "UserInterne" , this.pageNumber , this.pageSize).subscribe({
      next:(data)=>{
       this.users=data.content;
       this.userPage=data;
      },  
      error:(err)=>{
           console.log('error filtrage user by type user ');
      }
    }    
     
    );
  }  
  saveUser() {
    const userData =this.signupForm.value ; 
     if (this.isUpdate) {
         this.userService.create(userData).subscribe({
           next:(data) => {
             this.isUpdate=false
             this.modalComponent.onClose();
             this.findAllUserInterne();
           }
         })
     } else { 
         this.authEntreprise.register(userData).subscribe({
           next:(data) => {
             this.findAllUserInterne();
             this.modalComponent.onClose();
           } , 
           error :(error) => {
            console.log(error)
           }
          }
        )
     } 
 } 








 
  updateStatus( id: number, status: string ) {
    this.userService
      .putStatusUsers(id, status, this.users[0])
      .subscribe((response) => {
        this.findAllUserInterne();
      });
  }

  loadData(){
    forkJoin({
        groups:this.groupsService.findAll()
    }).subscribe({
      next:(result) => {
        this.groups=result.groups
      },
      error: (err) => {
        console.error('Error fetching roles or groups:', err);
      }
    })
  }

  onPageNumberChange(pageNumber: number) {
    this.pageNumber = pageNumber
    this.onPaginationChange.emit('')
  }
  onPageSizeChange(pageSize: number) {
    this.pageSize = pageSize
    this.pageNumber = 0
    this.onPaginationChange.emit('')
  }

}


