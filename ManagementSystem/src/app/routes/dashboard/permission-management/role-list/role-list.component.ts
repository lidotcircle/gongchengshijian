import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { NbToastrService } from '@nebular/theme';
import { LocalDataSource } from 'ng2-smart-table';
import { Role } from 'src/app/entity';
import { ButtonsCellComponent } from './buttons-cell.component';

type DataType = Role;

@Component({
    selector: 'ngx-role-list',
    templateUrl: './role-list.component.html',
    styleUrls: ['./role-list.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class RoleListComponent implements OnInit {
    settings = {
        actions: {
            columnTitle: '操作',
            add: true,
            edit: true,
            delete: true,
            position: 'right',
        },
        noDataMessage: '找不到字典类型',
        sort: false,
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
        rowClassFunction: () => 'data-row',
        columns: {
            buttons: {
                title: 'Buttons',
                width: '4em',
                editable: false,
                type: 'custom',
                renderComponent: ButtonsCellComponent,
            },
            roleName: {
                title: '名称',
                type: 'text',
            },
            createdDate: {
                title: '创建日期',
                editable: false,
                type: 'text',
            },
            modifiedDate: {
                title: '修改日期',
                editable: false,
                type: 'text',
            },
        },
    };
    source: DataType[] = [
        {roleName: "管理员", createdDate: new Date(), modifiedDate: new Date()},
    ];

    constructor(private toastrService: NbToastrService) { }

    recordLength: number;
    ngOnInit(): void {
        this.recordLength = this.source.length;
    }

    private static nameRegex = /.{2,10}/;
    private async checkData(name: string, toaster: boolean = true): Promise<boolean> //{
    {
        const nameTestFail = name == null || !RoleListComponent.nameRegex.test(name);

        if(toaster && nameTestFail) {
            let message = "错误的角色名";
            this.toastrService.show(message, '角色管理', {
                duration: 2000,
                destroyByClick: true,
                status: 'warning'
            });
        }

        return !nameTestFail;
    } //}


    async onCreateConfirm(event: {
        newData: DataType, 
        source: LocalDataSource,
        confirm: {resolve: (data: DataType) => void, reject: () => void},
    }) //{
    {
        if(!(await this.checkData(event.newData.roleName))) {
            event.confirm.reject();
            return;
        }

        console.log(await event.source.getAll());
        this.recordLength++;
        event.confirm.resolve(event.newData);
    } //}

    async onEditConfirm(event: {
        data: DataType,
        newData: DataType,
        source: LocalDataSource,
        confirm: {resolve: (data: DataType) => void, reject: () => void},
    }) //{
    {
        if(!(await this.checkData(event.newData.roleName))) {
            event.confirm.reject();
            return;
        }

        console.log(await event.source.getAll());
        event.confirm.resolve(event.newData);
    } //}

    async onDeleteConfirm(event: {
        data: DataType, 
        source: LocalDataSource,
        confirm: {resolve: (data: DataType) => void, reject: () => void},
    }) //{
    {
        console.log(await event.source.getAll(), this.source);
        this.recordLength--;
        event.confirm.resolve(event.data);
    } //}
}

