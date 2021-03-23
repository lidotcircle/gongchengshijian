import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NbCardComponent } from '@nebular/theme';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { UserData } from '../../../@core/data/users';

@Component({
    selector: 'ngx-user-info-viewer',
    templateUrl: './user-info-viewer.component.html',
    styleUrls: ['./user-info-viewer.component.scss']
})
export class UserInfoViewerComponent implements OnInit, OnDestroy {
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
        this.title = '用户个人信息编辑';
        this.inEdit = true;
    }

    gotoInfoView() {
        this.title = '用户个人信息';
        this.inEdit = false;
    }
}

