import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs';


@Injectable()
export class AlertService {
  private subject = new Subject();
  private keepAfterRouteChange = false;
  private subject2 = new Subject();
  private keepAfterRouteChange2 = false;
  private subject3 = new Subject();
  private keepAfterRouteChange3 = false;

  constructor(private router: Router) { }

    getAlert(): Observable<any> {
      return this.subject.asObservable();
    }
    alert(type, message: string, keepAfterRouteChange = false) {
        this.keepAfterRouteChange = keepAfterRouteChange;
        this.subject.next({ type: type, message: message });
    }
    getAlert2(): Observable<any> {
      return this.subject2.asObservable();
    }
    alert2(type, message: string, keepAfterRouteChange2 = false) {
        this.keepAfterRouteChange2 = keepAfterRouteChange2;
        this.subject2.next({ type: type, message: message });
    }
    getAlert3(): Observable<any> {
      return this.subject3.asObservable();
    }
    alert3(type, message: string, keepAfterRouteChange3 = false) {
        this.keepAfterRouteChange3 = keepAfterRouteChange3;
        this.subject3.next({ type: type, message: message });
    }
}