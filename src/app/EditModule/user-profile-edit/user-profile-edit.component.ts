import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, Form} from '@angular/forms';

@Component({
  selector: 'app-user-profile-edit',
  templateUrl: './user-profile-edit.component.html',
  styleUrls: ['./user-profile-edit.component.css']
})
export class UserProfileEditComponent implements OnInit {

  editProfileEmpForm: FormGroup;
  logCredential= false;
  validationMessages = {
    'firstName': {
      'required': 'firstName is required.',
      'minlength': 'firstName must be greater than 2 characters.',
      'maxlength': 'firstName must be less than 15 characters.',
    },
    'lastName': {
      'required': 'lastName is required.',
      'minlength': 'lastName must be greater than 2 characters.',
      'maxlength': 'lastName must be less than 15 characters.',
    },
    'company': {
      'required': 'company is required.',
      'minlength': 'company must be greater than 2 characters.',
      'maxlength': 'company must be less than 15 characters.',
    },
    'phone': {
      'required': 'phone is required.',
      'minlength': 'phone must be greater than 2 characters.',
      'maxlength': 'phone must be less than 15 characters.',
    },
    'address': {
      'required': 'address is required.',
      'minlength': 'address must be greater than 2 characters.',
      'maxlength': 'address must be less than 15 characters.',
    },
    'city': {
      'required': 'city is required.',
      'minlength': 'city must be greater than 2 characters.',
      'maxlength': 'city must be less than 15 characters.',
    },
    'state': {
      'required': 'state is required.',
      'minlength': 'state must be greater than 2 characters.',
      'maxlength': 'state must be less than 15 characters.',
    },
    'zipCode': {
      'required': 'zipCode is required.',
      'minlength': 'zipCode must be greater than 2 characters.',
      'maxlength': 'zipCode must be less than 15 characters.',
    },
    'country': {
      'required': 'country is required.',
      'minlength': 'country must be greater than 2 characters.',
      'maxlength': 'country must be less than 15 characters.',
    }
  };

  formErrors = {
    "firstName": "",
    "lastName": "",
    "company":"",
    "phone":"",
    "address":"",
    "city":"",
    "state":"",
    "zipCode":"",
    "country":""
  }

  constructor(private fb: FormBuilder) {  }

  ngOnInit() {
    this.editProfileEmpForm= this.fb.group({
       'firstName': ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
       'lastName' : ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
       'company'  : ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
       'phone'    : ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
       'address'  : ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
       'city'     : ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
       'state'    : ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
       'zipCode'  : ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]],
       'country'  : ['', [Validators.required, Validators.minLength(2), Validators.maxLength(15)]]
    });
    this.editProfileEmpForm.valueChanges.subscribe((val)=>{
      this.logCredential= false;
      this.logValidationErrors(this.editProfileEmpForm);
    })
  }


  logValidationErrors(group : FormGroup=this.editProfileEmpForm) : void{
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


}
