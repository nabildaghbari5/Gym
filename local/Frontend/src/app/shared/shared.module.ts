import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

// component
import { BreadcrumbsComponent } from './breadcrumbs/breadcrumbs.component';
import { ReusableTableComponent } from './reusable-table/reusable-table.component';
import { ModalComponent } from './modal/modal.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { NestedDataPipe } from './service/nestedDataPipe';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { PaginationComponent } from './pagination/pagination.component';

@NgModule({
  declarations: [
    BreadcrumbsComponent,
    ReusableTableComponent ,
    ModalComponent ,
    NestedDataPipe ,
    ConfirmDialogComponent ,
    PaginationComponent      

  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ModalModule.forRoot() ,
    BsDropdownModule.forRoot(),

  ],
  exports:  [
    BreadcrumbsComponent , 
    ReusableTableComponent , 
    ModalComponent , 
    NestedDataPipe , 
    ConfirmDialogComponent , 
    PaginationComponent
  ]  
})
export class SharedModule { }
