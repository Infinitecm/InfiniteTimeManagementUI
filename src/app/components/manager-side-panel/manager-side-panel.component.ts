import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-manager-side-panel',
  templateUrl: './manager-side-panel.component.html',
  styleUrls: ['../side-panel.css']
})
export class ManagerSidePanelComponent implements OnInit {

  todayDate: any;
  selectedTo:string= '';
  loggedRole:string='';
  date:any; month:any;
  constructor(private router: Router) {
     let urlArray = router.url.split('/');
     this.selectedTo= urlArray[2];
   }

  ngOnInit() {
    this.todayDate = new Date();
    this.loggedRole= localStorage.getItem('tmRole');
    console.log("--- "+this.loggedRole);
    this.month =this.changeTwoDigitDate((this.todayDate.getMonth()+1).toString());
    this.date =this.changeTwoDigitDate((this.todayDate.getDate()).toString());
  }

  actionOnBulkOperation(url:string){
    this.selectedTo= url;
    this.router.navigate([`TM/${url}`])
  }

  changeTwoDigitDate(data: string): string {
    if (data.length === 1) {
      return 0 + data;
    }
    return data;
  }
}
