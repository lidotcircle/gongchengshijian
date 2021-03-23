import { Component, OnInit } from '@angular/core';
import { DaoyunMenu } from './daoyun-menu';

@Component({
    selector: 'ngx-daoyun',
    template: `
    <ngx-default-layout>
        <router-outlet></router-outlet>
    </ngx-default-layout>
  `,
    styleUrls: ['./daoyun.component.scss']
})
export class DaoyunComponent implements OnInit {
    menu = DaoyunMenu;

    constructor() { }

    ngOnInit(): void {
    }
}

