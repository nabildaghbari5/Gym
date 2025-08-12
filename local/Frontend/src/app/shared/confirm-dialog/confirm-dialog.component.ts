import { Component, HostListener } from '@angular/core';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.scss'
})
export class ConfirmDialogComponent  {
  message: any;
  constructor() { }

  ngOnInit(): any {
  }

  show(confirm: any, cancel?: any) { 
    this.message = {
      type: 'confirm', 
      confirm, 
      cancel: () => {
        this.message = null
        if (cancel) {
          cancel()
        }
      }
    }
  }

  hide() { 
    if (this.message.cancel) { 
      this.message.cancel()
    }
    this.message = null
  }

  @HostListener('document:keydown.escape', ['$event']) onKeydownHandler(evt: KeyboardEvent) {
    this.hide()
  }
}
