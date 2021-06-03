import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthServiceService } from '../../authTM/auth-service.service';
import { IAllEmployee } from '../../InterfaceClass/AllEmployee';
import { LoaderServiceService } from '../../Loader/loader-service.service';
import { CommonMethod } from 'src/app/CommonCodeVal/CommonMethod';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['../LoginModule.css']
})
export class LoginPageComponent implements OnInit {
  
  loginEmpForm : FormGroup;
  logCredential= false;
  employeeData: IAllEmployee = new IAllEmployee();

  validationMessages = {
    'emailId': {
      'required': 'Email is required',
      'email': 'Email is not Valid'
    },
    'password': {
      'required': 'Password is required.',
      'minlength': 'Password must be greater than 8 characters.',
      'maxlength': 'Password must be less than 20 characters.',
    }
  };

  formErrors = {
    "emailId": "",
    "password": ""
  }

  public employeesTestApi=[];
  constructor(
      private router: Router, 
      private fb: FormBuilder,
      private authService: AuthServiceService,
      private loaderService: LoaderServiceService) {  }

  ngOnInit() {
    this.loginEmpForm = this.fb.group({
      emailId : ['',[Validators.required, Validators.email]],
      password  : ['',[Validators.required, Validators.minLength(8), Validators.maxLength(20)]]
    });

    //clearing the LocalStorage
     CommonMethod.clearLocalStorage();

    this.loginEmpForm.valueChanges.subscribe((val)=>{
      this.logCredential=false;
      this.logValidationErrors(this.loginEmpForm);
    });
    console.log(localStorage.getItem('tm@Ics#user'));

  }

  logValidationErrors(group : FormGroup=this.loginEmpForm) : void{
    Object.keys(group.controls).forEach((key : string) =>{
      const abstractControl = group.get(key);
      if(abstractControl instanceof FormGroup){
        this.logValidationErrors(abstractControl);
      }
      else{
        //abstractControl.disable();
        this.formErrors[key]= '';
        if(abstractControl && !abstractControl.valid 
             && (abstractControl.dirty || abstractControl.touched)){
          const messages =  this.validationMessages[key];
          
          for(const errorKey in abstractControl.errors){
            if(errorKey){
              this.formErrors[key]+= messages[errorKey] +" ";
            }
          }
        }

      }
    })
  }

  public empDet='';
  //Submit button Clicked
  onSubmit():void{
      console.log(this.loginEmpForm.value)
      this.formErrors['password']='';
      this.formErrors['emailId']='';
      if(!this.loginEmpForm.get('emailId').valid || !this.loginEmpForm.get('password').valid){
       this.logCredential = true;
      }else{ 
              this.authService.doLogin( this.loginEmpForm.get('emailId').value ,
              this.loginEmpForm.get('password').value).subscribe((val)=>{
                this.loaderService.requestEnded();
                
                if(val.statusCode == 200){
                  localStorage.setItem('tmRole',val.role);
                  localStorage.setItem('tmUserName', val.firstName+" "+val.lastName);
                  localStorage.setItem('tm@Ics#user',val.emailId);
                  localStorage.setItem('tmLocation',val.location);
                  if(val.role == 'User'){
                     this.router.navigate(['/userTimesheet']);
                  }else{
                    localStorage.setItem('accountInfo', JSON.stringify(val.accountInfo));
                    this.router.navigate(['/TM']);   
                  }
                }
                else{
                  this.logCredential = true;
                   return true;
                }
              });
       }

  }


}
