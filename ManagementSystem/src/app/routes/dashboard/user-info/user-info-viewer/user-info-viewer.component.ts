import { Component, ElementRef, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { NbCardComponent, NbToastrService, NbWindowService } from '@nebular/theme';
import { Subject } from 'rxjs';
import { takeUntil, filter } from 'rxjs/operators';
import { UserData } from 'src/app/@core/data/users';
import { User } from 'src/app/entity/User';
import { LocalStorageService } from 'src/app/service/storage';
import { UserService } from 'src/app/service/user/user.service';
import { ConfirmWindowComponent } from 'src/app/shared/components/confirm-window.component';
import { computeDifference, Pattern } from 'src/app/shared/utils';
import { PrivEditWindowComponent } from './priv-edit-window.component';
import { ProfilePhotoUploadComponent } from './profile-photo-upload.component';


@Component({
    selector: 'ngx-user-info-viewer',
    templateUrl: './user-info-viewer.component.html',
    styleUrls: ['./user-info-viewer.component.scss']
})
export class UserInfoViewerComponent implements OnInit, OnDestroy {
    private destroy$: Subject<void> = new Subject<void>();
    title: string;
    user: User;
    updatedUser: User;

    constructor(private userService: UserService,
                private toastService: NbToastrService,
                private windowService: NbWindowService) {
        this.user = new User();
        this.updatedUser = new User();

        this.userService.getUser()
        .pipe(takeUntil(this.destroy$))
        .subscribe(user => {
            this.user = user || new User();
            this.updatedUser = Object.create(User.prototype, Object.getOwnPropertyDescriptors(this.user));

            if(this.user.birthday != null) {
                this.birthday = new Date(this.user.birthday);
            }
        });
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    ngOnInit(): void {
        this.gotoInfoView();
    }

    inEdit: boolean = false;
    birthday: Date;
    gotoInfoEdit() {
        this.title = '用户个人信息编辑';
        this.inEdit = true;
    }

    inSavingUserinfo: boolean = false;
    async gotoInfoView() {
        this.updatedUser.birthday = this.birthday?.getTime() || this.updatedUser.birthday;
        const diff = computeDifference(this.updatedUser, this.user);

        if(diff) {
            const win = this.windowService.open(ConfirmWindowComponent, {
                title: '修改用户信息',
                context: {message: '是否保存?'}
            });
            await win.onClose.toPromise();
            const confirmed = win.config.context['isConfirmed'];

            if(confirmed) {
                try {
                    this.inSavingUserinfo = true;
                    await this.userService.updateUser(diff);
                } catch {
                    this.toastService.show("保存用户信息失败", "保存", {status: 'danger'});
                    return;
                } finally {
                    this.inSavingUserinfo = false;
                }
            } else {
                return;
            }
        }

        this.title = '用户个人信息';
        this.inEdit = false;
    }

    async editPassword() {
        const win = this.windowService.open(PrivEditWindowComponent, {
            title: '修改密码',
            context: {
                name: '新密码',
                type: 'password',
                pattern: Pattern.password.toString(),
            }
        });
        await win.onClose.toPromise();

        if(win.config.context['isConfirmed']) {
            const password = win.config.context['password'];
            const newPassword = win.config.context['value'];

            try {
                await this.userService.updateUserPrivileged(password, {password: newPassword});
                this.toastService.show('修改密码成功', '修改密码', {status: 'info'});
            } catch {
                this.toastService.show('修改密码失败', '修改密码', {status: 'danger'});
            }
        }
    }

    async editEmail() {
        const win = this.windowService.open(PrivEditWindowComponent, {
            title: '修改邮箱',
            context: {
                name: '邮箱',
                type: 'text',
                value: this.user.email,
                pattern: Pattern.email.toString(),
            }
        });
        await win.onClose.toPromise();

        if(win.config.context['isConfirmed']) {
            const password = win.config.context['password'];
            const email = win.config.context['value'];

            try {
                await this.userService.updateUserPrivileged(password, {email: email});
                this.toastService.show('修改邮箱成功', '邮箱', {status: 'info'});
            } catch {
                this.toastService.show('修改邮箱失败', '邮箱', {status: 'danger'});
            }
        }
    }

    async editPhone() {
        const win = this.windowService.open(PrivEditWindowComponent, {
            title: '修改手机号',
            context: {
                name: '手机号',
                type: 'text',
                value: this.user.phone,
                pattern: Pattern.phone.toString(),
            }
        });
        await win.onClose.toPromise();

        if(win.config.context['isConfirmed']) {
            const password = win.config.context['password'];
            const phone = win.config.context['value'];

            try {
                await this.userService.updateUserPrivileged(password, {phone: phone});
                this.toastService.show('修改手机号成功', '手机号', {status: 'info'});
            } catch {
                this.toastService.show('修改手机号失败', '手机号', {status: 'danger'});
            }
        }
    }

    async editProfilePhoto() {
        const win = this.windowService.open(ProfilePhotoUploadComponent, {
            title: '修改头像',
            context: {
                photo: this.user.photo
            }
        });
        await win.onClose.toPromise();

        if(win.config.context['isConfirmed']) {
            const photo = win.config.context['photo'];

            try {
                await this.userService.updateUser({photo: photo});
                this.toastService.show('修改头像成功', '头像', {status: 'info'});
            } catch {
                this.toastService.show('修改头像失败', '头像', {status: 'danger'});
            }
        }

    }
}

