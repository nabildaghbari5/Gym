import { UserService } from './../../service/user.service';
import { Component, EventEmitter, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Action } from 'src/app/shared/service/action';
import { Column } from 'src/app/shared/service/column';
import { RoleService } from '../../service/role.service';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';
import { initPage, Page } from 'src/app/shared/models/page';

@Component({
  selector: 'app-role-user',
  templateUrl: './role-user.component.html',
  styleUrl: './role-user.component.scss'
})
export class RoleUserComponent {
  @ViewChild("deleteModal") deleteModal!: ConfirmDialogComponent;
  breadCrumbItems!: Array<{}>;
  listUser:any[]=[];
  columns: Column[] = [];
  actions: Action[] = [];
  roleId!:number;
  pageNumber = 0
  pageSize = 10
  roleUserPage: Page<any> = initPage
  onPaginationChange: EventEmitter<string> = new EventEmitter<string>();

  constructor(
       private router:ActivatedRoute,
       private roleService:RoleService , 
       private userService:UserService
    ){} 
ngOnInit(){
      this.router.params.subscribe(params => {
        this.roleId = params['id']
      })
      this.initializeTable();
      this.breadCrumbItems = [
        { label: 'Role', active: true },
        { label: 'liste-users', active: true },
      ];
      this.findUserByRole();
      this.onPaginationChange.subscribe(()=>this.findUserByRole()); 
    }
initializeTable(): void {
      this.columns = [
        { key: 'id', label: 'ID' },
        { key: 'lastname', label: 'Nom', sortable: true },
        { key: 'firstname', label: 'Prénom', sortable: true },
        { key: 'email', label: 'Email', sortable: true },
        { key: 'groups', label: 'Groupe', sortable: true },
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
        this.userService.removeRoleFromUser(user.id , this.roleId ).subscribe({
          next: () => {
             this.findUserByRole();
            this.deleteModal.hide()
          },
          error: error => {
           // this.deleteModal.hide()
          }
        })
      })
    }
  }
findUserByRole(){
    if(this.roleId){
      this.roleService.findUserPage(this.roleId , this.pageNumber , this.pageSize).subscribe({
        next:(data) => {
          this.listUser=data.content;
          this.roleUserPage=data;
        }
      })
    }
  }
  onFilterChange(event: { key: string, searchValue: string }) {
    console.log('Recherche dans la colonne : ', event.key, ' avec la valeur : ', event.searchValue);
    this.roleService.searchInTableRole(event.key, event.searchValue , this.roleId ,this.pageNumber , this.pageSize  ).subscribe({
      next:(data)=> {
        this.listUser=data.content;
          this.roleUserPage=data;
          console.log(this.listUser) 
      }
    }    
    );
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
