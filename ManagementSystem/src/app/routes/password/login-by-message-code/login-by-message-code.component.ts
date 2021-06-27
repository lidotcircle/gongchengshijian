import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NbAuthService, NbLoginComponent } from '@nebular/auth';
import { NbToastrService } from '@nebular/theme';
import { Subject, interval } from 'rxjs';
import { map, takeUntil } from 'rxjs/operators';
import { AuthService } from 'src/app/service/auth';
import { MessageService, MessageType } from 'src/app/service/message/message.service';
import { httpErrorHandler } from 'src/app/shared/utils';

@Component({
    selector: 'ngx-login-by-message-code',
    templateUrl: './login-by-message-code.component.html',
    styleUrls: ['./login-by-message-code.component.scss']
})
export class LoginByMessageCodeComponent extends NbLoginComponent implements OnInit {
    user: {
        phone: string;
        messageCode: string;
        messageCodeToken: string;
    };
    enableRememberMe: boolean = true;
    rememberMe: boolean = true;

    constructor(service: NbAuthService, cd: ChangeDetectorRef, router: Router,
                private authService: AuthService,
                private messageService: MessageService,
                private toastrService: NbToastrService) {
        super(service, {}, cd, router);
    }

    ngOnInit(): void {
        this.user = {} as any;
    }

    captchaResp: string;
    requireCaptcha: boolean = false;
    messageWaiting: number = 0;
    async sendMessage() {
        try {
            this.user.messageCodeToken = await this.messageService.sendMessageTo({
                phone: this.user.phone, 
                captcha: this.captchaResp, 
                type: MessageType.login
            });
            this.messageWaiting = 5;
            const sub = interval(1000)
                .pipe(map(() => this.messageWaiting))
                .subscribe(() => {
                    this.messageWaiting--;
                    if(this.messageWaiting == 0) {
                        sub.unsubscribe();
                    }
                });
        } catch (err) {
            if(err instanceof HttpErrorResponse && err.status == 403 && err.error.reason == 'require captcha') {
                this.toastrService.info("需要先完成验证码", "登录");
                this.requireCaptcha = true;
                return;
            }

            httpErrorHandler(err, "登录", null);
        }
    }
    get WaitText(): String {
        return this.messageWaiting + 'S';
    }

    async login() {
        this.errors = [];

        try {
            await this.authService.loginByMessage(this.user);
            await this.toastrService.show("登录成功, 跳转到首页", "登录", {status: 'primary'});
            await new Promise(resolve => setTimeout(resolve, 1000));
            this.submitted = true;
            this.router.navigateByUrl('/daoyun/dashboard');

            if(!this.rememberMe) {
                this.authService.forgetLogin();
            }
        } catch (e: any) {
            const errorMsg = httpErrorHandler(e, "登录", "未知错误, 服务不可达");
            this.errors.push(errorMsg);
            this.showMessages = {error: true};
        }
    }

    async onVerify(token: string) {
        this.captchaResp = token;
        await this.sendMessage();
    }
    async onExpired(error: string) {
        this.toastrService.warning(error, '登录');
    } 
    async onError(error: string) {
        this.toastrService.warning(error, '登录');
    }
}

