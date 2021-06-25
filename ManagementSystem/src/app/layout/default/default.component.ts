import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { DaoyunMenu, NbMenuItemEx } from 'src/app/routes/dashboard/dashboard-menu';
import { DYMenuService } from 'src/app/service/menu/dy-menu.service';

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
export class DefaultComponent implements OnInit, OnDestroy {
    private destroy$: Subject<void>;
    private allmenu = DaoyunMenu;
    menu = JSON.parse(JSON.stringify(DaoyunMenu));

    constructor(private dymenuService: DYMenuService) {
        this.destroy$ = new Subject();
        this.dymenuService.menuOnReady()
            .pipe(takeUntil(this.destroy$))
            .subscribe(() => {
                this.menu = JSON.parse(JSON.stringify(this.allmenu));
                this.doit(this.menu);
            });
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    ngOnInit(): void {
    }

    private doit(menuitems: NbMenuItemEx[]) {
        const removeEntries = [];

        for(let i=0;i<menuitems.length;i++) {
            const item = menuitems[i];
            if(item.children) {
                this.doit(item.children);
            }

            if (item.descriptorExpr)
            {
                if(!this.dymenuService.testDescriptorExpr(item.descriptorExpr)) {
                    removeEntries.push(i);
                }
            } else {
                if(!item.children || item.children.length == 0) {
                    removeEntries.push(i);
                }
            }
        }

        for(const i of removeEntries.reverse()) {
            menuitems.splice(i, 1);
        }
    }
}

