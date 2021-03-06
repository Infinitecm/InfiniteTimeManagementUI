import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-user-side-panel',
  templateUrl: './user-side-panel.component.html',
  styleUrls: ['../side-panel.css']
})
export class UserSidePanelComponent implements OnInit {
  
  weekSubscription: Subscription;
  weekStatus:boolean =true;
  todayDate: any;
  date:any; month:any;

  constructor(
    public router:Router
    ) {}

  ngOnInit() {
    this.todayDate = new Date();
    this.month =this.changeTwoDigitDate((this.todayDate.getMonth()+1).toString());
    this.date =this.changeTwoDigitDate((this.todayDate.getDate()).toString());
  }
  changeTwoDigitDate(data: string): string {
    if (data.length === 1) {
      return 0 + data;
    }
    return data;
  }

}
