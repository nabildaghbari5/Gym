import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdherentService } from '../../service/adherent.service';

@Component({
  selector: 'app-detail-adherent',
  templateUrl: './detail-adherent.component.html',
  styleUrl: './detail-adherent.component.scss'
})
export class DetailAdherentComponent implements OnInit {
  adherentId:number ;
  adherentDetails:any
   constructor(
     private activatedRoute:ActivatedRoute,
     private adherentService:AdherentService
   ){

   }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.adherentId =+params['id'];
      this.findById(this.adherentId)
    })
  }

  findById(id:number){
   if(this.adherentId){
    this.adherentService.findById(id).subscribe({
      next:(data)=> {
        this.adherentDetails=data ;
      }
    })
   }
  }
}
