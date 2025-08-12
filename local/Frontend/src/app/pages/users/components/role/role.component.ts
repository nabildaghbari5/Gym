import { Component, EventEmitter, ViewChild } from '@angular/core';
import { RoleService } from '../../service/role.service';
import { Column } from 'src/app/shared/service/column';
import { Action } from 'src/app/shared/service/action';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Validators } from 'ngx-editor';
import { ModalComponent } from 'src/app/shared/modal/modal.component';
import { Router } from '@angular/router';
import { initPage, Page } from 'src/app/shared/models/page';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-role',
  templateUrl: './role.component.html',
  styleUrls: ['./role.component.scss']
})
export class RoleComponent {
  roleForm!: FormGroup;
  isUpdate!: boolean ;
  roles: any[] = []; 
  endItem: any;
  messageError='';
  pageNumber = 0
  pageSize =10
  rolePage: Page<any> = initPage
  onPaginationChange: EventEmitter<string> = new EventEmitter<string>();
  columns: Column[] = [];
  actions: Action[] = [];
  @ViewChild(ModalComponent) modalComponent!: ModalComponent;
  checkedListRole: any[]=[];
  openModal():void {
    this.modalComponent.openModal(this.modalComponent.modal);
  }
  StatusName = [
    { name: 'En attente' },
    { name: 'Actif' },
    { name: 'Inactif' },
  ];

  PermissionsName = [
    { name: 'Création' },
    { name: 'Modification' },
    { name: 'Suppression' },
    { name: 'Consultation' },
  ];
  constructor(
    private roleService:RoleService , 
    private fb: FormBuilder ,
    private router:Router,
    public toastService: ToastrService,

  ) {}
  ngOnInit(): void {
    this.initializeTable();
    this.findAllRole();
    this.onPaginationChange.subscribe(()=>this.findAllRole()) ;
    this.roleForm = this.fb.group({
      id: [''],
      name: ['', Validators.required],
      permissions: [[], Validators.required],
      status: ['', Validators.required],
      description: ['', Validators.required] ,
      nbrUtilisateur: ['0' ] 
    });
  }
  saveRole() {
     const roleData =this.roleForm.value ; 
     console.log(roleData)
      if (this.isUpdate) {
        if(this.roleForm.valid){
          this.roleService.create(roleData).subscribe({
            next:(data) => {
              this.isUpdate=false
              this.modalComponent.onClose();
              this.findAllRole();
            }
          })
        }
      } else { 
        if(this.roleForm.valid){
          this.roleService.create(roleData).subscribe({
            next:(data) => {
              this.findAllRole();
              this.modalComponent.onClose();
              this.roleForm.get('nbrUtilisateur')?.setValue(0);
            } , 
            error :(error) => {
            
            }
           }
          )
        }else{
          console.log('form role  invalid ')
        }
      } 
  } 
  findAllRole(){
     this.roleService.findPage(this.pageNumber, this.pageSize).subscribe({
      next:(data) => {
         this.roles=data.content ;
         this.rolePage=data;
      }, 
      error:(error) => {
        console.log("error du récupération de la liste de role")
      }
     }
     )
  }
  initializeTable(): void {
    this.columns = [
      { key: 'id', label: 'ID' },
      { key: 'name', label: 'Libellé', sortable: true },
      { key: 'permissions', label: 'Permissions', sortable: true },
      { key: 'description', label: 'Description', sortable: true },
      { key: 'nbrUtilisateur', label: 'Nombre D\’utilisateurs  ', sortable: false },
      { key: 'status', label: 'Status', sortable: true } 
    ];
    this.actions = [
      { type: 'edit', buttonClass:"btn btn-subtle-success btn-icon btn-sm edit-item-btn",  iconClass: 'ph-pencil', visible:true },
      { type: 'users', buttonClass:"btn btn-subtle-primary btn-icon btn-sm",  iconClass: ' bx bxs-user-detail', visible:true }
    ];
    // Ajout d'un champ 'selected' pour chaque rôle dans la liste
 
  }
 // Gestion des actions sur les rôles
  onAction(event: { id: number, action: string }): void {
    const role = this.roles.find(r => r.id === event.id);
    if (event.action === 'edit') {
      this.isUpdate=true
      this.roleForm.patchValue(role);  
      this.openModal();
    }else if(event.action === 'users'){
        this.router.navigate(['users/role_user' , role.id])
    } 
    // rest impl les autre action dans le tableau 
  } 
  onFilterChange(event: { key: string, searchValue: string }) {
    console.log('Recherche dans la colonne : ', event.key, ' avec la valeur : ', event.searchValue);
    this.roleService.searchInTable(event.key, event.searchValue ).subscribe({
      next:(data)=> {
        this.roles=data;       
      }
    }    
    );
  } 
  
  checkedList(event:any[]){
    this.checkedListRole=event;
    console.log(event);
  }
  onDeleteAllAction(){
    this.roleService.deleteAllRoles(this.checkedListRole).subscribe({
      next:(data)=>{
          this.findAllRole();
          this.toastService.success("Succès de suppression des rôles", 'Suppression de rôle', {
            timeOut:3000
           })
      }, 
      error:(err)=>{
         this.messageError=err.error.businessErrorDescription
         this.toastService.warning(this.messageError , 'Error' , {
          timeOut:3000
         })
         this.messageError=""; 
      }
    })
  }
  
  breadCrumbItems = [
    { label: 'users', link: '/' },
    { label: 'role', link: '/roles' }
  ];
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


