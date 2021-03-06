import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { AuthServiceService } from './authTM/auth-service.service';
import { AuthManagerTmService } from './authTM/auth-manager-tm.service';
import { HttpClientModule } from '@angular/common/http';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { CommonModule } from '@angular/common'
import { NgxDateRangePickerModule } from 'ngx-daterangepicker';
import { DataTablesModule } from 'angular-datatables';

import { AppComponent } from './app.component';
import { LoginPageComponent } from './LoginModule/login-page/login-page.component';
import { HeaderComponent } from './components/header/header.component';
import { AppRoutingModule } from './/app-routing.module';
import { ForgotPasswordComponent } from './LoginModule/forgot-password/forgot-password.component';
import { UserTimesheetComponent } from './user-timesheet/user-timesheet.component';
import { UserProfileEditComponent } from './EditModule/user-profile-edit/user-profile-edit.component';
import { UserChangePasswordComponent } from './EditModule/user-change-password/user-change-password.component';
import { UserManagementEditProfileComponent } from './EditModule/user-management-edit-profile/user-management-edit-profile.component';
import { UserManagementAddUserComponent } from './EditModule/user-management-add-user/user-management-add-user.component';
import { TmLoaderComponent } from './Loader/tm-loader/tm-loader.component';

import { BulkActionComponent } from './manager/bulk-action/bulk-action.component';
import { UserManagementComponent } from './manager/user-management/user-management.component';
import { ManagerFrameworkComponent } from './manager/manager-framework/manager-framework.component';
import { PoSummaryComponent } from './manager/po-summary/po-summary.component';
import { UserSidePanelComponent } from './components/user-side-panel/user-side-panel.component';
import { ManagerSidePanelComponent } from './components/manager-side-panel/manager-side-panel.component';
import { UserBurnRateComponent } from './manager/user-burn-rate/user-burn-rate.component';
import { AlertComponent } from './components/alert/alert.component';

import { AlertService } from './components/alert/alert.service';
import { SidePanelComponent } from './side-panel/side-panel.component';
import { ManagerTimesheetComponent } from './manager-timesheet/manager-timesheet.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    HeaderComponent,
    ForgotPasswordComponent,
    UserTimesheetComponent,
    UserProfileEditComponent,
    UserChangePasswordComponent,
    UserManagementEditProfileComponent,
    UserManagementAddUserComponent,
    TmLoaderComponent,
    BulkActionComponent,
    UserManagementComponent,
    ManagerFrameworkComponent,
    PoSummaryComponent,
    UserSidePanelComponent,
    ManagerSidePanelComponent,
    UserBurnRateComponent,
    AlertComponent,
    SidePanelComponent,
    ManagerTimesheetComponent

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    NgxDateRangePickerModule,
    NgMultiSelectDropDownModule.forRoot(),
    DataTablesModule
  ],
  providers: [AuthServiceService, AlertService,  AuthManagerTmService ],
  bootstrap: [AppComponent]
})
export class AppModule { }
