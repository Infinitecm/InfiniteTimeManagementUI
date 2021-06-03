import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthManagerTmService } from '../../authTM/auth-manager-tm.service';
import { LoaderServiceService } from '../../Loader/loader-service.service';
import { Router } from '@angular/router';
import { CommonMethod } from '../../CommonCodeVal/CommonMethod';

@Component({
  selector: 'app-user-management-edit-profile',
  templateUrl: './user-management-edit-profile.component.html',
  styleUrls: ['./user-management-edit-profile.component.css']
})
export class UserManagementEditProfileComponent implements OnInit {

  umAddUser: FormGroup;
  logCredential = false;
  limitSelection = false;
  projectList = [];
  accounts: any = [];
  roleList: any = [];
  editRole: string = '';
  leadList: any = [];
  lead: any = [];
  selectedItems: any = [];
  roleDropDownSettings: any = {};
  leadDropDownSettings: any = {};
  selectedRole1: any = [];
  selectedLead: any = [];
  managerSelected: string = '';
  empRole: string = '';
  seniorName: string = '';
  editProfile: any;
  type: string = '';
  offshoreDropDownSettings: any = {};
  onshoreDropDownSettings: any = {};
  selectedOnshore: any = [];
  selectedOffshore: any = [];
  selectedLocation: any = [];
  onshoreList: any = [];
  offshoreList: any = [];
  onshore: any = []; offshore: any = [];
  editLocation: any;
  previous: any;
  locationtype1: boolean = false;
  locationtype2: boolean = false;
  private selectedLink: string = "";
  demoaccountList: any = [];
  projectDataset: any = [];
  accountsList: any = [];
  dropdownSettings: {};
  demoaccountsettings: {};
  selectedAccounts: any = [];
  billStatus: string = '';

  editAccount: any = [];
  editProjects: any = [];
  validationMessages = {
    'firstName': {
      'required': 'firstName is required.',
      'minlength': 'firstName must be greater than 2 characters.',
      'maxlength': 'firstName must be less than 25 characters.',
    },
    'lastName': {
      'required': 'lastName is required.',
      'minlength': 'lastName must be greater than 2 characters.',
      'maxlength': 'lastName must be less than 25 characters.',
    },
    'email': {
      'required': 'Email is required',
      'email': 'Email is not Valid'
    },
    'empId': {
      'required': 'Employee ID is required.',
      'minlength': 'Employee ID must be greater than 2.'
    },
    /*  'rate': {
        'required': 'rate is required.' 
     },
     'vendor': {
       'required': 'vendor name is required.', 
       'minlength': 'vendor name must be greater than 2 characters.',
       'maxlength': 'vendor name must be less than 20 characters.', 
     } */
  };


  formErrors = {
    "firstName": "",
    "lastName": "",
    "email": "",
    "empId": "",
    "location": "",
    "location2": "",
    'accounts': "",
    "projectList": "",
    "role": "",
    "lead": "",
    "rate": "",
    "vendor": ""
  }


  constructor(private fb: FormBuilder, private authManager: AuthManagerTmService,
    private service: AuthManagerTmService,
    private router: Router, private loaderService: LoaderServiceService) {
    if (!CommonMethod.authenticateSenior()) {
      this.router.navigate(['/login']);
    }
  }

  ngOnInit() {
    this.renderConsolidateFunction();
    this.setAccountAndProject();
    let num = this.leadList.findIndex(a => a.id === this.editProfile.lead);
    if (num != '-1') {
      this.selectedLead.push({ id: this.leadList[num].id, leadName: this.leadList[num].leadName });
    }
    // this.selectedItems=[{ id: 'Vmas', projectName: 'Vmas' }];
    this.umAddUser = this.fb.group({
      firstName: [this.editProfile.firstName, [Validators.required, Validators.minLength(2), Validators.maxLength(25)]],
      lastName: [this.editProfile.lastName, [Validators.required, Validators.minLength(2), Validators.maxLength(25)]],
      email: [this.editProfile.emailId, [Validators.required, Validators.email]],
      empId: [this.editProfile.empId, [Validators.required, Validators.minLength(2)]],
      location: [this.selectedLocation],
      location2: [this.selectedLocation],
      billable: [this.billStatus],
      accounts: [this.selectedAccounts],
      projectList: [this.selectedItems],
      role: [this.selectedRole1],
      lead: [this.selectedLead],
      rate: [this.editProfile.rate],
      vendor: [this.editProfile.vendor]
    });

    this.umAddUser.valueChanges.subscribe((val) => {
      this.logCredential = false;
      this.logValidationErrors(this.umAddUser);
    });
    //this.renderDropDownSettings();
   //  this.dropDownMultiSelectSetting();  - lp
  }

