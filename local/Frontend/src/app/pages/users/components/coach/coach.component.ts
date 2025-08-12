
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
import { CoachService } from '../../service/coach.service';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-coach',
  templateUrl: './coach.component.html',
  styleUrl: './coach.component.scss'
})
export class CoachComponent {


  // Bread crumb items
  breadCrumbItems!: Array<{}>;
  users: any[] = []; 
  signupForm!: UntypedFormGroup;
  isUpdate!:boolean;
  coachsId:number
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
@ViewChild("deleteModal") deleteModal!: ConfirmDialogComponent;
  
  selectedStatus: any = 'Sélectionner status';
  StatusName = [  
    { name: 'Actif' },
    { name: 'Inactif' },
  ];


  constructor(   
    private formBuilder: FormBuilder,
    private coachService:CoachService,
    private groupsService:GroupsService,
    public toastService: ToastrService ,
    
  ) {
  }

  ngOnInit(): void {  
    this.initializeTable();
    this.findAllCoach();
    this.loadData();
    this.onPaginationChange.subscribe(()=>this.findAllCoach());
    this.breadCrumbItems = [
      { label: 'Users', active: true },
      { label: 'user-interne', active: true },
    ];
    this.signupForm = this.formBuilder.group({
      id:[''],
      lastname: ['', Validators.required],
      firstname: ['', Validators.required],
      email: ['', [Validators.required]],
      telephone: ['', [Validators.required]],
      status: ['', [Validators.required]],  
      groupIds:[[]] 
    });
    
  }
  initializeTable(): void {
    this.columns = [
      { key: 'id', label: 'ID' },
      { key: 'lastname', label: 'Nom', sortable: false },
      { key: 'firstname', label: 'Prénom', sortable: false },
      { key: 'telephone', label: 'Télephone', sortable: false },
      { key: 'status', label: 'Status', sortable: false }
    ];
    this.actions = [
      { type: 'edit', buttonClass:"btn btn-subtle-success btn-icon btn-sm edit-item-btn",  iconClass: 'ph-pencil', visible:true },
      { type: 'delete', buttonClass:"btn btn-subtle-danger btn-icon btn-sm",  iconClass: 'bx bxs-trash-alt', visible:true },
    //  { type: 'control', buttonClass:"btn btn-subtle-primary btn-icon btn-sm",  iconClass: 'ri-user-settings-line', visible:true }
    ];
    // Initialisation des statuts
   this.statusOptions = ['Actif', 'Inactif'];  
  }
 // Gestion des actions sur les users interne 
  onAction(event: { id: number, action: string }): void {
    if (event.action === 'edit') {
      const user = this.users.find(r => r.id === event.id);
      this.isUpdate=true
      this.coachsId=user.id ;
      console.log(user);
      this.signupForm.patchValue(user);  
      this.signupForm.patchValue({
        groupIds: user.groups.map((group: any) => group.id) // Typage explicite
      });
      this.openModal();
    } else if(event.action=== 'delete'){
      this.deleteModal.show(() => {
        this.coachService.delete(event.id).subscribe({
          next:(data)=>{
            this.findAllCoach();
            this.deleteModal.hide();
            this.toastService.success("Le coach a été supprimé avec succès", "Suppression d'un coach", {
              timeOut: 3000
          });
          }
        })
      })
    }
    else {
      this.updateStatus(event.id , event.action);
      this.findAllCoach();   
    }
  }
  findAllCoach(){
    this.coachService.findPage(this.pageNumber , this.pageSize ).subscribe({ 
      next:(users) => {
        this.users=users.content; 
        this.userPage=users;
      }
      }
    )
  }
  onFilterChange(event: { key: string, searchValue: string }) {
   
  }  
  saveUser() {
    const userData =this.signupForm.value ; 
    console.log(userData);
     if (this.isUpdate) {
         this.coachService.update(this.coachsId , userData ).subscribe({
           next:(data) => {
             this.isUpdate=false
             this.modalComponent.onClose();
             this.findAllCoach();
             this.toastService.success("Modification du coach avec succès", "Modification d'un coach", {
              timeOut: 3000
          });
           }
         })    
     } else { 
         this.coachService.create(userData).subscribe({
           next:(data) => {
             this.findAllCoach();
             this.modalComponent.onClose();
             this.toastService.success("Création du coach avec succès", "Ajout d'un coach", {
              timeOut: 3000
            });          
           } , 
           error :(error) => {
            console.log(error)
           }
          }
        )
     } 
 } 








 
  updateStatus( id: number, status: string ) {
    /*
    this.coachService.putStatusUsers(id, status, this.users[0])
      .subscribe((response) => {
        this.findAllCoach();
      });*/
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


