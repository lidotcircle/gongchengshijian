import { Component, OnInit } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { UserData } from 'src/app/@core/data/users';

@Component({
    selector: 'ngx-user-add',
    templateUrl: './user-add.component.html',
    styleUrls: ['./user-add.component.scss']
})
export class UserAddComponent implements OnInit {
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
    }

    async addUser() {
    }
}
