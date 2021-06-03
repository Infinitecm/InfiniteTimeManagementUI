import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthServiceService } from '../../authTM/auth-service.service';
import { ConfirmedValidator } from '../../CommonCodeVal/ConfirmedValidator';
import { LoaderServiceService } from '../../Loader/loader-service.service';
import { Router } from '@angular/router';
import { CommonMethod } from '../../CommonCodeVal/CommonMethod';

@Component({
  selector: 'app-user-change-password',
  templateUrl: './user-change-password.component.html',
  styleUrls: ['./user-change-password.component.css']
})
export class UserChangePasswordComponent implements OnInit {

  changePwdEmpForm : FormGroup;
  logCredential= false;
  confirmPwd = false;

  validationMessages = {
    'oldPass': {
      'required': 'Password is required.',
      'minlength': 'Password must be greater than 4 characters.'
    },
    'newPass': { 
      'required': 'Password is required.',
      'minlength': 'Password must be greater than 8 characters.',
      'maxlength': 'Password must be less than 25 characters.',
    },
    'conPass': {
      'required': 'Password is required.',
      'confirmedValidator': 'Password and Confirm Password must be match.'
    }
  };

  formErrors = {
    "oldPass": "",
    "newPass": "",
    "conPass": ""
  }
  constructor(private fb: FormBuilder, private authService: AuthServiceService,
              private loaderService: LoaderServiceService, private router:Router) {
                if(!CommonMethod.authenticateUser()){
                  this.router.navigate(['/login']);
                }
              }

  ngOnInit() {
    this.changePwdEmpForm = this.fb.group({
      oldPass : ['', [Validators.required, Validators.minLength(8)]],
      newPass : ['', [Validators.required, Validators.minLength(8), Validators.maxLength(25)]],
      conPass : ['', [Validators.required]]
    },{
      validator: ConfirmedValidator('newPass', 'conPass')
    });
   
    this.changePwdEmpForm.valueChanges.subscribe((val)=>{
      this.logCredential=false;
      this.logValidationErrors(this.changePwdEmpForm);
    });
    
  }


  // --- VALIDATION ERROR CONDITION CHK LOGIC
  logValidationErrors(group : FormGroup=this.changePwdEmpForm) : void{
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
  };

  updatePassword():void{ 
    console.log("HI");
    if(!this.changePwdEmpForm.get('oldPass').valid || !this.changePwdEmpForm.get('newPass').valid 
       || !this.changePwdEmpForm.get('conPass').valid){
       this.logCredential= true;
     }else{ 
       console.log("true");
       this.authService.changePassword(this.changePwdEmpForm).subscribe((val)=>{
            this.loaderService.requestEnded();
            if(val.StatusCode == 200){
              alert("Password Changed Successfully. Please Login Again!")
              this.router.navigate(['/login']);
            }
            else if(val.StatusCode === 500){
              alert('Wrong password!');
            }

        });

     }
  }

}
1