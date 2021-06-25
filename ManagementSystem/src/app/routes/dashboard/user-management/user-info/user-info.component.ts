import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NbToastrService, NbWindowService } from '@nebular/theme';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { User } from 'src/app/entity';
import { AdminUserService } from 'src/app/service/admin-user/admin-user.service';
import { RoleService } from 'src/app/service/role/role.service';
import { ConfirmWindowComponent } from 'src/app/shared/components/confirm-window.component';
import { computeDifference, httpErrorHandler } from 'src/app/shared/utils';

@Component({
    selector: 'ngx-user-info',
    templateUrl: './user-info.component.html',
    styleUrls: ['./user-info.component.scss']
})
export class UserInfoComponent implements OnInit {
    private destroy$: Subject<void> = new Subject<void>();
    user: User;
    updatedUser: User;
    password: string;
    title: string;
    roleList: string[];
    selectedRoleIndex: string;

    constructor(private adminUserService: AdminUserService,
                private roleService: RoleService,
                private activatedRoute: ActivatedRoute,
                private windowService: NbWindowService,
                private toastrService: NbToastrService) {
        this.user = {} as any;
        this.updatedUser = {} as any;
        this.roleList = [];
        this.selectedRoleIndex = "0";
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    ngOnInit(): void {
        this.roleService.getList()
            .then(roleList => this.roleList = (roleList || []).map(r => r.roleName))
            .catch(e => httpErrorHandler(e, "用户管理", "获取角色列表失败"));

        this.gotoInfoView();

        this.activatedRoute.queryParamMap.subscribe(async (params) => {
            const userName = params.get("userName");
            try {
                this.user = await this.adminUserService.get(userName);
                if(this.user.birthday != null) {
                    this.birthday = new Date(this.user.birthday);
                }
                await this.cancelAndGotoInfoView();
            } catch (err) {
                httpErrorHandler(err, "用户管理", "加载用户信息失败");
            }
        });
    }
    
    get canAdd(): boolean {
        const r = this.roleList[this.selectedRoleIndex];
        if(r == null) return false;
        const n = this.updatedUser?.roles || [];

        return n.indexOf(r) < 0;
    }

    get canDel(): boolean {
        const r = this.roleList[this.selectedRoleIndex];
        if(r == null) return false;
        const n = this.updatedUser?.roles || [];

        return n.indexOf(r) >= 0;
    }

    addRole() {
        const r = this.roleList[this.selectedRoleIndex];
        this.updatedUser.roles.push(r);
    }

    delRole() {
        const r = this.roleList[this.selectedRoleIndex];
        const n = this.updatedUser.roles.indexOf(r);
        this.updatedUser.roles.splice(n, 1);
    }

    inEdit: boolean = false;
    async gotoInfoEdit() {
        this.title = '用户个人信息编辑 - 管理员';
        this.inEdit = true;
    }

    inSavingUserinfo: boolean = false;
    birthday: Date;
    async gotoInfoView() //{
    {
        this.updatedUser.birthday = this.birthday?.getTime() || this.updatedUser.birthday;
        const diff = computeDifference(this.updatedUser, this.user) as User;

        if(diff) {
            diff.userName = this.user.userName;
            const win = this.windowService.open(ConfirmWindowComponent, {
                title: '修改用户信息',
                context: {message: '是否保存?'}
            });
            await win.onClose.toPromise();
            const confirmed = win.config.context['isConfirmed'];

            if(confirmed) {
                try {
                    this.inSavingUserinfo = true;
                    await this.adminUserService.put(diff as any);
                    this.user = Object.create(User.prototype, Object.getOwnPropertyDescriptors(this.updatedUser));
                } catch (err) {
                    httpErrorHandler(err, "用户管理", "保存用户信息失败");
                    return;
                } finally {
                    this.inSavingUserinfo = false;
                }
            } else {
                return;
            }
        }

        this.title = '用户个人信息 - 管理员';
        this.inEdit = false;
    } //}

    async cancelAndGotoInfoView() {
        this.updatedUser = Object.create(User.prototype, Object.getOwnPropertyDescriptors(this.user));
        this.updatedUser.roles = this.user.roles.slice();
        this.title = '用户个人信息 - 管理员';
        this.inEdit = false;
    }
}