  //  --- END OF ngOnInit --- 

  renderConsolidateFunction() {
    this.getSessionForEdit();
    this.renderDropDownValue();
    // this.serviceForMultiDropDown();
    this.dropDownMultiSelectSetting();
    this.setLocalStorageValue();
  }



  // --- SET THE MULTI SELECT DROPDOWN SETTINGS
  renderDropDownSettings() {
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'projectName',
      itemsShowLimit: 5,
      closeDropDownOnSelection: false
    };
    this.demoaccountsettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'accountName',
      itemsShowLimit: 5,
      closeDropDownOnSelection: false
    }

    this.onshoreDropDownSettings = {
      singleSelection: true,
      idField: 'type',
      textField: 'location',
      closeDropDownOnSelection: true
    }
    this.offshoreDropDownSettings = {
      singleSelection: true,
      idField: 'type',
      textField: 'location',
      closeDropDownOnSelection: true
    }
    this.roleDropDownSettings = {
      singleSelection: true,
      idField: 'id',
      textField: 'roleName',
      closeDropDownOnSelection: true
    }
    this.leadDropDownSettings = {
      singleSelection: true,
      idField: 'id',
      textField: 'leadName',
      closeDropDownOnSelection: true
    }
  }


  // GET SESSION STORAGE VALUE FOR EDIT FROM MANAGER TIMESHEET 
  getSessionForEdit() {
    this.editProfile = JSON.parse(sessionStorage.getItem('editProfile'));
    this.billStatus = this.editProfile.billable;
    this.previous = this.editProfile.location;
    this.selectedLink = this.editProfile.type;
    if (this.editProfile.type == 'onshore') {
      this.locationtype1 = true;
    }
    else {
      this.locationtype2 = true;
    }
    this.setAccountAndProject();
    let x = JSON.parse(localStorage.getItem(('accountInfo')));
    this.accounts = x;
    this.editRole = this.editProfile.role;
    this.editLocation = this.editProfile.location;
    this.selectedRole1.push({ 'id': this.editRole, 'roleName': this.editRole })
    this.selectedLocation.push({ 'type': this.editLocation, 'location': this.editLocation })
    this.editAccount = this.editProfile.accounts;
    this.editProjects = this.editProfile.projects;
    for (let i = 0; i < this.editProfile.accounts.length; ++i) {
      this.selectedAccounts.push({ 'id': this.editProfile.accounts[i], 'accountName': this.editProfile.accounts[i] });
    }
    for (let c = 0; c < this.selectedAccounts.length; c++) {
      this.projectDropDownOnChange(this.selectedAccounts[c]);
    }
    for (let i = 0; i < this.editProfile.projects.length; ++i) {
      this.selectedItems.push({ 'id': this.editProfile.projects[i], 'projectName': this.editProfile.projects[i] });
    }
  }

  setBillable(billStatus: string) {
    this.umAddUser.patchValue({
      'billable': billStatus
    })
    this.billStatus = billStatus;
  }

  //location details    
  setradio(e: string): void {
    this.selectedLink = e;
    if (e === "offshore") {
      this.reset();
    }
    else { this.reset2(); }
  }
  reset() {
    this.umAddUser.controls['location'].setValue("");
  }
  reset2() { this.umAddUser.controls['location2'].setValue(""); }
  isSelected(name: string): boolean {
    if (!this.selectedLink) {
      return false;
    }
    return (this.selectedLink === name);
  }
  setAccountAndProject() {
    return this.service.getAccountAndProject().subscribe(val => {
      this.loaderService.requestEnded();
      this.accounts = val.valueOf();
      this.onshoreList = []; this.offshoreList = []; this.roleList=[];
      this.renderDropDownValue();
    });
  }
  //SET DROPDOWN VALUE
  renderDropDownValue() {
    this.onshore = ["Chennai", "Bangalore US Back Office"];
    this.offshore = ["Gurgaon", "Bangalore", "Hyderabad"];
    let role: any = [];
    /* This is data present in the server */
    /* this.accounts =
   [{"account":"Verizon","projects":["VMAS","GENESYS","Tool Automation","TimeManagement","Iron Mountain","Scripps"]},
   {"account":"Zyter","projects":["PMS","IOS","Android"]},
   {"account":"Nokia","projects":["Network","Communication"]}];
    */
    this.accounts = JSON.parse(localStorage.getItem(('accountInfo')));
    let accountduplicate = [];
    for (let i = 0; i < this.accounts.length; ++i) {
      accountduplicate.push({ id: this.accounts[i].account, accountName: this.accounts[i].account });
    };
    this.accountsList = accountduplicate;
    //this.projectList = a.projects;
    let projectduplicate = [];
    for (let i = 0; i < this.projectList.length; ++i) {
      projectduplicate.push({ id: this.projectList[i], projectName: this.projectList[i] });
    };
    this.projectDataset = projectduplicate;
    if (this.editRole === 'User') {
      role = ['User'];
    }
    else if (this.editRole === 'Lead') {
      role = ['Lead'];
    }
    else if (this.editRole === 'Manager') {
      role = ['Manager'];
    }
    for (let i = 0; i < this.onshore.length; ++i) {
      this.onshoreList.push({ type: this.onshore[i], location: this.onshore[i] });
    }
    for (let i = 0; i < this.offshore.length; ++i) {
      this.offshoreList.push({ type: this.offshore[i], location: this.offshore[i] });
    }
    //for (let i = 0; i < role.length; ++i) {
      this.roleList.push({ id: role[0], roleName: role[0] });
    //}
    console.log('role.', this.roleList);
    this.lead = JSON.parse(sessionStorage.getItem('leadCollection'));
    for (let i = 0; i < this.lead.length; ++i) {
      this.leadList.push({ id: this.lead[i].emailId, leadName: this.lead[i].firstName + " " + this.lead[i].lastName });
    }

  }


  //SEETING LOCAL STORAGE VALUE TO VARIABLE
  setLocalStorageValue() {
    // Manager's selection for user/TL
    this.managerSelected = localStorage.getItem('tmManagerSelected');
    // Employee role
    this.empRole = localStorage.getItem('tmRole');
    // Lead and Manager default Name
    this.seniorName = localStorage.getItem('tmUserName');
  }


  dropDownMultiSelectSetting() {
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'projectName',
      itemsShowLimit: 5
    };
    this.demoaccountsettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'accountName',
      itemsShowLimit: 5
    }
    this.onshoreDropDownSettings = {
      singleSelection: true,
      idField: 'type',
      textField: 'location',
      closeDropDownOnSelection: true
    }
    this.offshoreDropDownSettings = {
      singleSelection: true,
      idField: 'type',
      textField: 'location',
      closeDropDownOnSelection: true
    }

    this.roleDropDownSettings = {
      singleSelection: true,
      idField: 'id',
      textField: 'roleName',
      closeDropDownOnSelection: true
    };

    this.leadDropDownSettings = {
      singleSelection: true,
      idField: 'id',
      textField: 'leadName',
      closeDropDownOnSelection: true
    };
  }

  //FOR SELECTED PROJECT
  onItemSelect(item: any) {
    this.selectedItems.push(item);
  }
  onItemDeselect(item: any) {
    let num = this.selectedItems.findIndex(a => a.id === item.id);
    this.selectedItems.splice(num, 1);
  }
  onSelectAll(item: any) {
    this.selectedItems = [];
    for (let i = 0; i < this.projectDataset.length; ++i) {
      //this.selectedItems.push(this.projectDataset[i].projectName);
      this.selectedItems.push({ id: this.projectDataset[i].projectName, projectName: this.projectDataset[i].projectName });
    }
  }
  onDeSelectAll(item: any) {
    this.selectedItems = [];
  }
  //for account
  onItemSelectDemoAccount(item: any) {
    this.selectedAccounts.push(item);
    this.projectDropDownOnChange(item);
  }
  projectDropDownOnChange(item) {

    for (let each of this.accounts) {
      if (each.account == item.accountName) {
        for (let each_projects of each.projects) {
          this.projectList.push(each_projects);
        }
      }
    }
    this.onshoreList = []; this.offshoreList = []; this.roleList = []; this.renderDropDownValue();
  }
  onItemDeSelectDemoAccount(item: any) {
    let num = this.selectedAccounts.findIndex(a => a.id === item.id);
    this.removeProjectsOnDeselectingAccount(item);
    this.selectedAccounts.splice(num, 1);
  }

  removeProjectsOnDeselectingAccount(item: any) {
    let flag = 0;
    let indexsave = [];
    for (let each of this.accounts) {
      if (each.account == item.accountName) {
        for (let each_projects of each.projects) {
          indexsave.push(this.projectList.findIndex(x => x === each_projects));
        }
        flag = 1;
      }
      let duplicate = [];
      for (let i = 0; i < this.projectList.length; ++i) {
        if (indexsave.indexOf(i) < 0) {
          duplicate.push(this.projectList[i]);
        }
      } this.projectList = duplicate;

      if (flag == 1) { break; }
    } this.onshoreList = []; this.offshoreList = []; this.roleList = [];
    this.renderDropDownValue();

  }
  onSelectAllDemoAccount(item: any) {
    this.projectList = [];
    this.selectedAccounts = [];
    for (let each of item) {
      this.projectDropDownOnChange(each);
    }
    for (let i = 0; i < this.accounts.length; ++i) {
      //this.selectedAccounts.push(this.accounts[i].account);
      this.selectedAccounts.push({ 'id': this.accounts[i].account, 'accountName': this.accounts[i].account });
    }
  }
  onDeSelectAllDemoAccount(item: any) {
    this.selectedAccounts = [];
    this.projectList = [];
    this.onshoreList = []; this.offshoreList = []; this.roleList = []; this.renderDropDownValue();
  }

  //FOR SELECTED Onshore
  onItemSelectOnshore(type: any) {
    this.selectedOnshore = type.location;
    this.selectedLocation = this.selectedOnshore;
  }
  onItemDeselectOnshore(type: any) {
    this.selectedLocation = '';
  }
  //FOR SELECTED Offshore
  onItemSelectOffshore(type: any) {
    this.selectedOffshore = type.location;
    this.selectedLocation = this.selectedOffshore;
  }
  onItemDeselectOffshore(type: any) {
    this.selectedLocation = '';
  }

  //FOR SELECTED ROLE
  onItemSelectRole(item: any) {
    this.selectedRole1 = [];
    this.selectedRole1.push(item);
  }
  onItemDeselectRole(item: any) {
    this.selectedRole1 = [];
  }

  //FOR SELECTED LEAD
  onItemSelectLead(item: any) {
    this.selectedLead = item.leadName;
  }
  onItemDeselectLead(item: any) {
    this.selectedLead = '';
  }

  //USER MANAGEMENT ADD USER SUBMISSION 
  addUserSubmission(): void {
    if (!this.umAddUser.valid) {
      this.logCredential = true;
    } else {
      sessionStorage.setItem('actionOnAddUpdate', 'update');
      this.authManager.addUserInUserManagement(this.umAddUser, this.selectedLink)
        .subscribe((val) => {
          this.loaderService.requestEnded();
          if (val.statusCode == 200) {
            alert('Updated Successfully!')
            this.router.navigate(['/TM/UserManagement']);
          }
          else if (val.statusCode == 409) {
            alert('Member already exists!')
          }
        });
    }
  }


  // LOG VALIDATAION MESSAGE ---
  logValidationErrors(group: FormGroup = this.umAddUser): void {
    Object.keys(group.controls).forEach((key: string) => {
      const abstractControl = group.get(key);
      if (abstractControl instanceof FormGroup) {
        this.logValidationErrors(abstractControl);
      }
      else {
        //abstractControl.disable();
        this.formErrors[key] = '';
        if (abstractControl && !abstractControl.valid
          && (abstractControl.dirty || abstractControl.touched)) {
          const messages = this.validationMessages[key];

          for (const errorKey in abstractControl.errors) {
            if (errorKey) {
              this.formErrors[key] += messages[errorKey] + " ";
            }
          }
        }

      }
    })
  }


}

//SERVICE FOR DEFAULT SELECT DROP DOWN - LOCALLY JSON RESPONSE E.G.
//  serviceForMultiDropDown(){
//   this.authManager.editUserInUserManagement().subscribe( val =>{
//     //this.employeeInfo= val;
//     for(var i=0; i <val.project.length; ++i){
//       console.log("---"+ val.project[i]);
//     //  this.selectedItems.push({ id: val.project[i], projectName: val.project[i]});
//      this.selectedItems =  this.selectedItems.concat({ id: val.project[i], projectName: val.project[i]});
//     }
//     this.umAddUser.patchValue({project: this.selectedItems});  
//     this.selectedRole1.push({id: val.role, roleName: val.role})
//     this.umAddUser.patchValue({firstName:val.firstName, lastName:val.lastName, email:val.emailId, 
//                               rate: val.rate, project: this.selectedItems,
//                               role: this.selectedRole1, vendor: val.vendor});

//   });
//  }

