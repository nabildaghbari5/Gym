import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UsersRoutingModule } from './users-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { UserInterneComponent } from './components/user-interne/user-interne.component';
import { UserExterneComponent } from './components/user-externe/user-externe.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { ModalModule } from 'ngx-bootstrap/modal';
import { NgSelectModule } from '@ng-select/ng-select';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { RoleComponent } from './components/role/role.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { GroupsComponent } from './components/groups/groups.component';
import { RoleUserComponent } from './components/role-user/role-user.component';
import { GroupsUserComponent } from './components/groups-user/groups-user.component';
import { DetailAdherentComponent } from './components/detail-adherent/detail-adherent.component';
import { AbonnementExpiresComponent } from './components/abonnement-expires/abonnement-expires.component';
import { CoachComponent } from './components/coach/coach.component';


@NgModule({
  declarations: [
    UserInterneComponent ,
    UserExterneComponent  ,
    RoleComponent ,
    GroupsComponent,
    RoleUserComponent,
    GroupsUserComponent,
    DetailAdherentComponent,
    AbonnementExpiresComponent,
    CoachComponent 
  ],
  imports: [
    CommonModule,
    UsersRoutingModule ,
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    PaginationModule.forRoot(),
    ModalModule.forRoot(), 
    NgSelectModule,
    BsDropdownModule.forRoot(),
    NgbModule,           
    
    


  ]
})
export class UsersModule { }
