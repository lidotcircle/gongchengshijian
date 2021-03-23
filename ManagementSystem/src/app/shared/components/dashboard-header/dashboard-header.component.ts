import { Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'ngx-dashboard-header',
    templateUrl: './dashboard-header.component.html',
    styleUrls: ['./dashboard-header.component.scss']
})
export class DashboardHeaderComponent implements OnInit {
    @Input('title')
    title: string = '到云后台管理系统';

    constructor() { }

    ngOnInit(): void {
    }
}

