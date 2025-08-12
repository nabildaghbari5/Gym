import { Component, EventEmitter, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Validators } from 'ngx-editor';
import { ModalComponent } from 'src/app/shared/modal/modal.component';
import { Action } from 'src/app/shared/service/action';
import { Column } from 'src/app/shared/service/column';
import { GroupsService } from '../../service/groups.service';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { Router } from '@angular/router';
import { initPage, Page } from 'src/app/shared/models/page';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrl: './groups.component.scss'
})
export class GroupsComponent {
  groupForm!: FormGroup;
  isUpdate!: boolean;
  groups: any[] = [];
  endItem: any;
  pageNumber = 0
  pageSize = 10
  groupPage: Page<any> = initPage
  onPaginationChange: EventEmitter<string> = new EventEmitter<string>();
  columns: Column[] = [];
  actions: Action[] = [];
  @ViewChild(ModalComponent) modalComponent!: ModalComponent;
  openModal(): void {
    this.modalComponent.openModal(this.modalComponent.modal); 
  }
  constructor(
    private formBuilder: FormBuilder,
    private groupsService: GroupsService , 
    private router:Router ,
    public toastService: ToastrService,
  ) { }
  ngOnInit(): void {
    this.initializeTable();
    this.findAllGroups();
    this.onPaginationChange.subscribe(()=>this.findAllGroups());
    this.groupForm = this.formBuilder.group({
      id: [''],
      name: ['', Validators.required],
      description: ['', Validators.required],
      nbrUtilisateur: ['0']
    });
  }
  initializeTable(): void {
    this.columns = [
      { key: 'id', label: 'ID' },
      { key: 'name', label: 'Libellé', sortable: true },
      { key: 'description', label: 'Description', sortable: true },
    ]; 
    this.actions = [
      { type: 'edit', buttonClass: "btn btn-subtle-success btn-icon btn-sm edit-item-btn", iconClass: 'ph-pencil', visible: true },
      { type: 'users', buttonClass:"btn btn-subtle-primary btn-icon btn-sm",  iconClass: ' bx bxs-user-detail', visible:true },
      { type: 'delete', buttonClass:"btn btn-subtle-danger btn-icon btn-sm",  iconClass: 'bx bxs-trash-alt', visible:true }

    ]; 
  }
  onAction(event: { id: number, action: string }): void {
    const group = this.groups.find(r => r.id === event.id);
    if (event.action === 'edit') {
      this.isUpdate=true
      this.groupForm.patchValue(group);  
      this.openModal();
      
    } else if(event.action === 'users'){
      this.router.navigate(['users/groups_user' , group.id]) 
    } else if(event.action=== 'delete'){
      this.groupsService.delete(group.id).subscribe({
        next:(data)=>{
          this.findAllGroups(); 
          this.toastService.success("Activité supprimée avec succès", 'Suppression d\'une activité', {
            timeOut: 3000
          });         
        }
      })
    }
  }
  findAllGroups() {
    this.groupsService.findPage(this.pageNumber, this.pageSize).subscribe({
      next: (data) => {
        this.groups = data.content;
        this.groupPage=data; 
      },
      error: (error) => {
        console.log("error du récupération de la liste de groups")
      }
    }
    )
  }
  onFilterChange(event: { key: string, searchValue: string }) {
    console.log('Recherche dans la colonne : ', event.key, ' avec la valeur : ', event.searchValue);
    this.groupsService.searchInTable(event.key, event.searchValue).subscribe({
      next:(data)=> {
        this.groups=data ;
      }
    }    
    );
  }  
  saveGroups(){
    const groupsData =this.groupForm.value ; 
     if (this.isUpdate) {
       if(this.groupForm.valid){
         this.groupsService.create(groupsData).subscribe({
           next:(data) => {
             this.isUpdate=false
             this.modalComponent.onClose();
             this.findAllGroups();
             this.toastService.success("Activité modifiée avec succès", 'Modification d\'une activité', {
              timeOut: 3000
             });          
           }
         })
       }
     } else { 
       if(this.groupForm.valid){
         this.groupsService.create(groupsData).subscribe({
           next:(data) => {
             this.findAllGroups();
             this.modalComponent.onClose();
             this.groupForm.get('nbrUtilisateur')?.setValue(0);
             this.toastService.success("Activité ajoutée avec succès", 'Ajout d\'une activité', {
              timeOut: 3000
             });
           } , 
           error :(error) => {
           
           }
          }
         )
       }else{
         console.log('form group invalid ')
       }
     } 
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
  breadCrumbItems = [
    { label: 'groups', link: '/' },
    { label: 'groups', link: '/list' }
  ];
}
