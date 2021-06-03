import { Component, OnInit } from '@angular/core';
import { NgxDateRangePickerOptions } from 'ngx-daterangepicker';
import { Router } from '@angular/router';
import { AuthManagerTmService } from '../../authTM/auth-manager-tm.service';
import { LoaderServiceService } from '../../Loader/loader-service.service';
import { CommonMethod } from '../../CommonCodeVal/CommonMethod';


@Component({
  selector: 'app-bulk-action',
  templateUrl: './bulk-action.component.html',
  styleUrls: ['./bulk-action.component.css']
})
export class BulkActionComponent implements OnInit {

  options: NgxDateRangePickerOptions;
  selectedDate: any;
  bulkEmployees: any = [];
  loggedMail: string = '';
  loggedRole: string = '';
  startDate: string = '';
  endDate: string = '';
  todaysDate: Date;
  fromDate: Date;
  dataResponse: any = [];

  constructor(
    private manager: AuthManagerTmService,
    private loaderService: LoaderServiceService,
    private router: Router) {

    if (!CommonMethod.authenticateSenior()) {
      this.router.navigate(['/login']);
    }
  }

  ngOnInit() {
    this.loggedMail = localStorage.getItem('tm@Ics#user');
    this.loggedRole = localStorage.getItem('tmRole');
    this.getCurrentDate();
    this.initiateDatePicker();

    this.callServiceForSelectedWeek();
  }

  // --- CURRENT DATE
  getCurrentDate() {
    this.todaysDate = new Date();
    this.fromDate = new Date(this.todaysDate.getFullYear(), this.todaysDate.getMonth() - 1, 1);
    console.log(this.todaysDate);
    console.log(this.fromDate);
  }


  // --- INITIALIZE DATE PICKER FUNCTION  
  initiateDatePicker() {
    this.options = {
      theme: 'green',
      labels: ['Start', 'End'],
      menu: [],
      dateFormat: 'MM-DD-YYYY',
      outputFormat: 'MM-DD-YYYY',
      startOfWeek: 0,
      // locale: 'en',
      date: {
        from: {
          year: this.fromDate.getFullYear(),
          month: this.fromDate.getMonth() ,
          day: this.fromDate.getDate()
        },
        to: {
          year: this.todaysDate.getFullYear(),
          month: this.todaysDate.getMonth() + 1,
          day: this.todaysDate.getDate()
        }
      }
    }
  }

  // --- Updating Date Change
  dateChange(event: any) {
    let dateRange = event.split('-');
    this.startDate = `${dateRange[0]}/${dateRange[1]}/${dateRange[2]}`;
    this.endDate = `${dateRange[3]}/${dateRange[4]}/${dateRange[5]}`;
    console.log(this.startDate);
    console.log(this.endDate);
  }

  // --- GET SUBMITTED DETAILS ON DEFAULT LODE
  callServiceForSelectedWeek() {
    let end = `${this.todaysDate.getMonth() + 1}/${this.todaysDate.getDate()}/${this.todaysDate.getFullYear()}`;
    let start = `${this.fromDate.getMonth()}/${this.fromDate.getDate()}/${this.fromDate.getFullYear()}`;
    this.manager.getDetailforBulkOnStartAndEndDate(this.loggedMail, this.loggedRole, start, end)
      .subscribe((val) => {
        this.loaderService.requestEnded();
        console.log(val);
        this.bulkEmployees = val;
        console.log(this.bulkEmployees);
      });
    console.log(this.bulkEmployees.length);
  }

  // --- SEARCHING SUBMITTED DETAILS WITH DATE RAGE
  searchBulkEmployees() {
    this.manager.getDetailforBulkOnStartAndEndDate(this.loggedMail, this.loggedRole, this.startDate, this.endDate)
      .subscribe((val) => {
        this.loaderService.requestEnded();
        console.log("bulk employee", val);
        this.bulkEmployees = val;
      });
  }

  // onBulkCheckBoxChange(event:any, email:string, weeks:any){
  //   console.log(weeks);
  //   if(event.target.checked){
  //    for(let week of weeks){
  //      this.dataResponse.push({email:email, startDate:week.startDate, endDate:week.endDate, weekNo:week.weekNo, approverRole:this.loggedRole});
  //    }
  //   }else{

  //   }
  //   console.log(this.dataResponse);
  // }

  // --- CHECK BOX SELECTION FOR BULK ACTION
  onCheckboxChange(event: any, email: string, startDate: string, endDate: string, weekNo: number) {
    if (event.target.checked) {
      this.dataResponse.push({ email: email, startDate: startDate, endDate: endDate, weekNo: weekNo, approverRole: this.loggedRole });
    }
    else {
      let index = this.dataResponse.findIndex((v) =>
        (v.email === email) && (v.startDate === startDate));
      console.log(index);
      this.dataResponse.splice(index, 1);
    }
    console.log(this.dataResponse);
  }

  // --- service action for approve or reject
  actionOnBulkApproveOrRejection(action: string) {
    this.manager.actionOnBulkApproveAndReject(action, this.dataResponse)
      .subscribe(val => {
        this.loaderService.requestEnded();
        alert(val);
        this.callServiceForSelectedWeek();
      });
    this.dataResponse = [];
  }

}
