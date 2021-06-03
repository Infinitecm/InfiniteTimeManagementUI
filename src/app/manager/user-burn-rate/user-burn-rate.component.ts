import { AfterViewInit, Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { Subject } from 'rxjs';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DataTableDirective } from 'angular-datatables';
import 'datatables.net';
import 'datatables.net-dt/css/jquery.dataTables.css';
import 'datatables-fixedcolumns';
import 'datatables-fixedcolumns/css/fixedColumns.dataTables.scss';
import { AuthManagerTmService } from '../../authTM/auth-manager-tm.service';
import { ExportExcelService } from '../../export-excel.service';


@Component({
  selector: 'app-user-burn-rate',
  templateUrl: './user-burn-rate.component.html',
  styleUrls: ['./user-burn-rate.component.css']
})
export class UserBurnRateComponent implements OnInit, AfterViewInit {

  @ViewChild(DataTableDirective)
  dtElement: DataTableDirective;

  public  dtOptions: any = {};
  public dtTrigger: Subject<any> = new Subject();  private dtTrigger2: Subject<any> = new Subject();
  private month = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
  public  yearList:any=[];
  private year:any=[];
  private selectedYear:any=[];
  public burnForm: FormGroup; 
  public  burnReports: any[] = [];  private burnReports2: any[] = [];
  public burnReportBool: boolean;    private burnReportBool2: boolean;
  public  errorMsg: boolean = false;  private errorMsg2: boolean = false;
  public  monthList: any = [];
  public  monthDropDownSettings: any = {};
  private selectedMonth: any = [];
  public accountList: any = [{ id: 'All', accountName: 'All Account' }];
  public  accountDropDownSettings: any = {};
  private selectedAccount: any = [{ id: 'All', accountName: 'All Account' }];
  public projectList: any = [];
  public projectDropDownSettings: any = {};
  private selectedProject: any = [];
  public yearDropdownSettings:any=[];


  columnList: any = [];
  projectInfo: any = [];

  excelColumn: any = [];
  excelData: any = [];


  constructor(
    private manager: AuthManagerTmService,
    private fb: FormBuilder,
    public router: Router,
    private excelService: ExportExcelService
  ) { 
   

  }

  ngOnInit() {
    this.projectInfo = JSON.parse(localStorage.getItem(('accountInfo')));
    this.initializeDataTable();
    this.renderDropDownValue();
    this.initializeForm();
    this.renderDropDownSettings();


    this.burnForm.valueChanges.subscribe(val => {
      this.errorMsg = false;
    });
  }


