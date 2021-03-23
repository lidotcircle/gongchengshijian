import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NbAuthService, NbRequestPasswordComponent } from '@nebular/auth';

@Component({
    selector: 'ngx-request-password',
    templateUrl: './request-password.component.html',
    styleUrls: ['./request-password.component.scss']
})
export class RequestPasswordComponent extends NbRequestPasswordComponent implements OnInit {

    constructor(service: NbAuthService, cd: ChangeDetectorRef, router: Router) { 
        super(service, {}, cd, router);
    }

    ngOnInit(): void {
    }

    requestPass() {}
}

