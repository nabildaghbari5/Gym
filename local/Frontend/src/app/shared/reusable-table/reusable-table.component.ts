import { Component, EventEmitter, Input, Output } from '@angular/core';
import { List } from 'echarts';

@Component({
  selector: 'app-reusable-table',
  templateUrl: './reusable-table.component.html',
  styleUrl: './reusable-table.component.scss'
})
export class ReusableTableComponent {
  @Input() data: any[] = [];
  @Input() columns: { key: string, label: string, sortable?: boolean }[] = [];
  @Input() title: string = '';
  @Input() showAddButton: boolean = false;
  @Input() addButtonLabel: string = '';
  @Input() actions: { type: string; buttonClass: string; iconClass: string; visible: boolean }[] = [];
  @Input() statusOptions: string[] = [];
  @Output() add = new EventEmitter<void>();
  @Output() search = new EventEmitter<{key:string , searchValue:string}>();
  @Output() actionClicked = new EventEmitter<{ id: number, action: string }>();
  @Output() checkedList = new EventEmitter<any[]>; 
  @Output() deleteAll = new EventEmitter<void>();

  masterSelected: boolean = false;  // Master checkbox state
  checkedVal: any[] = [];

  isSearching: { [key: string]: boolean } = {};
  searchTerms: { [key: string]: string } = {};

   // Fonction pour déterminer les classes CSS selon le statut
   getStatusClass(status: string | boolean): string {
    if (status === 'En attente') {
      return 'bg-info-subtle text-warning';
    } else if (status === 'Inactif' || status === false) {
      return 'bg-danger-subtle text-danger';
    } else if (status === 'Actif' || status === true) {
      return 'bg-success-subtle text-success';
    }
    return '';
  }

  // Fonction pour afficher le libellé correct selon le statut
  getStatusLabel(status: string | boolean): string {
    if (status === true) {
      return 'Actif';
    } else if (status === false) {
      return 'Inactif';
    }
    return status.toString();
  }
  
  toggleSearch(key: string): void {
    // Fermer tous les autres inputs de recherche
    for (const searchKey in this.isSearching) {
      if (searchKey !== key) {
        this.isSearching[searchKey] = false;
        this.searchTerms[searchKey] = ''; // Optionnel, si vous souhaitez également vider les termes de recherche des autres colonnes
      }
    }
    // Basculer l'état de l'input de la colonne sélectionnée
    this.isSearching[key] = !this.isSearching[key];
    // Si l'input est fermé, vider le terme de recherche
    if (!this.isSearching[key]) {
      this.searchTerms[key] = '';
      this.onSearch(key);
    }
  }

  onSearch(key: string): void {
    this.search.emit({ key, searchValue: this.searchTerms[key] });
    console.log('Recherche dans la colonne', key, ':', this.searchTerms[key]); // Assurez-vous que ce log s'affiche
  }
  onAdd(): void {
    this.add.emit();     
  }  

  onActionClick(itemId: number, action: string): void {
    console.log(`Action: ${action}, ID: ${itemId}`);
    this.actionClicked.emit({ id: itemId, action });   
  }

 // Master checkbox change event
 checkUncheckAll(event: any): void {
  this.masterSelected = event.target.checked;
  this.checkedVal = []; // Clear checked values
  this.data.forEach(item => {
    item.selected = this.masterSelected;
    if (item.selected) {
      this.checkedVal.push(item); // Add selected item to checkedVal
    }
  });
  this.checkedList.emit(this.checkedVal);
}

// Individual checkbox change event
checkIfAllSelected(): void {
  this.masterSelected = this.data.every(item => item.selected);
  this.checkedVal = this.data.filter(item => item.selected); 
  this.checkedList.emit(this.checkedVal);
}
 
onClickDeleteAll(){
  this.deleteAll.emit();
}

}
