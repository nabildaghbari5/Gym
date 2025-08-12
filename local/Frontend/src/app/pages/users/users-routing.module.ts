import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserInterneComponent } from './components/user-interne/user-interne.component';
import { UserExterneComponent } from './components/user-externe/user-externe.component';
import { RoleComponent } from './components/role/role.component';
import { TasksComponent } from './components/tasks/tasks.component';
import { GroupsComponent } from './components/groups/groups.component';
import { RoleUserComponent } from './components/role-user/role-user.component';
import { GroupsUserComponent } from './components/groups-user/groups-user.component';
import { DetailAdherentComponent } from './components/detail-adherent/detail-adherent.component';
import { AbonnementExpiresComponent } from './components/abonnement-expires/abonnement-expires.component';
import { CoachComponent } from './components/coach/coach.component';

const routes: Routes = [

  {
    path:'user-interne',
    component:UserInterneComponent
  }, 
  {
    path:'coachs',
    component:CoachComponent
  }, 
  {
    path:'user-externe',
    component:UserExterneComponent
  },
  {
    path:'groups',
    component:GroupsComponent 
  },
  {
    path:'detail-adherent/:id', 
    component:DetailAdherentComponent
  },
  {
    path:'role_user/:id', 
    component:RoleUserComponent
  },
  {
    path:'groups_user/:id', 
    component:GroupsUserComponent
  },
  {
    path:'abonnement_expirés',   
    component:AbonnementExpiresComponent
  },  
 
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule { }
