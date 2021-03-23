import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserInfoRoutingModule } from './user-info-routing.module';
import { UserInfoViewerComponent } from './user-info-viewer/user-info-viewer.component';
import { SharedModule } from '../../shared/shared.module';


@NgModule({
    declarations: [UserInfoViewerComponent],
    imports: [
        CommonModule,
        UserInfoRoutingModule,
        SharedModule
    ]
})
export class UserInfoModule { }

