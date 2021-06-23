import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NbAuthService, NbRequestPasswordComponent } from '@nebular/auth';
import { NbToastrService } from '@nebular/theme';
import { interval } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from 'src/app/service/auth';
import { MessageService, MessageType } from 'src/app/service/message/message.service';

@Component({
    selector: 'ngx-request-password',
    templateUrl: './request-password.component.html',
    styleUrls: ['./request-password.component.scss']
})
export class RequestPasswordComponent extends NbRequestPasswordComponent implements OnInit {
    user: {
        phone: string;
        messageCode: string;
        messageCodeToken: string;
    };

    constructor(service: NbAuthService, cd: ChangeDetectorRef, router: Router,
                private activatedRoute: ActivatedRoute,
                private toastrService: NbToastrService,
                private messageService: MessageService,
                private authService: AuthService) { 
        super(service, {}, cd, router);

        this.user = {} as any;
    }

    ngOnInit(): void {
    }

    captchaResp: string;
    requireCaptcha: boolean = false;
    messageWaiting: number = 0;
    async sendMessage() {
        try {
            this.user.messageCodeToken = await this.messageService.sendMessageTo({
                phone: this.user.phone, 
                captcha: this.captchaResp, 
                type: MessageType.reset
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
                this.toastrService.info("需要先完成验证码", "重置密码");
                this.requireCaptcha = true;
                return;
            }

            this.toastrService.danger(`获取验证码失败, ${err?.reason}`);
        }
    }
    get WaitText(): String {
        return this.messageWaiting + 'S';
    }

    async requestPass() {
        try {
            const resetToken = await this.authService.requestReset(this.user);
            this.router.navigate(['../reset-password'], {
                relativeTo: this.activatedRoute,
                queryParams: {
                    'reset-token': resetToken
                }
            });
        } catch (err) {
            this.toastrService.danger("重置密码失败: ", err?.reason || "错误");
        }
    }

    async onVerify(token: string) {
        this.captchaResp = token;
        await this.sendMessage();
    }
    async onExpired(error: string) {
        this.toastrService.warning(error, '重置密码');
    } 
    async onError(error: string) {
        this.toastrService.warning(error, '重置密码');
    }
}

