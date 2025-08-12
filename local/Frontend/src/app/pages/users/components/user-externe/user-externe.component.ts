import { forkJoin, Subscription } from 'rxjs';
import { Component, EventEmitter, ViewChild } from '@angular/core';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';
import { UserService } from '../../service/user.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Column } from 'src/app/shared/service/column';
import { Action } from 'src/app/shared/service/action';
import { initPage, Page } from 'src/app/shared/models/page';
import { Validators } from 'ngx-editor';
import { FormBuilder, UntypedFormGroup } from '@angular/forms';
import { ModalComponent } from 'src/app/shared/modal/modal.component';
import { GroupsService } from '../../service/groups.service';
import { AdherentService } from '../../service/adherent.service';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-user-externe',
  templateUrl: './user-externe.component.html',
  styleUrls: ['./user-externe.component.scss']
})
export class UserExterneComponent {
  signupForm!: UntypedFormGroup;
  breadCrumbItems!: Array<{}>;
  adherents: any[] = []; 
  statusOptions:string[] = [];
  columns: Column[] = [];
  actions: Action[] = [];
  montantAPayer:number
  pageNumber = 0
  pageSize =10
  filter = '';
  isUpdate:boolean
  groups:any[]=[];
  adherentsPage: Page<any> = initPage
  onPaginationChange: EventEmitter<string> = new EventEmitter<string>();
  @ViewChild(ModalComponent) modalComponent!: ModalComponent;
  @ViewChild("deleteModal") deleteModal!: ConfirmDialogComponent;
  idAdherent: number;
  
  openModal():void {
    this.modalComponent.openModal(this.modalComponent.modal);
  }
  selectedStatus: any = 'Sélectionner status';
  StatusName = [  
    { name: 'Actif' },
    { name: 'Inactif' },
  ];  
  typeAbonnement: any = 'Sélectionner abonnement';
  AbonnementName = [   
    { name: 'MENSUEL' },
    { name: 'ANNUEL' },
    { name: 'JOURNALIER' },
  ];  

  constructor( 
    private userService: UserService,
    private formBuilder: FormBuilder,
    private groupsService:GroupsService,
    private adherentService:AdherentService ,
    private router:Router,
    public toastService: ToastrService ,
    
  ) { }
  ngOnInit(): void {
    this.initializeTable();
    this.loadData();
    this.findAllAdherent();
    this.onPaginationChange.subscribe(()=>this.findAllAdherent()); 
    this.breadCrumbItems = [
      { label: 'Users', active: true },
      { label: 'user-externe', active: true }
    ];

    this.signupForm = this.formBuilder.group({
      id: [''], 
      lastname: ['', Validators.required], 
      firstname: ['', Validators.required], 
      dateDeNaissance: ['', Validators.required], 
      telephone: ['', Validators.required], 
      abonnement:['', Validators.required],
      activite:['', Validators.required],
      months: ['', Validators.required], 
      price: ['', Validators.required],
      montantAPayer:[null, Validators.required],
      
    });
    // Écouter les changements sur les champs price et months
    this.signupForm.get('price')?.valueChanges.subscribe(() => {
      this.updateMontantAPayer();
    });
    this.signupForm.get('months')?.valueChanges.subscribe(() => {
      this.updateMontantAPayer();
    });
    
  }
  initializeTable(): void {
    this.columns = [
      { key: 'id', label: 'ID' },
      { key: 'lastname', label: 'Nom', sortable: true },
      { key: 'firstname', label: 'Prénom', sortable: true },
      { key: "telephone", label: "Télephone", sortable: true },
      { key: "dateInscription", label: "Date d'inscription", sortable: false },
      { key: "dateExpiration", label: "Date d'expiration", sortable: false },
      { key: 'status', label: 'Status', sortable: false } 
    ];
    this.actions = [
      { type: 'edit', buttonClass:"btn btn-subtle-success btn-icon btn-sm edit-item-btn",  iconClass: 'ph-pencil', visible:true },
      { type: 'detail', buttonClass:"btn btn-subtle-primary btn-icon btn-sm",  iconClass: ' bx bxs-user-detail', visible:true },
      { type: 'delete', buttonClass:"btn btn-subtle-danger btn-icon btn-sm",  iconClass: 'bx bxs-trash-alt', visible:true },

    ];
    // Initialisation des statuts
   this.statusOptions = ['Actif', 'Inactif'];
  }

  onAction(event: { id: number, action: string }): void {
    const adherent = this.adherents.find(a => a.id === event.id);
    if (event.action === 'delete') {
      console.log('delete');
      this.deleteModal.show(() => {
        this.adherentService.delete(event.id).subscribe({
          next:(data)=>{
            this.findAllAdherent();
            this.deleteModal.hide();
          }
        })
      })
    }
    else if (event.action === 'edit') {
      const adherent = this.adherents.find(a => a.id === event.id);
      this.isUpdate=true
      this.idAdherent=adherent.id;
      this.signupForm.patchValue(adherent);  
      this.signupForm.patchValue({
        activite: adherent.activite.map((activite: any) => activite.name), // Typage explicite
      });
      this.openModal();
    }
    else if(event.action === 'detail'){
      this.router.navigate(["users/detail-adherent" , event.id]) ;  
    }
  }

 onFilterChange(event: { key: string, searchValue: string }) {
    console.log('Recherche dans la colonne : ', event.key, ' avec la valeur : ', event.searchValue);
    this.adherentService.searchInTableAdherent(event.key, event.searchValue , this.pageNumber , this.pageSize).subscribe({
      next:(data)=>{
       this.adherents=data.content;
       this.adherentsPage=data;
      },  
      error:(err)=>{
           console.log('error filtrage user by type user ');
      }
    }    
     
    );
  }  

 // Méthode pour mettre à jour le montantAPayer
 updateMontantAPayer() {
  const price = this.signupForm.get('price')?.value;
  const months = this.signupForm.get('months')?.value;

  if (price && months) {
    const montant = price * months;
    this.signupForm.get('montantAPayer')?.setValue(montant);
  }
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

 findAllAdherent(){
  this.adherentService.findUsersByStatus(this.pageNumber , this.pageSize ,"Actif" ).subscribe({ 
    next:(adherents) => {
      this.adherents=adherents.content; 
      this.adherentsPage=adherents;
    }
    }
  )
}
  
 OnSave(){
    const formData = this.signupForm.value ;
    
    
    if(this.isUpdate){
      this.adherentService.patch(this.idAdherent,formData).subscribe({
        next:(data)=> { 
          this.findAllAdherent();
          this.modalComponent.onClose();
          this.isUpdate=false;
        }
      })
    }else{
      this.adherentService.create(formData).subscribe({
        next:(data)=> { 
          this.findAllAdherent();
          this.modalComponent.onClose();        
        }
      })
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


}
