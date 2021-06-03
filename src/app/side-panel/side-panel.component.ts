import { Component, OnInit } from '@angular/core';
import { Router } from  '@angular/router';

@Component({
  selector: 'app-side-panel',
  templateUrl: './side-panel.component.html',
  styleUrls: ['./side-panel.component.css']
})
export class SidePanelComponent implements OnInit {
  
  todayDate: any;
  constructor(public router:Router) { }

  ngOnInit() {
    this.todayDate = new Date();
    console.log(this.todayDate.getDate());
  }

  actionOnBulkOperation(){
    console.log('Clicked');
    this.router.navigate(['managerTimesheet/BulkAction']);
  }

}
