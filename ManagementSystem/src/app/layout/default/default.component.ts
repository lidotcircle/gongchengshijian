import { Component, OnInit } from '@angular/core';
import { DaoyunMenu } from 'src/app/routes/dashboard/dashboard-menu';

@Component({
    selector: 'ngx-default-layout',
    template: `
    <ngx-one-column-layout>
        <nb-menu [items]="menu" menu></nb-menu>
        <ng-content content></ng-content>
    </ngx-one-column-layout>
    `,
    styleUrls: ['./default.component.scss']
})
export class DefaultComponent implements OnInit {
    menu = DaoyunMenu;

    constructor() { }

    ngOnInit(): void {
    }
}

