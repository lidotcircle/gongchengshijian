import { Component, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { UserData } from 'src/app/@core/data/users';

@Component({
    selector: 'ngx-user-info',
    templateUrl: './user-info.component.html',
    styleUrls: ['./user-info.component.scss']
})
export class UserInfoComponent implements OnInit {
    private destroy$: Subject<void> = new Subject<void>();
    user: {nickname: string, picture: string, name: string};
    title: string;

    constructor(private userService: UserData) {
        this.userService.getUsers()
            .pipe(takeUntil(this.destroy$))
            .subscribe((users: any) => this.user = users.zhangsan);
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    ngOnInit(): void {
        this.gotoInfoView();
    }

    inEdit: boolean = false;
    gotoInfoEdit() {
        this.title = '用户个人信息编辑 - 管理员';
        this.inEdit = true;
    }

    gotoInfoView() {
        this.title = '用户个人信息 - 管理员';
        this.inEdit = false;
    }
}

