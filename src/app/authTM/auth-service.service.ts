import { Injectable } from '@angular/core';
import { HttpClient, HttpParams ,HttpErrorResponse, HttpHeaders, HttpResponse} from '@angular/common/http';
import { catchError} from 'rxjs/operators';
import { IAllEmployee} from '../InterfaceClass/AllEmployee';
import { Observable, throwError } from 'rxjs';
import { tap } from 'rxjs/operators'; 
import { CorsBaseUrl } from '../CommonCodeVal/corsBaseURL'; 
import { LoaderServiceService } from '../Loader/loader-service.service';
import { FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {

 // baseUrl = 'http://localhost:8060/';
baseUrl= CorsBaseUrl.devURL;
 
constructor(private http: HttpClient,
            private loaderService:LoaderServiceService, ) {
   console.log(CorsBaseUrl.devURL);
 }

headers={
  headers: new HttpHeaders({
      'Content-Type': 'application/json'
  })
}

   
//Login API CALL
doLogin(email:string, password:string) : Observable<any>{
  this.loaderService.requestStarted();
  const loginVal ={
    'emailId':'',
    'password':''
  }  
  // var encrypted = this.cipher.set('123456$#@$^@1ERF', password);
  // var decrypted = this.cipher.get('123456$#@$^@1ERF', encrypted);
  loginVal.emailId=  email;
  loginVal.password= btoa(password);
  //loginVal.password= password
  //loginVal.password= this.cipher.set('TM@ics123', password);
   
  console.log(loginVal);
  return this.http.post(this.baseUrl+'TM/signin', 
  JSON.stringify(loginVal),this.headers)
  .pipe(  
       tap(
          (event)=>{
            if(event instanceof HttpResponse){
              this.loaderService.requestEnded();
              console.log("Status: "+ event.status);
            }
          },
          (error: HttpErrorResponse) =>{
            this.loaderService.requestEnded();
            console.log("error res- "+error.status );
          }
       )
       // catchError(this.handleError)
      );
  }


  //Forgot Password API CALL
  forgotPassword(emailId: string): Observable<any>{
    this.loaderService.requestStarted();
    console.log(emailId);///forgetPassword/Email/
    return this.http.post(this.baseUrl+`TM/forgetPassword/Email/${emailId}`,this.headers)
     .pipe(
       tap(
         (event)=>{
            this.loaderService.requestEnded();
         },
         (error: HttpErrorResponse) =>{
           this.loaderService.requestEnded();
           console.log("error res- "+error.status);
         }
       )
     );
  }
  
  
  //ChangePassword API CALL
  changePassword(chngPwdForm: FormGroup): Observable<any>{
    this.loaderService.requestStarted();
    console.log(`${chngPwdForm.get('oldPass').value} -*- ${chngPwdForm.get('newPass').value} -*- ${chngPwdForm.get('conPass').value}`)
    const chgPwdJson={
      "emailId": localStorage.getItem('tm@Ics#user'),
      "oldPassword": btoa(chngPwdForm.get('oldPass').value),
      "newPassword": btoa(chngPwdForm.get('newPass').value),
      "confirmPassword": btoa(chngPwdForm.get('conPass').value)
    }
    return this.http.post(this.baseUrl+'TM/changePassword', JSON.stringify(chgPwdJson),this.headers)
    .pipe(
      tap(
        (event)=>{
           this.loaderService.requestEnded();
        },
        (error: HttpErrorResponse) =>{
          this.loaderService.requestEnded();
          console.log("error res- "+error.status);
        }
      )
    );
  }


  handleError(error) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // client-side error
      errorMessage = `Error: ${error.error.message}`;
      console.log("Client Error");
    } else {
      // server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
      console.log("Server Error");
    }
    console.log(errorMessage);
    return throwError(errorMessage);
  }


getAllEmployees1() : Observable<IAllEmployee[]>{
  console.log("Inside getEmp");
  return this.http.get<IAllEmployee[]>('http://172.16.21.12:8060/TM/all')
}


}
