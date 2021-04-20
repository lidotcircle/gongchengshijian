import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PasswordRoutingModule } from './password-routing.module';
import { LoginComponent } from './login/login.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { RegisterComponent } from './register/register.component';
import { SharedModule } from '../../shared/shared.module';
import { NbAuthModule } from '@nebular/auth';
import { RequestPasswordComponent } from './request-password/request-password.component';
import { LoginByMessageCodeComponent } from './login-by-message-code/login-by-message-code.component';
import { NgHcaptchaModule } from 'src/app/shared/ng-hcaptcha';


@NgModule({
    declarations: [LoginComponent, ResetPasswordComponent, RegisterComponent, RequestPasswordComponent, LoginByMessageCodeComponent],
    imports: [
        CommonModule,
        PasswordRoutingModule,
        SharedModule,

        NgHcaptchaModule.forRoot({
            siteKey: 'e46186ab-3584-420b-8e06-40b049517be5',
        }),

        NbAuthModule
    ]
})
export class PasswordModule { }

