import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { PermMenu } from 'src/app/entity';
import { MenuService } from './menu.service';

@Component({
    selector: 'ngx-menu-management',
    templateUrl: './menu-management.component.html',
    styleUrls: ['./menu-management.component.scss']
})
export class MenuManagementComponent implements OnInit, OnDestroy {
    private $destroy = new Subject<void>();
    menus: PermMenu[];
    roleName: string;

    constructor(private menuservice: MenuService,
                private activatedRouter: ActivatedRoute) {
    }

    ngOnDestroy(): void {
        this.$destroy.next();
        this.$destroy.complete();
    }

    ngOnInit(): void {
        this.activatedRouter.queryParamMap
            .pipe(takeUntil(this.$destroy))
            .subscribe(params => {
                this.roleName = params.get('roleName');
            });

        this.menus = [];
        this.menuservice.menus
            .pipe(takeUntil(this.$destroy))
            .subscribe(menus => this.menus = menus);
        this.menuservice.refresh();
    }
}

