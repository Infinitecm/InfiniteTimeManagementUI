import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginPageComponent} from './LoginModule/login-page/login-page.component';
import {ForgotPasswordComponent} from './LoginModule/forgot-password/forgot-password.component';
import {UserTimesheetComponent} from './user-timesheet/user-timesheet.component';
import {UserProfileEditComponent} from './EditModule/user-profile-edit/user-profile-edit.component';
import {UserChangePasswordComponent} from './EditModule/user-change-password/user-change-password.component';
import {UserManagementAddUserComponent } from './EditModule/user-management-add-user/user-management-add-user.component';
import {UserManagementEditProfileComponent } from './EditModule/user-management-edit-profile/user-management-edit-profile.component';

import { BulkActionComponent } from './manager/bulk-action/bulk-action.component';
import { UserManagementComponent } from './manager/user-management/user-management.component';
import { ManagerFrameworkComponent } from './manager/manager-framework/manager-framework.component';
import { PoSummaryComponent } from './manager/po-summary/po-summary.component';
import { UserBurnRateComponent } from './manager/user-burn-rate/user-burn-rate.component';


const appRoutes : Routes =[
  {path : 'login' ,  component: LoginPageComponent},
  {path : 'forgotPassword' , component: ForgotPasswordComponent},
  {path : 'userTimesheet'  , component: UserTimesheetComponent},
  {path : 'userEditProfile'  , component: UserProfileEditComponent},
  {path : 'userChangePassword' , component: UserChangePasswordComponent},
  {
    path : 'TM' ,
    component: ManagerFrameworkComponent,
    children:[
      {path: '', redirectTo:'UserManagement', pathMatch:'full'},
      {path: 'UserManagement', component: UserManagementComponent},
      {path: 'PoSummay', component: PoSummaryComponent},
      {path: 'BulkAction', component: BulkActionComponent},
      {path: 'UserBurnRate', component: UserBurnRateComponent}
    ]
  },
  // {path : 'managerTimesheet' , component: ManagerTimesheetComponent},
  {path : 'umAddUser' , component: UserManagementAddUserComponent},
  {path : 'umEditUser', component: UserManagementEditProfileComponent},
  {path : '' , redirectTo:'/login', pathMatch:'full'},
  {path: '**', redirectTo:'/login', pathMatch:'full'}
]


@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes, { useHash: true })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
