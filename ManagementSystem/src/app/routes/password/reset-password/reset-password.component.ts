import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NbAuthService, NbRequestPasswordComponent } from '@nebular/auth';

@Component({
    selector: 'ngx-reset-password',
    templateUrl: './reset-password.component.html',
    styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent extends NbRequestPasswordComponent implements OnInit {
    constructor(service: NbAuthService, cd: ChangeDetectorRef, router: Router) {
        super(service, {}, cd, router);
    }

    ngOnInit(): void {
    }

    resetPass() {}
}

