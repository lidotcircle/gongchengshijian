import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NbAuthService, NbRegisterComponent } from '@nebular/auth';
import { NbToastrService } from '@nebular/theme';
import { interval, timer } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from 'src/app/service/auth';
import { MessageService, MessageType } from 'src/app/service/message/message.service';

@Component({
    selector: 'ngx-signup',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
})
export class RegisterComponent extends NbRegisterComponent implements OnInit {
    user: {
        userName: string;
        phone: string;
        password: string;
        messageCode: string;
        messageCodeToken: string;
    };

    confirmPassword: string;
    constructor(service: NbAuthService, cd: ChangeDetectorRef, router: Router,
                private messageService: MessageService,
                private authService: AuthService,
                private activatedRoute: ActivatedRoute,
                private toastrService: NbToastrService) {
        super(service, {}, cd, router);
    }

    ngOnInit(): void {
    }

    captchaResp: string;
    messageWaiting: number = 0;
    async sendMessage() {
        try {
            this.user.messageCodeToken = await this.messageService.sendMessageTo({
                phone: this.user.phone, 
                captcha: this.captchaResp, 
                type: MessageType.signup
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

            this.toastrService.danger(`获取验证码失败, ${err?.error?.reason}`, "注册");
        }
    }
    get WaitText(): String {
        return this.messageWaiting + 'S';
    }

    async register() {
        try {
            await this.authService.signup(this.user);
            this.toastrService.success("注册成功, 跳转到登录页", "注册");
            timer(1000).subscribe(() => {
                this.router.navigate(['../login'], {
                    relativeTo: this.activatedRoute
                });
            });
        } catch (err) {
            this.toastrService.danger(`注册失败: ${err?.error?.reason || "错误"}`, "注册");
        }
    }

    requireCaptcha: boolean = false;
    async onVerify(token: string) {
        this.captchaResp = token;
        await this.sendMessage();
    }
    async onExpired(error: string) {
        this.toastrService.warning(error, '注册');
    } 
    async onError(error: string) {
        this.toastrService.warning(error, '注册');
    }
}

