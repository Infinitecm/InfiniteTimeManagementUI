import { Component, OnInit } from "@angular/core";

import { AlertService } from "./alert.service";

// AngularInteg\src\app\alert\alert.service.ts


@Component({
  selector: "alert",
  templateUrl: "alert.component.html"
})
export class AlertComponent {
  alerts = []; alerts2 = [];  alerts3 = [];
  length; length2;   length3;
  //loggedRole: string;
  loggedRole: string = localStorage.getItem('tmRole');
  Username : string =  localStorage.getItem('tmUserName');
  constructor(private alertService: AlertService) {}

  ngOnInit() {
    this.alertService.getAlert().subscribe(alert => {
      if (!alert) {
        this.alerts = [];
        this.length = this.alerts.length;
        return;
      }
      this.alerts.push(alert);
      this.length = this.alerts.length;
    });

    this.alertService.getAlert2().subscribe(alert2 => {
      if (!alert2) {
        this.alerts2 = [];
        this.length2 = this.alerts2.length;
        return;
      }
      this.alerts2.push(alert2);
      this.length2 = this.alerts2.length;
    });

    this.alertService.getAlert3().subscribe(alert3 => {
      if (!alert3) {
        this.alerts3 = [];
        this.length3 = this.alerts3.length;
        return;
      }
      this.alerts3.push(alert3);
      this.length3 = this.alerts3.length;
    });
  } // end of ngOnInit

  removeAlert(alert) {
    this.alerts = this.alerts.filter(x => x !== alert);
    this.length = this.alerts.length;
  }
 

  cssClass(alert) {
    if (!alert) {
      return;
    }
  switch (alert.type) {
      case "success":
        return "alert-success show";
    }
  }

  removeAlert2(alert2) {
    this.alerts2 = this.alerts2.filter(x => x !== alert2);
    this.length2 = this.alerts2.length;
  }
 

  cssClass2(alert2) {
    if (!alert2) {
      return;
    }
  switch (alert2.type) {
      case "success":
        return "alert-success show";
    }
  }

  removeAlert3(alert3) {
    this.alerts3 = this.alerts3.filter(x => x !== alert3);
    this.length3 = this.alerts3.length;
  }
 

  cssClass3(alert3) {
    if (!alert3) {
      return;
    }
  switch (alert3.type) {
      case "success":
        return "alert-success show";
    }
  }

}
