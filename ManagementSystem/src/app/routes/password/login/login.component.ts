import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NbAuthService, NbLoginComponent } from '@nebular/auth';

@Component({
    selector: 'ngx-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent extends NbLoginComponent implements OnInit {

    constructor(service: NbAuthService, cd: ChangeDetectorRef, router: Router) {
        super(service, {}, cd, router);
    }

    ngOnInit(): void {
    }

    login() {
        this.router.navigateByUrl('/daoyun/dashboard');
    }
}

