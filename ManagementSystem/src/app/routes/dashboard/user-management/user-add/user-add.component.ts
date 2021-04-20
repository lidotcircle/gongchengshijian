import { Component, OnInit } from '@angular/core';
import { NbToastrService } from '@nebular/theme';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { User } from 'src/app/entity';
import { AdminUserService } from 'src/app/service/admin-user/admin-user.service';

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

    constructor(private adminUserService: AdminUserService,
                private toastrService: NbToastrService) {
        this.user = new User();
        this.birthday = new Date(0);
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    ngOnInit(): void {
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
        } catch {
            this.toastrService.danger("创建用户失败");
        }
    }
}

