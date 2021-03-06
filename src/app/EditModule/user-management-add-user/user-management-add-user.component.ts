import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonMethod } from '../../CommonCodeVal/CommonMethod';
import { AuthManagerTmService } from '../../authTM/auth-manager-tm.service';
import { LoaderServiceService } from '../../Loader/loader-service.service';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';


@Component({
  selector: 'app-user-management-add-user',
  templateUrl: './user-management-add-user.component.html',
  styleUrls: ['./user-management-add-user.component.css']
})

export class UserManagementAddUserComponent implements OnInit {
  umAddUser: FormGroup;
  logCredential = false;
  limitSelection = false;
  projectList = [];
  accounts: any = [];
  role: any = [];
  lead: any = [];
  leadMail: any = [];
  roleList: any = [];
  leadList: any = [];
  selectedItems: any = [];
  roleDropDownSettings: any = {};
  leadDropDownSettings: any = {};
  selectedRole: string = '';
  selectedLead: string = '';
  selectedAccounts: any = [];
  seniorName: string = '';
  managerSelected: string = '';
  empRole: string = '';
  location: string = '';
  location2: string = '';
  type: string = '';
  selectedOnshore: string = '';
  offshoreDropDownSettings: any = {};
  onshoreDropDownSettings: any = {};
  selectedOffshore: string = "";
  onshoreList: any = [];
  offshoreList: any = [];
  onshore: any = []; offshore: any = [];
  private selectedLink: string = "";
  projectDataset: any = [];
  accountsList: any = [];
  dropdownSettings: {};
  demoaccountsettings: {};

