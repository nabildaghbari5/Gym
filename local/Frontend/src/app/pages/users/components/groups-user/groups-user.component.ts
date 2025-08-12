import { Component, EventEmitter, ViewChild } from '@angular/core';
import { GroupsService } from '../../service/groups.service';
import { ActivatedRoute } from '@angular/router';
import { Column } from 'src/app/shared/service/column';
import { Action } from 'src/app/shared/service/action';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';
import { UserService } from '../../service/user.service';
import { initPage, Page } from 'src/app/shared/models/page';
import { CoachService } from '../../service/coach.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-groups-user',
  templateUrl: './groups-user.component.html',
  styleUrl: './groups-user.component.scss'
})
export class GroupsUserComponent {
  @ViewChild("deleteModal") deleteModal!: ConfirmDialogComponent;
  breadCrumbItems!: Array<{}>;
  listUser:any[]=[];
  columns: Column[] = [];
  actions: Action[] = [];
  groupsId!:number;
  pageNumber = 0
  pageSize =10
  groupUserPage: Page<any> = initPage;
  onPaginationChange: EventEmitter<string> = new EventEmitter<string>();
  constructor(
       private router:ActivatedRoute,
       private coachService:CoachService,
        public toastService: ToastrService,
    ){} 
    ngOnInit(){
      this.router.params.subscribe(params => {
        this.groupsId = params['id']
      })
      this.initializeTable();
      this.breadCrumbItems = [ 
        { label: 'groups', active: true },
        { label: 'liste-users', active: true }, 
      ];
      this.findCoachsByGroups();
      this.onPaginationChange.subscribe(()=>this.findCoachsByGroups());
    }
    initializeTable(): void {
      this.columns = [
        { key: 'id', label: 'ID' },
        { key: 'lastname', label: 'Nom', sortable: false },
        { key: 'firstname', label: 'Prénom', sortable: false },
        { key: 'telephone', label: 'Téléphone', sortable: false },
      ];
      this.actions = [
        { type: 'delete', buttonClass:"btn btn-subtle-danger btn-icon btn-sm",  iconClass: 'bx bxs-trash-alt', visible:true }
  
      ];
    }
  onAction(event: { id: number, action: string }): void {
    const user = this.listUser.find(u => u.id === event.id);
    if (event.action === 'delete') {
      console.log('delete');
      this.deleteModal.show(() => {
        this.coachService.removeGroupFromUser(user.id , this.groupsId).subscribe({
          next: () => {
            this.toastService.success("Suppresion du coach avec succès", "Suppresion d'un coach" , {
              timeOut: 3000
          });  
            this.findCoachsByGroups();
           this.deleteModal.hide();
          },
          error: error => {
          }
        })
      })
    }
  }
  findCoachsByGroups(){
    if(this.groupsId){
      this.coachService.findCoachsPage(this.groupsId , this.pageNumber , this.pageSize).subscribe({
        next:(data) => {
          this.listUser=data.content;
          this.groupUserPage=data;
        }
      })
    }
  }
  onFilterChange(event: { key: string, searchValue: string }) {
    console.log('Recherche dans la colonne : ', event.key, ' avec la valeur : ', event.searchValue);
   /**
    * this.groupsService.searchInTableGroups(event.key, event.searchValue , this.groupsId ,this.pageNumber , this.pageSize  ).subscribe({
      next:(data)=> {
          this.listUser=data.content;
          this.groupUserPage=data;
          console.log(this.listUser) 
      }
    }    
    );
    *  */ 
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
