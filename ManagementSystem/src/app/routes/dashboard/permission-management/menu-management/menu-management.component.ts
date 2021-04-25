import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { NbToastrService } from '@nebular/theme';
import { Subject } from 'rxjs';
import { concatMap, map, takeUntil, tap } from 'rxjs/operators';
import { PermMenu } from 'src/app/entity';
import { PermMenuService } from 'src/app/service/role/perm-menu.service';

@Component({
    selector: 'ngx-menu-management',
    templateUrl: './menu-management.component.html',
    styleUrls: ['./menu-management.component.scss']
})
export class MenuManagementComponent implements OnInit, OnDestroy {
    private $destroy = new Subject<void>();
    menus: PermMenu[];
    roleName: string;
    private refreshMe: Subject<void>;

    constructor(private activatedRouter: ActivatedRoute,
                private permMenuService: PermMenuService,
                private toastrService: NbToastrService) {
        this.refreshMe = new Subject<void>();
        this.refreshMe
            .pipe(concatMap(() => this.permMenuService.getTree()))
            .pipe(concatMap(async (tree) => {
                if(this.roleName) {
                    const entries = await this.permMenuService.getEntries(this.roleName);
                    const ds = new Set<string>();
                    for(const entry of entries) ds.add(entry.descriptor);

                    const goThroughTree = (node: PermMenu) => {
                        node.enabled = ds.has(node.descriptor);
                        for(const cnode of node.children || []) {
                            goThroughTree(cnode);
                        }
                    }
                    for(const node of tree) {
                        goThroughTree(node);
                    }
                }
                return tree;
            }))
            .subscribe(tree => this.menus = tree, error => {
                this.toastrService.danger("获取权限信息失败", "菜单管理");
            });

        this.menus = [];
        this.permMenuService.menuDuk
            .pipe(takeUntil(this.$destroy))
            .subscribe(this.refreshMe);

        this.activatedRouter.queryParamMap
            .pipe(takeUntil(this.$destroy))
            .pipe(tap(params => {
                this.roleName = params.get('roleName');
            }))
            .pipe(map(() => {return;}))
            .subscribe(this.refreshMe);
    }

    ngOnDestroy(): void {
        this.$destroy.next();
        this.$destroy.complete();
    }

    ngOnInit(): void {
    }
}

