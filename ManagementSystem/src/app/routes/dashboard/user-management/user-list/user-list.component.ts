import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { NbToastrService, NbWindowService } from '@nebular/theme';
import { LocalDataSource, ServerDataSource } from 'ng2-smart-table';
import { User } from 'src/app/entity';
import { AdminUserService } from 'src/app/service/admin-user/admin-user.service';
import { RESTfulAPI } from 'src/app/service/restful';
import { ConfirmWindowComponent } from 'src/app/shared/components/confirm-window.component';
import { OptionalCellComponent } from 'src/app/shared/components/optional-cell.component';
import { ClickCellComponent } from './click-cell.component';

@Component({
    selector: 'ngx-user-list',
    templateUrl: './user-list.component.html',
    styleUrls: ['./user-list.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class UserListComponent implements OnInit {
    // settings //{
    settings = {
        hideSubHeader: true,

        actions: {
            columnTitle: '操作',
            add: false,
            edit: false,
            delete: true,
            position: 'right',
        },
        noDataMessage: '找不到用户',
        add: {
            addButtonContent: '<i class="nb-plus"></i>',
            createButtonContent: '<i class="nb-checkmark"></i>',
            cancelButtonContent: '<i class="nb-close"></i>',
            confirmCreate: true,
        },
        edit: {
            editButtonContent: '<i class="nb-edit"></i>',
            saveButtonContent: '<i class="nb-checkmark"></i>',
            cancelButtonContent: '<i class="nb-close"></i>',
            confirmSave: true,
        },
        delete: {
            deleteButtonContent: '<i class="nb-trash"></i>',
            confirmDelete: true,
        },
        columns: {
            clickColumn: {
                width: '4em',
                editable: false,
                type: 'custom',
                renderComponent: ClickCellComponent,
            },
            userName: {
                title: '用户名',
                filter: false,
                type: 'text',
            },
            name: {
                title: '姓名',
                filter: false,
                type: 'text',
            },
            role: {
                title: '角色',
                filter: false,
                type: 'text',
            },
            email: {
                title: '邮箱',
                filter: false,
                type: 'custom',
                renderComponent: OptionalCellComponent,
            },
            phone: {
                title: '手机号',
                filter: false,
                type: 'text',
            },
        },
    }; //}
    source: ServerDataSource;

    constructor(private toastrService: NbToastrService,
                private adminUser: AdminUserService,
                private windowService: NbWindowService,
                private http: HttpClient) { }

    ngOnInit(): void //{
    {
        this.source = new ServerDataSource(this.http, {
            endPoint: RESTfulAPI.AdminUser.getPage,
            pagerPageKey: 'pageno',
            pagerLimitKey: 'size',
            sortFieldKey: 'sort',
            sortDirKey: 'sortDir',
            filterFieldKey: '#field#',
            dataKey: 'pairs',
            totalKey: 'total',
        });

        (this.source as any).find = function (element: User): Promise<any> {
            const found = (this.data as User[]).find(elem => elem.userName == element.userName);
            if(found) {
                return Promise.resolve(found);
            } else {
                return Promise.reject("NOT FOUND");
            }
        }
    } //}

    onsearchinput(pair: [string, (hints: string[]) => void]) //{
    {
        const input = pair[0];
        const hook = pair[1];

        const data: User[] = (this.source as any).data;
        let ans = [];
        if(data && input.trim().length > 0) {
            data.forEach((value) => {
                if(value.userName.match(input)) {
                    ans.push(value.userName);
                }

                if(value.name && value.name.match(input)) {
                    ans.push(value.name);
                }
            })
        }

        hook(ans);
    } //}

    onsearchenter(search: string) //{
    {
        this.source.addFilter({
            field: 'searchWildcard',
            search: search.trim()
        });
        this.source.refresh();
    } //}

    async onDeleteConfirm(event: {
        data: User, 
        source: LocalDataSource,
        confirm: {resolve: (data: User) => void, reject: () => void},
    }) //{
    {
        const win = this.windowService.open(ConfirmWindowComponent, {
            title: '用户管理',
            context: {message: '确认删除用户?'}
        });
        await win.onClose.toPromise();
        const confirmed = win.config.context['isConfirmed'];
        if(!confirmed) return;

        try {
            await this.adminUser.delete(event.data.userName);
            this.toastrService.success(`删除'${event.data.userName}'`, "用户管理");
        } catch {
            this.toastrService.danger(`删除用户'${event.data.userName}'失败`, "用户管理");
            return event.confirm.reject();
        }

        event.confirm.resolve(event.data);
        this.source.refresh();
    } //}
}

