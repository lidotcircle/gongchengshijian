import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NbAuthService, NbLoginComponent } from '@nebular/auth';
import { NbToastrService } from '@nebular/theme';
import { AuthService } from 'src/app/service/auth';

@Component({
    selector: 'ngx-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent extends NbLoginComponent implements OnInit {
    user: {
        userName: string;
        password: string;
        captcha: string;
    };
    enableRememberMe: boolean = true;
    rememberMe: boolean = true;

    constructor(service: NbAuthService, cd: ChangeDetectorRef, router: Router,
                private authService: AuthService,
                private toastService: NbToastrService) {
        super(service, {}, cd, router);
    }

    ngOnInit(): void {}

    async login() {
        this.errors = [];
        this.messages = [];
        this.showMessages = {};
        this.submitted = false;

        try {
            this.user['captcha'] = this.captchaToken;
            await this.authService.loginByUsername(this.user);
            await this.toastService.show("登录成功, 跳转到首页", "登录", {status: 'primary'});
            await new Promise(resolve => setTimeout(resolve, 1000));
            this.submitted = true;
            this.router.navigateByUrl('/daoyun/dashboard');

            if(!this.rememberMe) {
                this.authService.forgetLogin();
            }
        } catch (e: any) {
            let errorMsg = "未知错误, 服务不可达";
            if(e instanceof HttpErrorResponse) {
                if(e.status == 403 && e.error.reason == 'require captcha') {
                    this.toastService.info("需要先完成验证", "登录");
                    this.requireCaptcha = true;
                    return;
                }

                errorMsg = e.error.reason || errorMsg;
            }

            this.toastService.danger(errorMsg, "登录");
            this.errors.push(errorMsg);
            this.showMessages = {error: true};
        }
    }

    captchaToken: string;
    requireCaptcha: boolean;
    async onVerify(token: string) {
        console.log(token);
        this.captchaToken = token;
        await this.login();
    }
    async onExpired(error: string) {
        console.log(error);
        this.toastService.warning(error);
    } 
    async onError(error: string) {
        console.log(error);
        this.toastService.warning(error);
    }
}

