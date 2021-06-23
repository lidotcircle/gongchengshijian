
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ViewCell } from 'ng2-smart-table';
import { User } from 'src/app/entity/User';

@Component({
    template: `
        <button nbButton status='info' size='small' (click)='gotoUserInfo()' outline>查看</button>
    `,
})
export class ClickCellComponent implements ViewCell, OnInit {
    constructor(private router: Router, private activatedRouter: ActivatedRoute) {}

    @Input() value: string | number;
    @Input() rowData: User;

    ngOnInit() {
    }

    async gotoUserInfo() {
        await this.router.navigate(['../user-info'], {
            relativeTo: this.activatedRouter,
            queryParams: {
                userName: this.rowData.userName,
            }
        });
    }
}

