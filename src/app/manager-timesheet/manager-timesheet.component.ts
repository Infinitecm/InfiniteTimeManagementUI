import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonMethod} from '../CommonCodeVal/CommonMethod';
import { AuthManagerTmService } from '../authTM/auth-manager-tm.service';
import { LoaderServiceService } from '../Loader/loader-service.service';

@Component({
  selector: 'app-manager-timesheet',
  templateUrl: './manager-timesheet.component.html',
  styleUrls: ['./manager-timesheet.component.css']
})
export class ManagerTimesheetComponent implements OnInit {

  managerName: string ='';
  leadName: string ='';
  role:string='';
  email:string='';
  managerList:any = [];
  leadList : any = [];
  userList : any = [];
  selectedLead: string ='';
  selectedManager:string ='akash.barnwal@infinite.com';
  test:any;
  selectedItem = 0;



  constructor(private router: Router, 
              private authManager: AuthManagerTmService,
              private loaderService: LoaderServiceService) {
    if(!CommonMethod.authenticateUser()){
      this.router.navigate(['/login']);
    }
  }

   
  
  ngOnInit() {
    this.setLocalStorageValue();
    this.loadUserDetailHierarchy();
    

    console.log("Login as - "+localStorage.getItem('tm@Ics#user'));
    console.log(this.role);
  }

  
  // ---  ASSIGNING LOCAL_STORAGE VALUE
  setLocalStorageValue(){
    this.email= localStorage.getItem('tm@Ics#user');
    this.role = localStorage.getItem('tmRole');
    if(this.role == 'lead'){
      this.leadName = localStorage.getItem('tmUserName');
     }
    if(this.role == 'manager'){
      this.managerName = localStorage.getItem('tmUserName'); 
    }
  }


  //ADD PROFILE
  addThisEmployee(role:string){
    if(role === 'Manager'){
      localStorage.setItem('tmManagerSelected','Manager');
    }
    else if(role === 'Lead'){
      localStorage.setItem('tmManagerSelected','Lead');
    }
    else if(role === 'User'){
      localStorage.setItem('tmManagerSelected','User');
    }
    this.router.navigate(['/umAddUser']);
  }
    // //MANAGER ADDING LEAD
    // addLead(){
    //   localStorage.setItem('tmManagerSelected','Lead');
    //   console.log("Manager Selected - "+ localStorage.getItem('tmManagerSelected'));
    //   this.router.navigate(['/umAddUser'])
    //  }
   
    //  //MANAGER ADDING USER
    //  addMember(){
    //   localStorage.setItem('tmManagerSelected','User');
    //   console.log("Manager Selected - "+ localStorage.getItem('tmManagerSelected'));
    //   this.router.navigate(['/umAddUser'])
    //  }





  //EDIT PROFILE
  editThisRole(emailId:string, role:string){
    console.log(role+" -- "+emailId);
    if(role === 'Manager'){     
      console.log(this.managerList);
      let userIndex= this.managerList.findIndex(a=> a.emailId === emailId);
      console.log(userIndex);
      console.log(this.managerList[userIndex]);
      sessionStorage.setItem('editProfile',JSON.stringify(this.managerList[userIndex]));
      this.router.navigate(['/umEditUser']);
    }
    else if(role === 'Lead'){
      localStorage.setItem('tmManagerSelected','Lead');
      console.log(this.leadList);
      let userIndex= this.leadList.findIndex(a=> a.emailId === emailId);
      console.log(this.leadList[userIndex]);
      sessionStorage.setItem('editProfile',JSON.stringify(this.leadList[userIndex]));
      this.router.navigate(['/umEditUser']);
    }
    else if(role === 'User'){
      localStorage.setItem('tmManagerSelected','User');
      if(this.userList.length >0){

        console.log(this.userList);
        let userIndex= this.userList.findIndex(a=> a.emailId === emailId);
        console.log(this.userList[userIndex]);
        sessionStorage.setItem('editProfile',JSON.stringify(this.userList[userIndex]));
        this.router.navigate(['/umEditUser']);
      }
    }
  }


 // - LOADING USER DATA HIERARCHY ON DEFAULT LOAD
  loadUserDetailHierarchy(){
    this.authManager.loadUserDetailBYDefault(this.email, this.role).subscribe((val)=>{
      console.log(val);
      this.loaderService.requestEnded();
      if(val.status === '409'){
        alert(val.statusCode);
      }
      else if(val.status === '200'){

        this.managerList= val.managerList;
        this.leadList= val.tlList;
        this.userList= val.memberList;
        if(this.managerList.length>0){
          this.selected_Lead_Manager(this.managerList[0].emailId, this.managerList[0].role);
        }
        
       // this.selected_Lead_Manager(this.leadList[0].emailId, this.leadList[0].role);
        if(this.role === 'Lead'){
          sessionStorage.setItem('leads_Manager',this.leadList[0].manager);
        }
        sessionStorage.setItem('leadCollection',JSON.stringify(this.leadList));
      }
    });
  }



  //SHOW USERS ON SELECTED LEAD
  selected_Lead_Manager(email: string, role: string){
    if(role === 'Lead'){
      console.log("Lead selected: "+email);
      this.selectedLead=email;
    }
    else if(role === 'Manager'){
       this.selectedManager=email;
       let firstLead= this.leadList.findIndex(a=>a.manager === this.selectedManager);
       if(this.leadList.length>0){
        console.log(this.leadList[firstLead].emailId);
        this.selectedLead= this.leadList[firstLead].emailId;
       }
      console.log(this.selectedLead);
    }
  }


  routeToUserTimesheet(email:string, role:string){
    console.log(email+"- -"+role)
    this.router.navigate(['/userTimesheet'],{queryParams:{mail:email,role:role}});
    // this.router.navigate(['/userTimesheet',email]);
  }

}

    // this.authManager.loadHierarchyManagerLeadByDefault().subscribe((val)=>{
    //      console.log(val);
    //      for(let i=0; i<val.length; ++i){
    //        this.managerList.push(val[i]);
    //        for(let j=0; j<val[i].teams.length; ++j){
    //          this.leadList.push(val[i].teams[j]);
    //          console.log(val[i].teams[j].emailId);
    //          for(let k=0; k<val[i].teams[j].members.length; ++k){
    //           this.userList.push(val[i].teams[j].members[k]); 
    //          }
    //        }
    //      }
    //      this.selectedLeadForUser(val[0].teams[0].emailId);
    // });