  ngAfterViewInit(): void {
    this.manager.getUserBurnRateService(this.burnForm.value).subscribe(
      val => {
        console.log(val);
        this.burnReportBool = !(val.length > 0);
        if (!this.burnReportBool) {
          this.burnReports = val;
          this.dtTrigger.next();
        }
      });
  }

  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  rerender(): void {
    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
      dtInstance.destroy();
      this.initializeDataTable();
      setTimeout(() => {
        this.dtTrigger.next();

      }, 200);
    });
  }


  // ---INITIALIZE THE DATA TABLE SETTINGS
  initializeDataTable() {
    this.dtOptions = {
      scrollY: '400px',
      scrollX: true,
      ordering: true,
      info: false,
      scrollCollapse: true,
      // paging: false,
      columnDefs: [
        { width: '60px', targets: 0 },
        { width: '140px', targets: 1 },
        { width: '90px', targets: 2 },
        { width: '90px', targets: 3 }
      ],
      fixedColumns: {
        leftColumns: 4,
        rightColumns: 2
      }
    };
  }

  // ---REACTIVE FORM INITIALIZATION
  initializeForm() {
    this.burnForm = this.fb.group({
      'account': [this.selectedAccount, [Validators.required]],
      'project': [this.selectedProject, [Validators.required]],
      'month': [this.selectedMonth, [Validators.required]],
      "year":[this.selectedYear,Validators.required]
    });
  }

  // ---SEARCHING EMPLOYEES FOR COMPLETE APPROVED MONTH TIMESHEET
  searchMonthlyTimesheet() {
    if (this.burnForm.valid === false) {
      this.errorMsg = true;
    } else {
      this.manager.getUserBurnRateService(this.burnForm.value).subscribe(
        val => {
          console.log('val:', val);
          this.burnReportBool = !(val.length > 0);
          const prevBurnReport = (this.burnReports.length === 0);
          if (!this.burnReportBool) {
            this.burnReports = val;
            if (prevBurnReport === true) {
              this.dtTrigger.next();
            } else {
              this.rerender();
            }
          }console.log('b1:', this.burnReports);
        });
    }
  }
 //DOWNLOAD REPORTS
 downloadBurnReports() {
  console.log('download report');
  if (this.burnForm.valid === false) {
  } else {
    const month = this.burnForm.get('month').value[0]['id'] + 1;
    const year = this.burnForm.get('year').value[0]["yearName"];
    const endD = new Date(year, month, 0);
    const response = {
      startDate: `${month}/1/${year}`,
      endDate: `${endD.getMonth() + 1}/${endD.getDate()}/${endD.getFullYear()}`
    }
    this.manager.getHolidaysForExcel(response).subscribe(
      val => {
        this.burnReportBool2 = !(val.length > 0);
        const prevBurnReport2 = (this.burnReports2.length === 0);
        if (!this.burnReportBool2) {
          this.burnReports2 = val;
          if (prevBurnReport2 === true) {
            this.dtTrigger2.next();
          } else {
            this.rerender();
          }
        }  
        this.excelService.exportExcel(this.burnReports, this.burnReports2);
      });
  }   
}

  generateExcelColumn() {
    this.excelColumn=[];
    console.log(this.burnReports);
    const reportsNumber = this.burnReports.length;
    if(reportsNumber>0){
      Object.keys(this.burnReports[0]).forEach((i, v) => {
        const val= i;
        if(val === 'hours'){
          const days= this.burnReports[0]['hours'].length;
          const daysArr= this.burnReports[0]['hours'];
           
          for(let day = 0; day < days; ++day){
            this.excelColumn.push(`${daysArr[day]['day']} - ${daysArr[day]['date']}`);
          }
        }
        else if(val === 'weeks'){
           const weeks = this.burnReports[0]['weeks'].length;
           for(let week = 0; week < weeks; ++week){
              this.excelColumn.push(`Week${week+1}`);
           }
          }
        else{
          this.excelColumn.push(i);
        }
        
      });
    }
    console.log(this.excelColumn);
  }

  // --- SET THE DROPDOWN VALUE
  renderDropDownValue() {
    
    console.log(this.projectInfo);
    const accountLength = this.projectInfo.length;
    let date = new Date();
    const currentMonth = date.getMonth();
    // if(currentMonth===0){

    //   this.selectedYear=date.getFullYear()-1;

    // }
    // else{
    //   this.selectedYear=date.getFullYear();
    // }
    // console.log(this.selectedYear,"the year at initial");

    for (let i = 0; i < accountLength; ++i) {
      this.accountList.push({ id: this.projectInfo[i]['account'], accountName: this.projectInfo[i]['account'] });
    }

    this.projectList.push({ id: 'All', projectName: 'All Project' });
    console.log("the year",this.selectedYear);
   
    for (let i = 0; i < 12; ++i) {
      this.monthList.push({ id: i, monthName: this.month[i] });
    
  }
  let year=date.getFullYear();
  for(let i=0;i<3;i++){
    this.yearList.push({id:year,yearName:year});
    year--;
  }

    this.selectedProject.push({ id: 'All', projectName: 'All Project' });
   
    if(currentMonth===0){ 
      this.selectedMonth.push({ id: 11, monthName: this.month[11] });
      this.selectedYear.push({id:date.getFullYear()-1,yearName:date.getFullYear()-1});
    }
    else{
      this.selectedMonth.push({ id: currentMonth - 1, monthName: this.month[currentMonth - 1] });
      this.selectedYear.push({id:date.getFullYear(),yearName:date.getFullYear()});
    }
  
   
  }


  // --- SET THE SELECT DROPDOWN SETTINGS
  renderDropDownSettings() {
    this.accountDropDownSettings = {
      singleSelection: true,
      idField: 'id',
      textField: 'accountName',
      closeDropDownOnSelection: true
    }
    this.projectDropDownSettings = {
      singleSelection: true,
      idField: 'id',
      textField: 'projectName',
      closeDropDownOnSelection: true
    }
    this.monthDropDownSettings = {
      singleSelection: true,
      idField: 'id',
      textField: 'monthName',
      closeDropDownOnSelection: true
    }
    this.yearDropdownSettings={
      singleSelection:true,
      idField:"id",
      textField:"yearName",
      closeDropDownOnSelection: true
    }
  }


  // ---SELECT ACCOUNT DROPDOWN
  onSelectAccount(type: any) {
    this.selectedAccount = type.accountName;
    if (this.selectedAccount != 'All Account') {
      const accountIndex = this.projectInfo.findIndex(obj => obj.account === this.selectedAccount);
      this.setProjectDropDownAsAccount(this.projectInfo[accountIndex].projects);
    } else {
      this.setDefaultProject();
    }
  }
  onDeselectAccount(type: any) {
    this.setDefaultProject();
  }


  // --- SET PROJECTS AS PER ACCOUNT SELECTION 
  setProjectDropDownAsAccount(project: Array<any>) {
    this.setDefaultProject();
    for (let i = 0; i < project.length; ++i) {
      this.projectList.push({ id: project[i], projectName: project[i] });
    }
  }

  // ---SET DEFAULT PROJECT DROPDOWN ON ACCOUNT SWITCH
  setDefaultProject() {
    this.selectedProject = [];
    this.projectList = [{ id: 'All', projectName: 'All Project' }];
    this.burnForm.get('project').patchValue(this.selectedProject);
    
  }  


  // ---PROJECT DROPDOWN SELECTIONS
  onSelectProject(type: any) {
    this.selectedProject = type.projectName;
  }
  onDeselectProject(type: any) {
    this.selectedProject = [];
  }

  //---MONTH DROPDOWN SELECTIONS
  onSelectMonth(type: any) {
    this.selectedMonth = type.id;
    console.log(this.selectedMonth);
  }
  onDeselectMonth(type: any) {
    this.selectedMonth = [];
  }
//----YEAR DROPDOWN SELECTIONS
onSelectYear(type:any){
  // this.selectedYear=[];
  this.selectedYear=type.id;
  console.log(this.selectedYear);
}

onDeselectYear(type: any) {
  this.selectedYear=[];
}
  setDataTable() {
    const param = {
      scrollY: "300px",
      scrollX: true,
      scrollCollapse: true,
      paging: false,
      fixedColumns: true
    }
    setTimeout(() => {
      var t = $('#example').DataTable(param);
    }, 200);
  }

}

