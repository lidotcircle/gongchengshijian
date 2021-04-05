import { Component, OnInit } from '@angular/core';
import { DaoyunMenu } from './dashboard-menu';

@Component({
    selector: 'ngx-dashboard',
    template: `
    <ngx-default-layout>
        <router-outlet></router-outlet>
    </ngx-default-layout>
  `,
    styles: []
})
export class DashboardComponent implements OnInit {
    constructor() { }

    ngOnInit(): void {
    }
}

