import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NbAuthService, NbRequestPasswordComponent } from '@nebular/auth';
import { NbToastrService } from '@nebular/theme';
import { timer } from 'rxjs';
import { AuthService } from 'src/app/service/auth';
import { httpErrorHandler } from 'src/app/shared/utils';

@Component({
    selector: 'ngx-reset-password',
    templateUrl: './reset-password.component.html',
    styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent extends NbRequestPasswordComponent implements OnInit {
    user: {
        password: string;
        resetToken: string;
    };
    confirmPassword: string;

    constructor(service: NbAuthService, cd: ChangeDetectorRef, router: Router,
                private activatedRoute: ActivatedRoute,
                private authService: AuthService,
                private toastrService: NbToastrService) {
        super(service, {}, cd, router);

        this.user = {} as any;
    }

    ngOnInit(): void {
        this.activatedRoute.queryParamMap.subscribe(params => {
            this.user.resetToken = params.get('reset-token');
        });
    }

    async resetPass() {
        try {
            if(this.user.resetToken == null) {
                throw new Error("非法页面");
            }
            await this.authService.reset(this.user);
            this.toastrService.success("重置密码成功, 前往登录页");
            timer(1000).subscribe(() => this.router.navigate(["../login"], {
                relativeTo: this.activatedRoute
            }));
        } catch (err) {
            httpErrorHandler(err, "重置密码", "重置密码失败");
        }
    }
}

