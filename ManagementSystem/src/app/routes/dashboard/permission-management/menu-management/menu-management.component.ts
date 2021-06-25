import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { NbToastrService, NbWindowService } from '@nebular/theme';
import { Subject } from 'rxjs';
import { concatMap, map, takeUntil, tap } from 'rxjs/operators';
import { PermMenu } from 'src/app/entity';
import { PermMenuService } from 'src/app/service/role/perm-menu.service';
import { httpErrorHandler, softAssignEnum } from 'src/app/shared/utils';
import { MenuEditorComponent } from './menu-tree/menu-editor.component';

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
                private windowService: NbWindowService,
                private toastrService: NbToastrService) {
        this.menus = [];
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
            .subscribe(tree => softAssignEnum(this.menus, tree), error => {
                httpErrorHandler(error, "菜单管理", "获取权限信息失败");
            });

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

    inAdding: boolean = false;
    async addRootEntry() {
        if(this.inAdding) return;
        this.inAdding = true;

        const win = this.windowService.open(MenuEditorComponent, {
            context: {},
            title: "添加根节点菜单",
        });

        try {
            await win.onClose.toPromise();

            if(win.config.context['isConfirmed']) {
                const menu = win.config.context['menu'] as PermMenu;
                await this.permMenuService.create(null, menu);
            }
        } catch (err) {
            httpErrorHandler(err, "菜单管理", "创建菜单失败")
        } finally {
            this.inAdding = false;
        }
    }
}

