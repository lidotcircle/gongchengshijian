
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ViewCell } from 'ng2-smart-table';
import { ICommonUser } from '../../../shared/utils';

@Component({
    template: `
        <button nbButton status='info' size='small' (click)='gotoUserInfo()' outline>查看</button>
    `,
})
export class ClickCellComponent implements ViewCell, OnInit {
    constructor(private router: Router, private activatedRouter: ActivatedRoute) {}

    @Input() value: string | number;
    @Input() rowData: ICommonUser;

    ngOnInit() {
    }

    async gotoUserInfo() {
        await this.router.navigateByUrl('/daoyun/dashboard/user-management/user-info');
    }
}

