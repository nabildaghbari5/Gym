import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms'; // Import FormsModule and NgForm
import { BsModalService, BsModalRef, ModalModule } from 'ngx-bootstrap/modal';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core'; // Import schemas

// Define an interface for Task
interface Task {
  id: string;
  clientName: string;
  company: string;
  process: string;
  code: string;
  region: string;
  subregion: string;
  progress: number;
  status: string;
}

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.scss']
})
export class TasksComponent {
  tasks: Task[] = [
    {
      id: '1',
      clientName: 'Salah Salah',
      company: 'Company AZ',
      process: 'Audit',
      code: 'BR',
      region: 'Tunis',
      subregion: 'La Marsa',
      progress: 65,
      status: 'In Progress'
    },
    {
      id: '2',
      clientName: 'Samira Samira',
      company: 'Company BZ',
      process: 'Satisfaction Survey',
      code: 'BY',
      region: 'Tunis',
      subregion: 'Lac 2',
      progress: 83,
      status: 'In Progress'
    },
    {
      id: '3',
      clientName: 'Mohsen Mohsen',
      company: 'Company CZ',
      process: 'Audit',
      code: 'PH',
      region: 'Mahdia',
      subregion: 'Mahdia Ville',
      progress: 47,
      status: 'In Progress'
    },
    {
      id: '4',
      clientName: 'Saida Saida',
      company: 'Company DZ',
      process: 'Audit',
      code: 'AR',
      region: 'Monastir',
      subregion: 'Skanes',
      progress: 71,
      status: 'In Progress'
    }
  ];

  breadCrumbItems = [
    { label: 'Home', link: '/' },
    { label: 'Tasks', link: '/tasks' }
  ];

  modalRef?: BsModalRef;

  constructor(private modalService: BsModalService) {}

  trackById(index: number, item: Task): string {
    return item.id;
  }

  getProgressClass(progress: number): string {
    if (progress >= 80) return 'bg-success-subtle text-success';
    if (progress >= 50) return 'bg-warning-subtle text-warning';
    return 'bg-danger-subtle text-danger';
  }

  showModal(template: any) {
    this.modalRef = this.modalService.show(template, { class: 'modal-lg' });
  }

  hideModal() {
    if (this.modalRef) {
      this.modalRef.hide();
    }
  }

  addTask(form: NgForm) {
    if (form.valid) {
      const newTask: Task = {
        id: (this.tasks.length + 1).toString(),
        clientName: form.value.clientName,
        company: form.value.company,
        process: form.value.process,
        code: form.value.code,
        region: form.value.region,
        subregion: form.value.subregion,
        progress: form.value.progress,
        status: form.value.status
      };

      this.tasks.push(newTask);
      this.hideModal();
    }
  }
}

@NgModule({
  declarations: [
    TasksComponent,
  ],
  imports: [
    CommonModule,
    FormsModule, // Add FormsModule to imports
    ModalModule.forRoot(),
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA], // Add schemas to allow custom elements and unknown properties
  exports: [TasksComponent]
})
export class TasksModule {}
