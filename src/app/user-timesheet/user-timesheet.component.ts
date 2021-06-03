import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Form, FormArray } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthManagerTmService } from '../authTM/auth-manager-tm.service';
import { LoaderServiceService } from '../Loader/loader-service.service';
import * as jsPDF from 'jspdf';
import html2pdf from 'html2pdf.js';
import 'jspdf-autotable';
import { AlertService } from "../components/alert/alert.service";


@Component({
  selector: 'app-user-timesheet',
  templateUrl: './user-timesheet.component.html',
  styleUrls: ['./user-timesheet.component.css']
})
export class UserTimesheetComponent implements OnInit {

  weekForm: FormGroup;
  daysForm: FormArray;
  day = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'project'];
  dynamicFormField: any = {};
  weekList: any = [];
  weekSlotSelected: number;

  routeMail: string = '';
  routerRole: string = '';
  userName: string = '';
  userMail: string = '';
  userRole: string = '';
  userVendor: string = '';
  userAccounts: string = '';
  leadMail: string = '';
  managerMail: string = '';
  loggedRole: string = '';
  loggedMail: string = '';

  isApproved: boolean = false;
  isRejected: boolean = false;
  isRevoked: boolean = false;
  isSubmitted: boolean = false;
  disableSubmit: boolean;
  submittedByTag: boolean;
  submittedByName: string = '';

  projects: any = [];
  responseData: any = {};
  responseInDays: any = {};

  monDate: string = '';
  tueDate: string = '';
  wedDate: string = '';
  thuDate: string = '';
  friDate: string = '';

  monday: any = [];
  tuesday: any = [];
  wednesday: any = [];
  thursday: any = [];
  friday: any = [];

  mondayHrs: number = 0;
  tuesdayHrs: number = 0;
  wednesdayHrs: number = 0;
  thursdayHrs: number = 0;
  fridayHrs: number = 0;
  totalWeekHrs: number = 0;

  mondayHoliday = { 'is': false, 'title': '' };
  tuesdayHoliday = { 'is': false, 'title': '' };
  wednesdayHoliday = { 'is': false, 'title': '' };
  thursdayHoliday = { 'is': false, 'title': '' };
  fridayHoliday = { 'is': false, 'title': '' };

  leaveDOM: boolean = false;
  leaveDay: any = [];
  mondayLeave = false;
  tuesdayLeave = false;
  wednesdayLeave = false;
  thursdayLeave = false;
  fridayLeave = false;

  mondayEnd    = false;
  tuesdayEnd   = false;
  wednesdayEnd = false;
  thursdayEnd  = false;
  fridayEnd    = false;


  status: any = [{ id: 1, text: 'Not filled' },
  { id: 2, text: 'Draft' },
  { id: 3, text: 'Submitted' },
  { id: 4, text: 'Approved' },
  { id: 5, text: 'Rejected' },
  { id: 6, text: 'Revoked' }];

  holidayList: any = [];


  holidayTest: any = [
    { "date": "10/02/2020", "reason": "Gandhi Jayanti" },
    { "date": "11/26/2020", "reason": "Children Day" }
  ];

  @ViewChild("content") content: ElementRef;


  constructor(
    private fb: FormBuilder, private router: Router, private service: AuthManagerTmService,
    private loaderService: LoaderServiceService, private alertService: AlertService,
    private activatedRoute: ActivatedRoute) {

    this.activatedRoute.queryParams.subscribe(params => {
      this.routeMail = params['mail'];
      this.routerRole = params['role'];
      console.log(this.routeMail + " *** ROUTER *** " + this.routerRole);
    });

  }



  ngOnInit() {
    this.loggedRole = localStorage.getItem('tmRole');
    this.loggedMail = localStorage.getItem('tm@Ics#user');
    console.log(" Logged in Role: ", this.loggedRole);
    if (this.loggedRole != 'User') {
      this.userMail = this.routeMail;
      this.userRole = this.routerRole;
    } else {
      this.userMail = this.loggedMail;
      this.userRole = this.loggedRole;
    }

    this.getWeek_With_Start_End_Date();

    this.weekForm = this.fb.group({
      week: [],
      leave: [this.leaveDay],
      daysForm: this.fb.array([])
    })

    this.setDefaultsDropDown();
    this.weekForm.get('week').valueChanges
      .subscribe(weekSlot => {
        this.onWeekChanged(weekSlot)
      });
    console.log("weekform:", this.weekForm);
    const num = this.changeTwoDigitDate('2');
  }
  //  --- END OF ngOnINIT()

  //For Notification
  current = new Date();
  currentDate = this.current.getDate();
  month = new Date(this.current.getFullYear(), this.current.getMonth() + 1, 0);
    prevDate = this.month.getDate()-2;   //do this for last 3 days of all month
  alertmethod(a, b1) {
    if (this.loggedRole == "User") { //if notification only for user 
      for (let i = this.prevDate; i < this.prevDate + 3; ++i) {
        if (this.currentDate == i) {
          this.alertService.alert(a, b1);
        }
      }
    }
  }
  alertmethod2(a, b1) {   // notification for mananger and lead
    if (this.loggedRole == "Manager" || this.loggedRole == "Lead") {
      for (let i = this.prevDate; i < this.prevDate + 3; ++i) {
        if (this.currentDate == i) {
          this.alertService.alert2(a, b1);
        }
      }
    }
  }
  alertmethod3(a, b1) {   //notification when a higher authority logins and opens his/her team members timesheet.
    if (this.loggedRole != this.routerRole) {
      for (let i = this.prevDate; i < this.prevDate + 3; ++i) {
        if (this.currentDate == i) {
          this.alertService.alert3(a, b1);
        }
      }
    }
  }
  ngAfterViewInit() {
    setTimeout(() => {
      if (this.loggedRole == "User") {
        for (let i = this.prevDate; i < this.prevDate + 3; ++i) {
          if (this.currentDate == i) {
            this.alertmethod('success', 'Hi ');
          }
        }
      }
      if ((this.loggedRole == "Manager" || this.loggedRole == "Lead") && (this.loggedRole == this.routerRole)) {
        for (let i = this.prevDate; i < this.prevDate + 3; ++i) {
          if (this.currentDate == i) {
            this.alertmethod2('success', 'Hi ');
          }
        }
      }
      if ((this.loggedRole != this.routerRole) && (this.loggedRole != "User")) {
        for (let i = this.prevDate; i < this.prevDate + 3; ++i) {
          if (this.currentDate == i) {
            this.alertmethod3('success', 'Hi ');
          }
        }
      }
    }, 0);
  }

  // --- LEAVE RECORD CODE IMPLEMENTATION
  toggleLeaves() {
    this.leaveDOM = !this.leaveDOM;
  }
  applyLeave(event: any, day: string) {
    const date = day.substr(0, 3) + 'Date';
    if (event.target.checked) {
      this.leaveDay.push(this[date]);
      this.blockLeaveDay(day);
    } else {
      const index = this.leaveDay.findIndex(d => {
        return (d === this[date])
      });
      this.leaveDay.splice(index, 1);
      this.unblockLeaveDay(day);
    }
    console.log("Leave day: ",this.leaveDay);
  }

  unblockLeaveDay(day: string) {
    const leave = day + 'Leave';
    this[leave] = false;
  }
  blockLeaveDay(day: string) {
    let projectLength = this.projects.length;
    let setDay = day;
    let index = 1;
    while (index <= projectLength) {
      let keyTM = setDay + index;
      this.weekForm.get('daysForm').patchValue([
        { [keyTM]: 0 }
      ]);
      ++index;
    }
    const leave = day + 'Leave';
    this[leave] = true;
    this.caluculateHours(day);
  }

  setDefaultLeave() {
    this.leaveDay = [];
    this.leaveDOM = false;
    this.mondayLeave = false; this.tuesdayLeave = false; this.wednesdayLeave = false;
    this.thursdayLeave = false; this.fridayLeave = false;
  }


  // --- CALCULATING WORKED HOURS ON PROJECT FOR A DAY
  caluculateHours(day: string) {
    let projectLength = this.projects.length;
    let setDay = day;
    let setColumn = setDay + 'Hrs';
    this[setColumn] = 0;
    let index = 1;
    while (index <= projectLength) {
      let keyTM = setDay + index;
      let tmValue = this.weekForm.get('daysForm').get('0').get(keyTM).value;
      this[setColumn] = this[setColumn] + tmValue;
      if (this[setColumn] > 8) {
        alert("Working Hours cannot be exceed by 8 hours!");
        this.weekForm.get('daysForm').patchValue([
          { [keyTM]: 0 }
        ]);
        this[setColumn] = this[setColumn] - tmValue;
      }
      ++index;
    }
    this.calculateHoursOnProjectForWeek();
    this.calculateHoursForEntireWeek();
  }


  // --- CALCULATING WORKED HOURS FOR A PROJECT IN ENTIRE WEEK
  calculateHoursOnProjectForWeek() {
    let projectLength = this.projects.length;
    let index = 1;
    while (index <= projectLength) {
      let row = "project" + index;
      let projectADD = 0;
      for (let i = 0; i < this.day.length - 1; ++i) {
        let setDay = this.day[i];
        let keyTM = setDay + index;
        let tmValue = this.weekForm.get('daysForm').get('0').get(keyTM).value;
        projectADD = projectADD + tmValue;
      }
      this.weekForm.get('daysForm').patchValue([
        { [row]: projectADD }
      ])
      ++index;
    }
  }


  // --- CALCULATING WORKED HOURS FOR THE ENTIRE WEEK
  calculateHoursForEntireWeek() {
    this.totalWeekHrs = this.mondayHrs + this.tuesdayHrs + this.wednesdayHrs + this.thursdayHrs + this.fridayHrs;
  }


  // --- CONFIGURATION FOR STARTDATE, ENDDATE, WEEKNO, LEAD, EMAIL FOR SERVICE
  configureWeekStartEndValueForService(startDate: string, endDate: string, weekNo: number) {
    this.setDefaultLeave();
    this.setSubmitButtonDisableEnable(endDate);
    this.setTheSelectedWeekStatus();
    this.getDateFromSelectedWeek(startDate, endDate);

    let response = {
      'email': this.userMail,
      'leadEmail': this.leadMail,
      'role': this.userRole,
      'weekNo': weekNo,
      'startDate': startDate,
      'endDate': endDate
    }
    if (weekNo > 52) {
      response['weekNo'] = weekNo - 52;
    }
    if (this.loggedRole != 'User') {
      if (this.routerRole === 'User') {
        response.leadEmail = this.loggedMail;
      }
      response.role = this.loggedRole;
    }

    this.service.getUserTimesheetDetails(response).subscribe(val => {
      this.loaderService.requestEnded();
      if (val.projects != null && val.userData != null) {

        this.userName = val.userData.firstName + ' ' + val.userData.lastName;
        this.userMail = val.userData.email;
        this.userRole = val.userData.role;
        this.userAccounts = val.userData.accounts.toString()
        this.submittedByName = val.userData.submittedBy;
        this.userVendor = val.userData.vendor;
        this.leadMail = val.userData.lead;
        this.managerMail = val.userData.manager;

        console.log("Proejcts:",val.projects);

        this.holidayList = val.holiday;
        this.settingToDefaultHoliday();
        if (this.holidayList.length > 0) {
          this.checkTheHolidayList(this.holidayList);
        }
        this.dynamicFormCreationAsPerProjects(val.projects);
      }

    });
    this.setWeekStatus();
  }

  // --- FEEDING THE HOLIDAY LIST
  checkTheHolidayList(holidayArr: Array<any>) {
    let totalDays = this.day.length - 1;
    for (let i = 0; i < totalDays; ++i) {
      let holiday = this.day[i] + 'Holiday';
      let day1 = this.day[i].substring(0, 3) + 'Date';
      let presentDate = this[day1];

      for (let j = 0; j < holidayArr.length; ++j) {
        if (presentDate === holidayArr[j].date) {
          this[holiday]['is'] = true;
          this[holiday]['title'] = holidayArr[j]['reason'];
        }
      }
    }
  }

  // --- SETTING BACK TO DEFAULT HOLIDAY ON WEEK SLOT CHANGE
  settingToDefaultHoliday() {
    for (let i = 0; i < this.day.length - 1; ++i) {
      let holiDay = this.day[i] + 'Holiday';
      this[holiDay].is = false;
      //this[holiDay].title = 'Fill Hours';
    }
  }

  //  --- SAVE OR SUBMIT THE TIMESHEET
  onSubmitTheTimesheet(status: number) {
    let ok = true;
    if (this.leaveDay.length > 0) {
      ok = confirm("Are You sure want to take leave on " + this.leaveDay.toString());
    }
    if (ok) {
      this.prepareLeaveList();
      console.log("valid or not", this.weekForm.valid);
      this.prepareEachTmDataForInput();
      this.responseData['email'] = this.userMail;
      this.responseData['status'] = status;
      this.responseData['weekNo'] = this.weekSlotSelected;
      this.responseData['submittedBy'] = this.userMail;
      this.responseData['submittedByRole'] = this.userRole;
      this.responseData['startDate'] = this.weekList[this.weekSlotSelected - 1].startDate;
      this.responseData['endDate'] = this.weekList[this.weekSlotSelected - 1].endDate;
      this.responseData['days'] = this.responseInDays;
      this.responseData['leave'] = this.leaveDay;
      if (this.weekSlotSelected > 52) {
        this.responseData['weekNo'] = this.weekSlotSelected - 52;
      }
      this.submittedByName = this.userName;
      if (localStorage.getItem('tmRole') != 'User') {
        this.responseData['submittedBy'] = this.loggedMail;
        this.responseData['submittedByRole'] = this.loggedRole;
        this.submittedByName = localStorage.getItem('tmUserName');
        console.log("User Name: ", this.userName);
      }

      console.log('response data:', this.responseData);

      this.service.saveOrSubmitTheTimesheet(this.responseData).subscribe(val => {
        this.loaderService.requestEnded();
        if (val.VALUE === true) {
          this.setWeekStatus();
          // this.setTheSelectedWeekStatus();
          this.configureWeekStartEndValueForService(this.weekList[this.weekSlotSelected - 1].startDate,
            this.weekList[this.weekSlotSelected - 1].endDate,
            this.weekList[this.weekSlotSelected - 1].weekNo);
          console.log("result: ", val.RESULT);
          alert(val.RESULT);
        }
      });
    }
  }

  prepareLeaveList() {
    console.log('holida list : ',this.holidayList);
    console.log(' leave day : ', this.leaveDay);
    this.holidayList.forEach((i, v) => {
      console.log(i['reason']);
      if (i['reason'] === 'Leave') {
        this.leaveDay.push(i['date'])
      }
    });
    console.log('leave day : ', this.leaveDay);
  }


  actionOnApproveOrReject(action: string) {
    let startDate = this.weekList[this.weekSlotSelected - 1].startDate;
    let endDate = this.weekList[this.weekSlotSelected - 1].endDate;
    let week_No = this.weekSlotSelected;
    if (week_No > 52) { week_No = week_No - 52; }
    this.service.serviceOnApproveOrReject(this.userMail, action, startDate, endDate, week_No)
      .subscribe((val) => {
        this.loaderService.requestEnded();
        this.setWeekStatus();
        this.configureWeekStartEndValueForService(this.weekList[this.weekSlotSelected - 1].startDate,
          this.weekList[this.weekSlotSelected - 1].endDate, this.weekList[this.weekSlotSelected - 1].weekNo);
          alert(val);
      });

  }

  // --- DOWNLOAD PDF
 /*  public downloadPdf() {
    let pdfName = this.userName + '_Timesheet.pdf'
    console.log("download triggred");
    let options = {
      filename: pdfName,
      html2canvas: {
        alignment: 'center',
        padding: 1,

        image: { type: 'webp', quality: 1 },
        overflow: 'linebreak',
        margins: { top: 80, bottom: 60, left: 40, width: 522 },
      },
      jsPDF: {
        orientation: 'l',
        unit: 'pt',
        format: 'letter'
      }
    };
    const specialElementHandlers = {
      "#editor": function (element, renderer) { return true; }
    };
    const nativeElement = document.getElementById('content');
    html2pdf().from(nativeElement).set(options, specialElementHandlers).save();

  } */

  public downloadPdf() {
    let pdfName = this.userName + '_Timesheet.pdf'
    console.log("download triggred");
    let options = {
      filename: pdfName,
      margin: [40, 30, 30, 40],
      image: { type: 'jpeg', quality: 0.98 },
      html2canvas: {
        margin: 1,
        scale: 2,
        dpi: 192, letterRendering: true,
      },
      jsPDF: {
        orientation: 'l',
        unit: 'pt',
        format: 'a4'
      }
    };
    const specialElementHandlers = {
      "#editor": function (element, renderer) { return true; }
    };
    const nativeElement = document.getElementById('content');
    // html2pdf().from(nativeElement).set(options, specialElementHandlers).save();   
    html2pdf().from(nativeElement).set(options, specialElementHandlers).toPdf().get('pdf').then(function (pdf) {
      var totalPages = pdf.internal.getNumberOfPages();
      for (let i = 1; i <= totalPages; i++) {
        pdf.setPage(i);
        pdf.setFontSize(10);
        pdf.setTextColor(150);
        pdf.text('Page ' + i + ' of ' + totalPages, pdf.internal.pageSize.getWidth() - 100, pdf.internal.pageSize.getHeight() - 30);       
      }
    }).save();
  }

  // --- ON WEEK CHANGED, UPDATING DROP DOWN
  onWeekChanged(weekSlot: number) {
    this.projects = [];
    this.weekSlotSelected = weekSlot;
    this.configureWeekStartEndValueForService(this.weekList[this.weekSlotSelected - 1].startDate,
      this.weekList[this.weekSlotSelected - 1].endDate,
      this.weekList[this.weekSlotSelected - 1].weekNo);
    //      this.weekList[this.weekSlotSelected - 1].weekNo);
  }
  // --- SET THE WEEK STATUS IN DROPDOWN  
  setWeekStatus() {
    this.service.getWeekStatus(this.userMail).subscribe(val => {
      this.loaderService.requestEnded();

      for (let i = 0; i < val.length; ++i) {
        let statusFromServer = val[i].status;
        this.status.forEach(ele => {
          if (ele.id === statusFromServer) {
            const weekIndex = this.weekList.findIndex(a => { return a.startDate === val[i].startDate });
            //this.weekList[val[i].weekNo - 1].status = ele.text;
            this.weekList[weekIndex].status = ele.text;


          }
        });
      };

      this.setDafaultBooleanStatus();
      this.setTheSelectedWeekStatus();
    });
  }


  // --- SUBMITTED BY TAG VISIBILITY getStatusForSubmitTagVisibility
  setTheSelectedWeekStatus() {
    if (this.weekList[this.weekSlotSelected - 1].status === 'Submitted') {
      this.isSubmitted = true;
    }
    else if (this.weekList[this.weekSlotSelected - 1].status === 'Approved') {
      this.isApproved = true;
    }
    else if (this.weekList[this.weekSlotSelected - 1].status === 'Rejected') {
      this.isRejected = true;
    }
  }
  setDafaultBooleanStatus() {
    this.isRejected = false;
    this.isSubmitted = false;
    this.isApproved = false;
  }

  // --- ENABLING DISABLING THE SUBMIT BUTTON AS THE DATE
  setSubmitButtonDisableEnable(endDate: string) {
    let todayDate = new Date();
    let lastDate = new Date(endDate);
    if (todayDate.getTime() < lastDate.getTime()) {
      this.disableSubmit = true;
    } else {
      this.disableSubmit = false;
    }
  }



  // --- PREPARING EACH DATA FOR SUBMIT/SAVE CLICK
  prepareEachTmDataForInput() {
    let projectLength = this.projects.length;
    for (let i = 0; i < this.day.length - 1; ++i) {

      let setDay = this.day[i];
      let dayDate = setDay.substring(0, 3) + 'Date';
      this[setDay] = [];
      let index = 1;
      while (index <= projectLength) {
        let projectName = this.projects[index - 1].project;
        let keyTM = setDay + index;

        let tmValue = this.weekForm.get('daysForm').get('0').get(keyTM).value;
        if (tmValue === 0 || tmValue === 0.0 || tmValue === null) {
          tmValue = 0.0;
        }
        this[setDay].push({ 'projectName': projectName, 'hours': tmValue, 'date': this[dayDate] })
        ++index;
      }
      this.responseInDays[setDay] = this[setDay];
    }
  }




  // --- DYNAMIC WEEK FORM CREATION
  dynamicFormCreationAsPerProjects(projectsTM: any) {
    let projectLength = projectsTM.length;

    for (let i = 0; i < this.day.length; ++i) {
      let index = 1;
      while (index <= projectLength) {
        this.dynamicFormField[this.day[i] + "" + index] = '';
        ++index;
      }
    }
    this.daysForm = this.weekForm.get('daysForm') as FormArray;
    // this.weekForm.get('daysForm').reset();
    this.daysForm.push(this.fb.group(this.dynamicFormField));
    this.projects = projectsTM;
    this.setValueToTimeSheetField();

  }




 // --- SET VALUE TO TIMESHEET FIELD DYNAMICALLY
 setValueToTimeSheetField() {
  let projectLength = this.projects.length;
  for (let i = 0; i < this.day.length - 1; ++i) {
    let setDay = this.day[i];
    let holiday = this.day[i] + 'Holiday';
    let index = 1;
    while (index <= projectLength) {
      let keyTM = setDay + index;
      let valueTM = this.projects[index - 1][setDay];
      if(projectLength === 1 && this[holiday]['is'] === true){
        this.weekForm.get('daysForm').patchValue([
          { [keyTM]: 0 }
        ]);
      }else{
        this.weekForm.get('daysForm').patchValue([
          { [keyTM]: valueTM }
        ]);
      }
      ++index;
    }
    this.caluculateHours(setDay);
  }
}



  // --- Moving Previous or Next Week Slot
  btnPreNext(move: number) {
    this.weekSlotSelected = this.weekSlotSelected + move;
    this.weekForm.patchValue({
      week: this.weekSlotSelected
    });
    this.configureWeekStartEndValueForService(this.weekList[this.weekSlotSelected - 1].startDate,
      this.weekList[this.weekSlotSelected - 1].endDate,
      this.weekList[this.weekSlotSelected - 1].weekNo);
    //      this.weekList[this.weekSlotSelected - 1].weekNo);
  }

  // --- SETTING DEFAULT SELECT WEEK FOR DROPDOWN
  setDefaultsDropDown() {
    this.weekSlotSelected = this.weekList.length;
    this.weekForm.patchValue({
      week: this.weekSlotSelected
    })
    this.configureWeekStartEndValueForService(this.weekList[this.weekSlotSelected - 1].startDate,
      this.weekList[this.weekSlotSelected - 1].endDate,
      this.weekList[this.weekSlotSelected - 1].weekNo);
    //this.weekList[this.weekSlotSelected - 1].weekNo);
  }



  // -- ASSIGNING THE DATES TO THE RESPEFCTIVE DAYS FOR SELECTED WEEK
  getDateFromSelectedWeek(startDate: string, endDate: string) {
    let endMonth= false; let firstDate = '';

    let monday = new Date(startDate);
    if(!endMonth && (monday.getDate() === 1)){
       endMonth= true; firstDate = 'monday';
    }
    this.monDate = `${ this.changeTwoDigitDate((monday.getMonth() + 1).toString())}/${ this.changeTwoDigitDate(monday.getDate().toString())}/${monday.getFullYear()}`;
    
    let tuesday = new Date(monday.setDate(monday.getDate() + 1));
    if(!endMonth && (tuesday.getDate() === 1)){
      endMonth= true; firstDate = 'tuesday';
   }
    this.tueDate = `${this.changeTwoDigitDate((tuesday.getMonth() + 1).toString())}/${this.changeTwoDigitDate(tuesday.getDate().toString())}/${tuesday.getFullYear()}`;
    
    let wednesday = new Date(tuesday.setDate(tuesday.getDate() + 1));
    if(!endMonth && (wednesday.getDate() === 1)){
      endMonth= true; firstDate = 'wednesday';
   }
    this.wedDate = `${ this.changeTwoDigitDate((wednesday.getMonth() + 1).toString())}/${ this.changeTwoDigitDate(wednesday.getDate().toString())}/${wednesday.getFullYear()}`;
    
    let thursday = new Date(wednesday.setDate(wednesday.getDate() + 1));
    if(!endMonth && (thursday.getDate() === 1)){
      endMonth= true; firstDate = 'thursday';
   }
    this.thuDate = `${ this.changeTwoDigitDate((thursday.getMonth() + 1).toString())}/${ this.changeTwoDigitDate(thursday.getDate().toString())}/${thursday.getFullYear()}`;
    
    let friday = new Date(thursday.setDate(thursday.getDate() + 1));
    if(!endMonth && (friday.getDate() === 1)){
      endMonth= true; firstDate = 'friday';
   }
    this.friDate = `${ this.changeTwoDigitDate((friday.getMonth() + 1).toString())}/${ this.changeTwoDigitDate(friday.getDate().toString())}/${friday.getFullYear()}`;
    console.log(endMonth+ ' --- '+firstDate); 

   this.mondayEnd=false; this.tuesdayEnd=false; this.wednesdayEnd=false; this.thursdayEnd=false;this.fridayEnd=false;
   let selectedMonth = this.weekList[this.weekSlotSelected - 1]['endDate'].substr(0,2);
   let date1  = new Date();
   let  month= date1.getMonth()+1;
   let currentMonth = month.toString().length === 1 ? '0'+month : month;
   console.log(currentMonth);
   console.log(selectedMonth);

    if((currentMonth === selectedMonth) && endMonth){
      this.blockLastDateMonth(firstDate);
    }
  }

  blockLastDateMonth(day:string){
    const index = this.day.findIndex(d=> d === day);
    for(let i=0; i<index; ++i){
      const lastDay = this.day[i]+'End';
      this[lastDay]=true;
    }
  }

  // --- WEEK SLOT FOR DROPDOWN
  getWeek_With_Start_End_Date() {
    var currentDate = new Date(); 
    var start = new Date(currentDate.getFullYear() - 1, 0, 1);   
    var first = start.getDate() - start.getDay() + 1;   
    var startDate = new Date(start.setDate(first));    
    var last = startDate.getDate() + 4;   
    var endDate = new Date(start.setDate(last));   
    let weekNo = 1;
    while (startDate.getTime() <= currentDate.getTime()) {
      const startDD = this.changeTwoDigitDate(startDate.getDate().toString());
      const startMM = this.changeTwoDigitDate((startDate.getMonth() + 1).toString());
      const startYY = startDate.getFullYear();
      const finalStartDate = `${startMM}/${startDD}/${startYY}`;

      const endDD = this.changeTwoDigitDate(endDate.getDate().toString());
      const endMM = this.changeTwoDigitDate((endDate.getMonth() + 1).toString());
      const endYY = endDate.getFullYear();
      const finalEndDate = `${endMM}/${endDD}/${endYY}`;
      this.weekList.push({
        startDate: finalStartDate, endDate: finalEndDate, weekNo: weekNo,
        status: 'Not Filled'
      });

      first = endDate.getDate() + 3;
      startDate = new Date(start.setDate(first));
      last = startDate.getDate() + 4; 
      endDate = new Date(start.setDate(last)); 

      ++weekNo;
    }
  }


  changeTwoDigitDate(data: string): string {
    if (data.length === 1) {
      return 0 + data;
    }
    return data;
  }

}
