
import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ViewCell } from 'ng2-smart-table';
import { Role } from 'src/app/entity';

@Component({
    template: `
      <div class="buttons">
        <button nbButton status='info' size='small' (click)='nav()' outline>权限管理</button>
      </div>
    `,
})
export class ButtonsCellComponent implements ViewCell, OnInit {
    constructor(private router: Router, private activatedRouter: ActivatedRoute) {}

    @Input() value: string | number;
    @Input() rowData: Role;

    ngOnInit() {
    }

    async nav() {
        await this.router.navigate(['/daoyun/dashboard/permission-management/menu'], {queryParams: {
            roleName: this.rowData.roleName
        }});
    }
}

