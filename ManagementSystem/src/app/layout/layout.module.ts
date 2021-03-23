import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DefaultComponent } from './default/default.component';
import { PasswordComponent } from './password/password.component';
import { SharedModule } from '../shared/shared.module';
import { NbMenuModule } from '@nebular/theme';
import { RouterModule } from '@angular/router';



@NgModule({
    declarations: [DefaultComponent, PasswordComponent],
    imports: [
        CommonModule,
        SharedModule,
        NbMenuModule,
        RouterModule,
    ],
    exports: [
        DefaultComponent, PasswordComponent
    ]
})
export class LayoutModule { }

