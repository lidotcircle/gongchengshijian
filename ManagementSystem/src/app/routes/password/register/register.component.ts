import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NbAuthService, NbRegisterComponent } from '@nebular/auth';

@Component({
    selector: 'ngx-signup',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
})
export class RegisterComponent extends NbRegisterComponent implements OnInit {
    constructor(service: NbAuthService, cd: ChangeDetectorRef, router: Router) {
        super(service, {}, cd, router);
    }

    ngOnInit(): void {
    }
}

