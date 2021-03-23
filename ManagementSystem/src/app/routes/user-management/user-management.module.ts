import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserManagementRoutingModule } from './user-management-routing.module';
import { SharedModule } from '../../shared/shared.module';
import { UserListComponent } from './user-list/user-list.component';
import { UserInfoComponent } from './user-info/user-info.component';
import { UserAddComponent } from './user-add/user-add.component';
import { ClickCellComponent } from './user-list/click-cell.component';


@NgModule({
    declarations: [UserListComponent, UserInfoComponent, UserAddComponent, ClickCellComponent],
    imports: [
        CommonModule,
        SharedModule,
        UserManagementRoutingModule
    ]
})
export class UserManagementModule { }
