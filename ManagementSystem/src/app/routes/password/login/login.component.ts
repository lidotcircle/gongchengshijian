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
        username: string;
        password: string;
    };

    constructor(service: NbAuthService, cd: ChangeDetectorRef, router: Router,
                private authService: AuthService,
                private toastService: NbToastrService) {
        super(service, {}, cd, router);
    }

    ngOnInit(): void {
    }

    async login() {
        this.errors = [];
        this.messages = [];
        this.showMessages = {};
        this.submitted = false;

        try {
            await this.authService.loginByUsername(this.user.username, this.user.password);
            await this.toastService.show("登录成功, 跳转到首页", "登录", {status: 'primary'});
            await new Promise(resolve => setTimeout(resolve, 1000));
            this.submitted = true;
            this.router.navigateByUrl('/daoyun/dashboard');
        } catch (e: any) {
            let errorMsg = "未知错误, 服务不可达";
            if(e instanceof HttpErrorResponse) {
                errorMsg = e.error.reason || errorMsg;
            }

            this.toastService.show(errorMsg, "登录", {status: 'danger'});
            this.errors.push(errorMsg);
            this.showMessages = {error: true};
        }
    }
}

