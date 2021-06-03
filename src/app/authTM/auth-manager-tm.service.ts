import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { FormGroup } from '@angular/forms';
import { CorsBaseUrl } from '../CommonCodeVal/corsBaseURL';
import { LoaderServiceService } from '../Loader/loader-service.service';

@Injectable({
  providedIn: 'root'
})
export class AuthManagerTmService {

  baseUrl = CorsBaseUrl.devURL;
  private userBurn: string = '../../assets/json/burnReport.json';

  constructor(private http: HttpClient, private loaderService: LoaderServiceService) {
    console.log("baseUrl:", this.baseUrl);
  }

  headers = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  }



  // USER BURN RATE 
  getUserBurnRate(): Observable<any> {
    return this.http.get(`${this.userBurn}`, this.headers);
  }

  getUserBurnRateService(formValue: any): Observable<any> {
    const burnApiInput = {
      'email': localStorage.getItem('tm@Ics#user'),
      'role': localStorage.getItem('tmRole'),
      'startDate': '',
      'endDate': '',
      'project': formValue['project'][0]['id'],
      'account': formValue['account'][0]['id']
    }
    const monthNumber = formValue['month'][0]['id']; 
    const year=formValue['year'][0]['yearName'];
    this.setStartEndDate(monthNumber,year, burnApiInput);
    
    console.log("burnApiInput:", burnApiInput); 
    return this.http.post(`${this.baseUrl}TM/TimesheetEntry/showReport`,burnApiInput,this.headers)
    .pipe(
      tap(
        (event) => {
          if (event instanceof HttpResponse) {
            this.loaderService.requestEnded();
          }
        },  
        (error: HttpErrorResponse) => {
          this.loaderService.requestEnded();
          console.log("Error as - " + error.message);
        }
      )
    )
  }
  getHolidaysForExcel(response: any): Observable<any> {
    return this.http.post(`${this.baseUrl}TM/TimesheetEntry/getHolidaysForExcel`,response,this.headers)
    .pipe(
      tap(
         (event) => {
          if (event instanceof HttpResponse) {
            this.loaderService.requestEnded();
          }
        },
         (error: HttpErrorResponse) => {
          this.loaderService.requestEnded();
          console.log("Error as - " + error.message);
        }
      )
    )
  }
  
 
  setStartEndDate(monthNumber,syear, burnApiInput){
    
    const date = new Date();
    const selectedyear=syear;
    console.log("date:", date);
    const Year = date.getFullYear();
    console.log("date.full year:", date.getFullYear);
    const startDate = new Date(selectedyear,monthNumber,1);
    const endDate = new Date(selectedyear,monthNumber+1,0);
    const d=new Date(2020,2,29);
    console.log(startDate,endDate,d,monthNumber,monthNumber+1);
    burnApiInput.startDate= `${startDate.getMonth()+1}/${startDate.getDate()}/${startDate.getFullYear()}`;
    burnApiInput.endDate= `${endDate.getMonth()+1}/${endDate.getDate()}/${endDate.getFullYear()}`;
    console.log(burnApiInput.startDate,burnApiInput.endDate);
  }
  
  // ---FETCH ACCOUNT DETAILS
  getAccountAndProject(){
    return this.http.get('http://localhost:8060/TM/Profile/accountDetails')
        .pipe(
          tap(
            (error: HttpErrorResponse) => {
              this.loaderService.requestEnded();
            }
          )
        )
  }
  
  //ADD USER API CALL
  addUserInUserManagement(formValue: FormGroup, type: string): Observable<any> {
    this.loaderService.requestStarted();
    const addUserApiInput = {
      'action': sessionStorage.getItem('actionOnAddUpdate'),
      'firstName': formValue.get('firstName').value,
      'lastName': formValue.get('lastName').value,
      'emailId': formValue.get('email').value,
      'empId': formValue.get('empId').value,
      'role': formValue.get('role').value[0].roleName,
      'type': type,
      'billable':formValue.get('billable').value,
      "location": '',
      'rate': '',
      'vendor': '',
      'manager': '',
      'vendorContact': '',
      'lead': '',
      'accounts':[],
      'projectList': [],
      'isUser': '',
      'isLead': '',
      'newLead': '',
      'isManager': '',
      'isToolAdmin': '',
      'createDate': this.getCurrentDate()
    }

    if (type === "onshore") {
      addUserApiInput.location = formValue.get('location').value[0].location;
    }
    else if (type === 'offshore') {
      addUserApiInput.location = formValue.get('location2').value[0].location;
    }
    if(  formValue.get('rate').value == '' ||  formValue.get('rate').value == null)
    {
      addUserApiInput.rate ='0';
    }    
    else{
      addUserApiInput.rate = formValue.get('rate').value.toString();
    }
    if(   formValue.get('vendor').value == ''  ||  formValue.get('vendor').value == null){
      addUserApiInput.vendor = '';
    }
    else{
      addUserApiInput.vendor= formValue.get('vendor').value.toString();
    }
    this.getProjectArray(addUserApiInput, formValue);
    this.getAccountArray(addUserApiInput, formValue);
 
    this.assignValueAsRole(addUserApiInput, formValue);
    console.log(addUserApiInput);

    return this.http.post(this.baseUrl + "TM/Profile/addProfile",
      JSON.stringify(addUserApiInput), this.headers)
      .pipe(
        tap(
           (event) => {
            if (event instanceof HttpResponse) {
              this.loaderService.requestEnded();
            }
          },
          (error: HttpErrorResponse) => {
            this.loaderService.requestEnded();
            console.log("Error as - " + error.message);
          }
        )
      )
  }



  // LOADING HIERARCHY DATA ON DEFAULT LOAD
  loadUserDetailBYDefault(emailId: string, role: string): Observable<any> {
    this.loaderService.requestStarted();
    return this.http.get(`${this.baseUrl}TM/Profile/userDetails/emailId/${emailId}/role/${role}`)
      .pipe(
        tap(
        /*   (event) => {
             if (event instanceof HttpResponse) {
               this.loaderService.requestEnded();
             }
           }, */
          (error: HttpErrorResponse) => {
            this.loaderService.requestEnded();
          }
        )
      )
  }



  // --- GET USER TIMESHEET DETAILS 
  getUserTimesheetDetails(response: Object): Observable<any> {
    console.log("response:", response);
    this.loaderService.requestStarted();
    return this.http.post(this.baseUrl + "TM/TimesheetEntry/timesheetDetail",
      JSON.stringify(response), this.headers)
      .pipe(
        tap(
          (event) => {
            if (event instanceof HttpResponse) {
              this.loaderService.requestEnded();
            }
          },
          (error: HttpErrorResponse) => {
            this.loaderService.requestEnded();
            console.log("Error as - " + error.message);
          }
        ) 
      )
  } 



  // --- SAVE/SUBMIT THE USER TIMESHEET
  saveOrSubmitTheTimesheet(response: Object): Observable<any> {
    console.log("save or submit response:", response);
    this.loaderService.requestStarted();
    return this.http.post(this.baseUrl + "TM/TimesheetEntry/submitTimesheetDetails",
      JSON.stringify(response), this.headers)
      .pipe(
   /*      tap(
          (error: HttpErrorResponse) => {
            this.loaderService.requestEnded();
          }
        ) */
    
        tap(
          // (event) => {
            // if (event instanceof HttpResponse) {
              // this.loaderService.requestEnded();
             //}
           //},
          (error: HttpErrorResponse) => {
            this.loaderService.requestEnded();
            console.log("Error as - " + error.message);
          }
        )
      )
  }



  // LOADING HIERARCHY DATA ON DEFAULT LOAD
  getWeekStatus(emailId: string): Observable<any> {
    this.loaderService.requestStarted();
    return this.http.get(`${this.baseUrl}TM/TimesheetEntry/weekStatus/${emailId}`)
      .pipe(
        tap(
          /*  (event) => {
             if (event instanceof HttpResponse) {
               this.loaderService.requestEnded();
             }
           }, */
          (error: HttpErrorResponse) => {
            this.loaderService.requestEnded();
          }
        )
      )
  }

  // --- SERVICE CALL FOR APPROVE, REJECT AND REVOKE
  serviceOnApproveOrReject(mail: string, action: string, startDate: string, endDate: string, weekNo: number) {
    this.loaderService.requestStarted();
    console.log("mail action:", mail + "- " + action);
    let url: string = '';
    if (action === 'reject') {
      url = 'TM/TimesheetEntry/rejectTimesheetDetails';
    }
    else if (action === 'approve') {
      url = 'TM/TimesheetEntry/approveTimesheetDetails';
      return this.http.get(`${this.baseUrl}${url}?email=${mail}&weekNo=${weekNo}&startDate=${startDate}&endDate=${endDate}&approverRole=${localStorage.getItem('tmRole')}`, { responseType: 'text' })
    }
    else if (action === 'revoke') {
      url = 'TM/TimesheetEntry/revokeTimesheetDetails';
    }
    return this.http.get(`${this.baseUrl}${url}?email=${mail}&weekNo=${weekNo}&startDate=${startDate}&endDate=${endDate}`, { responseType: 'text' })

  }


  //--- GET EMPLOYEE DETAIL FOR BULK APPROVAL/REJECT USING START/END DATE
  getDetailforBulkOnStartAndEndDate(email: string, role: string,
    startDate: string, endDate: string): Observable<any> {
    this.loaderService.requestStarted();
    let response = {
      email: email,
      startDate: startDate,
      endDate: endDate,
      role: role
    }

    console.log("bulk", response);
    return this.http.post(this.baseUrl + "TM/TimesheetEntry/selectedWeek",
      JSON.stringify(response), this.headers)
      .pipe(
        tap(
          (error: HttpErrorResponse) => {
            this.loaderService.requestEnded();
          }
        )
      )
  }


  // --- ACTION ON BULK APPROVE OR REJECT
  actionOnBulkApproveAndReject(action: string, responseData: any): Observable<any> {
    this.loaderService.requestStarted();
    let url = '';
    if (action === 'approve') {
      url = 'TM/TimesheetEntry/approveAll';
    }
    else if (action === 'reject') {
      url = 'TM/TimesheetEntry/rejectAll';
    }
    return this.http.post(`${this.baseUrl}${url}`, responseData, { responseType: 'text' })

  }



  // --- CONVERT PROJECT OBJECT TO ARRAY
  getProjectArray(addUserApiInput: any, formValue: FormGroup) {
    let projectLists = [];
    console.log(formValue.get('projectList'));
    console.log(formValue.get('projectList').value[0].projectName)
    let projectCount = formValue.get('projectList').value.length;
    for (let i = 0; i < projectCount; ++i) {
//      projectLists.push(formValue.get('projectList').value[i]);
      projectLists.push(formValue.get('projectList').value[i].projectName);
    }
    addUserApiInput.projectList = projectLists;
  }  
 
  getAccountArray(addUserApiInput: any, formValue: FormGroup) {
    let AccountLists = [];
    let projectCount = formValue.get('accounts').value.length;
    for (let i = 0; i < projectCount; ++i) {
      AccountLists.push(formValue.get('accounts').value[i].accountName);
    }
    addUserApiInput.accounts = AccountLists;
  }


  // --- ASSIGN VALUE to PROPERTY AS PER ROLE OF USER
  assignValueAsRole(addUserApiInput: any, formValue: FormGroup) {
    if (localStorage.getItem('tmRole') === 'admin') {
      addUserApiInput.isToolAdmin = 'yes';
      // addUserApiInput.manager = formValue.get('email').value;
    }

    if (localStorage.getItem('tmRole') === 'Manager') {
      if (localStorage.getItem('tmManagerSelected') == 'Lead') {
        addUserApiInput.isLead = 'yes';
        addUserApiInput.lead = localStorage.getItem('tm@Ics#user');
        addUserApiInput.manager = localStorage.getItem('tm@Ics#user');
      }
      if (localStorage.getItem('tmManagerSelected') === 'User') {
        addUserApiInput.isUser = 'yes';
        addUserApiInput.manager = localStorage.getItem('tm@Ics#user');
        addUserApiInput.lead = formValue.get('lead').value[0].id;
      }
    }
    if (localStorage.getItem('tmRole') == 'Lead') {
      addUserApiInput.isUser = 'yes';
      addUserApiInput.lead = localStorage.getItem('tm@Ics#user');
      addUserApiInput.manager = sessionStorage.getItem('leads_Manager');
    }
  }


  // --- GET CURRENT DATE
  getCurrentDate(): string {
    let today = new Date();
    let todayDate = `${ this.changeTwoDigitDate((today.getMonth() + 1).toString())}/${this.changeTwoDigitDate(today.getDate().toString())}/${today.getFullYear()}`;
          
    return todayDate;
  }

  changeTwoDigitDate(data: string):string{
    if(data.length === 1){
      return 0+data;
    }
    return data;
  }
}
