import { Component, EventEmitter, Input, Output, TemplateRef, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.scss'
})
export class ModalComponent {

  @ViewChild('extralarge') modal: any;

  @Input() title: string = '';
  @Input() formGroup!: FormGroup;  
  @Output() save = new EventEmitter<void>();
  @Output() close = new EventEmitter<void>();
  @Output() isUpdate = new EventEmitter<boolean>();
 
  constructor( private modalService: NgbModal) {}

  openModal(exlargeModal: any) {
    this.modalService.open(
                  exlargeModal, 
                  { size: 'lg',
                    centered: true , 
                    backdrop: 'static',  
                    keyboard: false 
                    
                    }  )  ;
   }

  onClose(): void {
    this.modalService.dismissAll();
    this.formGroup.reset();
    this.isUpdate.emit();
   }
  dismissAll(){
    this.modalService.dismissAll();
  }

  onSave(): void {
      this.save.emit();
  }
  
}
