import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { NbToastrService } from '@nebular/theme';
import { LocalDataSource } from 'ng2-smart-table';
import { OptionalCellComponent } from '../../../shared/components/optional-cell.component';
import { ICommonUser } from '../../../shared/utils';
import { ClickCellComponent } from './click-cell.component';

@Component({
    selector: 'ngx-user-list',
    templateUrl: './user-list.component.html',
    styleUrls: ['./user-list.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class UserListComponent implements OnInit {
    settings = {
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
                title: '查看',
                width: '4em',
                class: 'user-detail',
                editable: false,
                type: 'custom',
                renderComponent: ClickCellComponent,
            },
            name: {
                title: '用户名',
                type: 'text',
            },
            role: {
                title: '角色',
                type: 'text',
            },
            email: {
                title: '邮箱',
                type: 'custom',
                renderComponent: OptionalCellComponent,
            },
            phoneno: {
                title: '手机号',
                type: 'text',
            },
        },
    };
    source: ICommonUser[] = [
        {
            name: 'niki',
            role: 'admin',
            phoneno: '110',
        }
    ];

    constructor(private toastrService: NbToastrService) { }

    recordLength: number;
    ngOnInit(): void {
        this.recordLength = this.source.length;
    }

    private async checkData(row: ICommonUser, toaster: boolean = true): Promise<boolean> //{
        {
            return true;
        } //}


    async onCreateConfirm(event: {
        newData: ICommonUser,
        source: LocalDataSource,
        confirm: {resolve: (data: ICommonUser) => void, reject: () => void},
    }) //{
    {
        if(!(await this.checkData(event.newData))) {
            event.confirm.reject();
            return;
        }

        console.log(await event.source.getAll());
        this.recordLength++;
        event.confirm.resolve(event.newData);
    } //}

    async onEditConfirm(event: {
        data: ICommonUser,
        newData: ICommonUser,
        source: LocalDataSource,
        confirm: {resolve: (data: ICommonUser) => void, reject: () => void},
    }) //{
    {
        if(!(await this.checkData(event.newData))) {
            event.confirm.reject();
            return;
        }

        console.log(await event.source.getAll());
        event.confirm.resolve(event.newData);
    } //}

    async onDeleteConfirm(event: {
        data: ICommonUser, 
        source: LocalDataSource,
        confirm: {resolve: (data: ICommonUser) => void, reject: () => void},
    }) //{
    {
        console.log(await event.source.getAll(), this.source);
        this.recordLength--;
        event.confirm.resolve(event.data);
    } //}
}

