import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators} from '@angular/forms';
import { AuthServiceService } from '../../authTM/auth-service.service';
import { LoaderServiceService } from '../../Loader/loader-service.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['../LoginModule.css']
})
export class ForgotPasswordComponent implements OnInit {
  forgotEmpForm: FormGroup;
  forgotMailId = false;

  validationMessages ={
    'empMail':{
       'required': 'Email is required',
       'email': 'Email is not Valid'
    }
  };

  formErrors ={
    empMail: ''
  }

  constructor(private fb: FormBuilder, private router: Router,
              private authService: AuthServiceService, private loaderService: LoaderServiceService) { }
  
  ngOnInit() {
    this.forgotEmpForm = this.fb.group({
     empMail : ['',[Validators.required, Validators.email]]
    });

    this.forgotEmpForm.valueChanges.subscribe((val)=>{
      this.forgotMailId=false;
       this.logValidationErrors(this.forgotEmpForm);
    });
  }


  logValidationErrors(group : FormGroup=this.forgotEmpForm) : void{
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

 
  getPassword():void{
    this.formErrors['empMail']='';
    if(!this.forgotEmpForm.get('empMail').valid){
       this.forgotMailId=true;
    }else{
      this.authService.forgotPassword(this.forgotEmpForm.get('empMail').value).subscribe((val)=>{
        this.loaderService.requestEnded();
        if(val.StatusCode === 200){
          alert('Password is Reset. Please Check your Mail!');
          this.router.navigate(['/login']);
        }
        else if(val.StatusCode === 500){
          alert('User doest not exist. Please give Correct Email ID!');
        }
        
        
        
      });
      
    }
    console.log("Triggered!!");
  }

}
