import { Component, OnInit } from '@angular/core';
import { NbToastrService } from '@nebular/theme';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { User } from 'src/app/entity';
import { AdminUserService } from 'src/app/service/admin-user/admin-user.service';
import { RoleService } from 'src/app/service/role/role.service';
import { httpErrorHandler } from 'src/app/shared/utils';

@Component({
    selector: 'ngx-user-add',
    templateUrl: './user-add.component.html',
    styleUrls: ['./user-add.component.scss']
})
export class UserAddComponent implements OnInit {
    private destroy$: Subject<void> = new Subject<void>();
    title: string;
    user: User;
    birthday: Date;
    password: string;
    roleList: string[];
    selectedRoleIndex: string;

    constructor(private adminUserService: AdminUserService,
                private roleService: RoleService,
                private toastrService: NbToastrService) {
        this.user = new User();
        this.user.roles = this.user.roles || [];
        this.birthday = new Date(0);
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
    }
    
    get canAdd(): boolean {
        const r = this.roleList[this.selectedRoleIndex];
        if(r == null) return false;
        const n = this.user?.roles || [];

        return n.indexOf(r) < 0;
    }

    get canDel(): boolean {
        const r = this.roleList[this.selectedRoleIndex];
        if(r == null) return false;
        const n = this.user?.roles || [];

        return n.indexOf(r) >= 0;
    }

    addRole() {
        const r = this.roleList[this.selectedRoleIndex];
        this.user.roles.push(r);
    }

    delRole() {
        const r = this.roleList[this.selectedRoleIndex];
        const n = this.user.roles.indexOf(r);
        this.user.roles.splice(n, 1);
    }

    async addUser() {
        if(this.birthday.getTime() > 0) {
            this.user.birthday = this.birthday.getTime();
        }

        this.user['password'] = this.password;
        try {
            await this.adminUserService.post(this.user);
            this.toastrService.success("创建用户成功");
            this.user = {} as any;
            this.password = '';
            this.birthday = new Date(0);
        } catch (err) {
            httpErrorHandler(err, "用户管理", "创建用户失败");
        }
    }
}

