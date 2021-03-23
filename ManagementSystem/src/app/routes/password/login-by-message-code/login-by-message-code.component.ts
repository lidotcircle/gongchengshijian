import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NbAuthService, NbLoginComponent } from '@nebular/auth';

@Component({
    selector: 'ngx-login-by-message-code',
    templateUrl: './login-by-message-code.component.html',
    styleUrls: ['./login-by-message-code.component.scss']
})
export class LoginByMessageCodeComponent extends NbLoginComponent implements OnInit {

    constructor(service: NbAuthService, cd: ChangeDetectorRef, router: Router) {
        super(service, {}, cd, router);
    }

    ngOnInit(): void {
    }

}

