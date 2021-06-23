import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { NbToastrService, NbWindowService } from '@nebular/theme';
import { PermMenu } from 'src/app/entity';
import { PermMenuService } from 'src/app/service/role/perm-menu.service';
import { MenuEditorComponent } from './menu-editor.component';
import { MenuRoleListComponent } from './menu-role-list.component';

@Component({
    selector: 'ngx-menu-tree',
    templateUrl: './menu-tree.component.html',
    styleUrls: ['./menu-tree.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class MenuTreeComponent implements OnInit {
    @Input()
    menu: PermMenu;
    @Input()
    roleName: string;
    loadingStatus = {
        enableIsLoading: false,
        disableIsLoading: false,
        enableRecurIsLoading: false,
        disableRecurIsLoading: false,

        roleListIsLoading: false,
        deleteIsLoading: false,
    }

    icon: string = 'menu-outline';
    show: boolean = false;

    constructor(private windowService: NbWindowService,
                private permMenuService: PermMenuService,
                private toastrService: NbToastrService) {}

    ngOnInit(): void {
        if(this.menu?.entryType == 'page') {
            this.icon = 'book-open-outline';
        }
        if(this.menu?.entryType == 'button') {
            this.icon = 'radio-button-on-outline';
        }
    }

    get hasChild(): boolean {return this.menu?.children && this.menu?.children.length > 0;}

    async openSubmenu() {
        this.show = true;
    }

    async closeSubmenu() {
        this.show = false;
    }

    async showRoleList() {
        if(this.loadingStatus.roleListIsLoading) return;

        await this.windowService.open(
            MenuRoleListComponent, {
                title: `角色列表`,
                context: { menu: this.menu },
            }).onClose.toPromise();
    }

    async enablePerm() //{
    {
        if(this.loadingStatus.enableIsLoading) return;
        this.loadingStatus.enableIsLoading = true;
        try {
            await this.permMenuService.enable(this.roleName, this.menu.descriptor);
            this.menu.enable();
        } catch {
            this.toastrService.danger("开启失败", "菜单权限");
        } finally {
            this.loadingStatus.enableIsLoading = false;
        }
    } //}

    async disablePerm() //{
    {
        if(this.loadingStatus.disableIsLoading) return;
        this.loadingStatus.disableIsLoading = true;
        try {
            await this.permMenuService.disable(this.roleName, this.menu.descriptor);
            this.menu.disable();
        } catch {
            this.toastrService.danger("关闭失败", "菜单权限");
        } finally {
            this.loadingStatus.disableIsLoading = false;
        }
    } //}

    async enableRecurPerm() //{
    {
        if(this.loadingStatus.enableRecurIsLoading) return;
        this.loadingStatus.enableRecurIsLoading = true;
        try {
            await this.permMenuService.enable(this.roleName, this.menu.descriptor, true);
            this.menu.enable(true);
        } catch {
            this.toastrService.danger("开启失败", "菜单权限");
        } finally {
            this.loadingStatus.enableRecurIsLoading = false;
        }
    } //}

    async disableRecurPerm() //{
    {
        if(this.loadingStatus.disableRecurIsLoading) return;
        this.loadingStatus.disableRecurIsLoading = true;
        try {
            await this.permMenuService.disable(this.roleName, this.menu.descriptor, true);
            this.menu.disable(true);
        } catch {
            this.toastrService.danger("开启失败", "菜单权限");
        } finally {
            this.loadingStatus.disableRecurIsLoading = false;
        }
    } //}

    async addChildEntry() {
        const win = this.windowService.open(MenuEditorComponent, {
            context: {},
            title: "添加子菜单",
        });

        await win.onClose.toPromise();

        if(win.config.context['isConfirmed']) {
            const menu = win.config.context['menu'] as PermMenu;
            menu.descriptor = this.menu.descriptor + '.' + menu.descriptor;
            try {
                await this.permMenuService.create(this.menu.descriptor, menu);
            } catch {
                this.toastrService.danger("创建菜单失败", "菜单管理")
            }
        }
    }

    async editPermEntry() {
        const menu = Object.create(PermMenu.prototype, Object.getOwnPropertyDescriptors(this.menu)) as PermMenu;
        const win = this.windowService.open(MenuEditorComponent, {
            context: {menu: menu},
            title: "编辑菜单",
        });

        await win.onClose.toPromise();

        if(win.config.context['isConfirmed']) {
            try {
                await this.permMenuService.update(menu);
            } catch {
                this.toastrService.danger("编辑菜单失败", "菜单管理")
            }
        }
    }
}