  validationMessages = {
    'firstName': {
      'required': 'First Name is required.',
      'minlength': 'First Name must be greater than 2 characters.',
      'maxlength': 'First Name must be less than 25 characters.',
    },
    'lastName': {
      'required': 'Last Name is required.',
      'minlength': 'Last Name must be greater than 2 characters.',
      'maxlength': 'Last Name must be less than 25 characters.',
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
       'required': 'Rate is required.'
     },
     'vendor': {
      'required': 'Vendor name is required.',
       'minlength': 'Vendor name must be greater than 2 characters.',
       'maxlength': 'Vendor name must be less than 20 characters.',
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


  constructor(private fb: FormBuilder, private httpClient: HttpClient,
    private router: Router,
    private service: AuthManagerTmService,
    private authManager: AuthManagerTmService, private loaderService: LoaderServiceService) {
    if (!CommonMethod.authenticateSenior()) {
      this.router.navigate(['/login']);
    }
  }

  ngOnInit() {
    this.setLocalStorageValue();
    //this.setAccountAndProject();
    this.renderDropDownValue();
    this.umAddUser = this.fb.group({
      'firstName': ['', [Validators.required, Validators.minLength(2), Validators.maxLength(25)]],
      'lastName': ['', [Validators.required, Validators.minLength(2), Validators.maxLength(25)]],
      'email': ['', [Validators.required, Validators.email]],
      'empId': ['', [Validators.required, Validators.minLength(2)]],
      'billable': [''],
      'location': [this.selectedOnshore || this.selectedOffshore],
      'location2': [this.selectedOnshore || this.selectedOffshore],
      'accounts': [this.selectedAccounts],
      'projectList': [this.selectedItems],
      'role': [this.selectedRole, [Validators.required]],
      'lead': [this.selectedLead],
      /*   'rate': ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
        'vendor': ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]] */
      'rate': ['' ],
      'vendor': ['']
    });


    this.umAddUser.valueChanges.subscribe((val) => {
      this.logCredential = false;
      this.logValidationErrors(this.umAddUser);
    });
    this.renderDropDownSettings();
  }

  //**********************************************  END OF NGOnIT 

  setBillable(billStatus: string) {
    this.umAddUser.patchValue({
      'billable': billStatus
    })
  }

  setradio(e: string): void {
    this.selectedLink = e;
  }
  reset() {
    this.location = '';
    this.location2 = '';
  }
  isSelected(name: string): boolean {
    if (!this.selectedLink) {
      return false;
    }
    return (this.selectedLink === name);
  }
  //USER MANAGEMENT ADD USER SUBMISSION 
  addUserSubmission(): void {
    if ((!this.umAddUser.valid)) {
      this.logCredential = true;
    } else {
      sessionStorage.setItem('actionOnAddUpdate', 'add');
      this.authManager.addUserInUserManagement(this.umAddUser, this.selectedLink)
        .subscribe((val) => {
          this.loaderService.requestEnded();
          if (val.statusCode == 200) {
            alert('Addeed Successfully!')
            this.router.navigate(['/TM/UserManagement']);
          }
          else if (val.statusCode == 409) {
            alert('Member already exists!')
          }
        });
    }
  }


  // setAccountAndProject() {
  //   return this.service.getAccountAndProject().subscribe(val => {
  //     this.loaderService.requestEnded();
  //     this.accounts = val.valueOf();
  //     this.renderDropDownValue();
  //   });
  // }

  //SETTING DROP DOWN VALUE
  renderDropDownValue() {
    this.onshore = ["Chennai", "Bangalore US Back Office"];
    this.offshore = ["Gurgaon", "Bangalore", "Hyderabad"];
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
    let projectduplicate = [];
    for (let i = 0; i < this.projectList.length; ++i) {
      projectduplicate.push({ id: this.projectList[i], projectName: this.projectList[i] });
    };
    this.projectDataset = projectduplicate;
    if (this.managerSelected === 'User') {
      this.role = ['User'];
    }
    else if (this.managerSelected === 'Lead') {
      this.role = ['Lead'];
    }
    else if (this.managerSelected === 'Manager') {
      this.role = ['Manager'];

    }
    for (let i = 0; i < this.onshore.length; ++i) {
      this.onshoreList.push({ type: this.onshore[i], location: this.onshore[i] });
    }
    for (let i = 0; i < this.offshore.length; ++i) {
      this.offshoreList.push({ type: this.offshore[i], location: this.offshore[i] });
    }
    for (let i = 0; i < this.role.length; ++i) {
      this.roleList.push({ id: this.role[i], roleName: this.role[i] });
    }
    this.lead = JSON.parse(sessionStorage.getItem('leadCollection'));
    if (this.lead != null) {
      for (let i = 0; i < this.lead.length; ++i) {
        this.leadList.push({ id: this.lead[i].emailId, leadName: this.lead[i].firstName + " " + this.lead[i].lastName });
      }
    }
    this.selectedItems = [];
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

  setLocalStorageValue() {
    // Manager's selection for user/TL
    this.managerSelected = localStorage.getItem('tmManagerSelected');
    // Employee role
    this.empRole = localStorage.getItem('tmRole');
    // Lead and Manager default Name
    this.seniorName = localStorage.getItem('tmUserName');
  }

  //FOR SELECTED PROJECT
  onItemSelect(item: any) {
    this.selectedItems.push(item.projectName);
  }
  onItemDeselect(item: any) {
    let num = this.selectedItems.findIndex(a => a.id === item.id);
    this.selectedItems.splice(num, 1);
  }
  onSelectAll(item: any) {
    this.selectedItems = [];
    for (let i = 0; i < this.projectDataset.length; ++i) {
      this.selectedItems.push(this.projectDataset[i].projectName);
    }
  }
  onDeSelectAll(item: any) {
    this.selectedItems = [];
  }
  //for account
  onItemSelectDemoAccount(item: any) {
    this.selectedAccounts.push(item.accountName);
    this.projectDropDownOnChange(item);
  }
  projectDropDownOnChange(item) {
    for (let each of this.accounts) {
      if (each.account == item.accountName) {
        for (let each_projects of each.projects) {
          this.projectList.push(each_projects);
        }
      }
    } this.onshoreList = []; this.offshoreList = []; this.renderDropDownValue();

  }
  onItemDeSelectDemoAccount(item: any) {
    let num = this.selectedAccounts.findIndex(a => a.id === item.id);
    this.selectedAccounts.splice(num, 1);
    this.removeProjectsOnDeselectingAccount(item);
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
    }
    this.onshoreList = []; this.offshoreList = [];
    this.renderDropDownValue();

  }
  onSelectAllDemoAccount(item: any) {
    this.projectList = [];
    this.selectedAccounts = [];
    for (let each of item) {
      this.projectDropDownOnChange(each);
    }
    for (let i = 0; i < this.accounts.length; ++i) {
      this.selectedAccounts.push(this.accounts[i].account);
    }
  }
  onDeSelectAllDemoAccount(item: any) {
    this.selectedAccounts = [];
    this.projectList = [];
    this.onshoreList = []; this.offshoreList = [];
    this.renderDropDownValue();
  }
  //For SELECTED Onshore
  onItemSelectOnshore(type: any) {
    this.selectedOnshore = type.location;
  }
  onItemDeselectOnshore(type: any) {
    this.selectedOnshore = '';
  }
  //FOR SELECTED Offshore
  onItemSelectOffshore(type: any) {
    this.selectedOffshore = type.location;
  }
  onItemDeselectOffshore(type: any) {
    this.selectedOffshore = '';
  }


  //FOR SELECTED ROLE
  onItemSelectRole(item: any) {
    this.selectedRole = item.roleName;
  }
  onItemDeselectRole(item: any) {
    this.selectedRole = '';
  }

  //FOR SELECTED LEAD
  onItemSelectLead(item: any) {
    this.selectedLead = item.leadName;
  }
  onItemDeselectLead(item: any) {
    this.selectedLead = '';

  }


  //VALIDATING THE FEILD ---
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
