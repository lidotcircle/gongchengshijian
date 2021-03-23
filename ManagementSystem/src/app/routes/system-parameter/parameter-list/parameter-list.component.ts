import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { NbToastrService } from '@nebular/theme';
import { LocalDataSource } from 'ng2-smart-table';
import { ISystemParameter } from '../../../shared/utils';

@Component({
    selector: 'ngx-parameter-list',
    templateUrl: './parameter-list.component.html',
    styleUrls: ['./parameter-list.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class ParameterListComponent implements OnInit {
    settings = {
        actions: {
            columnTitle: '操作',
            add: true,
            edit: true,
            delete: true,
            position: 'right',
        },
        noDataMessage: '找不到系统参数',
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
            code: {
                title: '名称',
                type: 'text',
            },
            value: {
                title: '值',
                type: 'text',
            },
            remark: {
                title: '备注',
                type: 'text',
                editor: {
                    type: 'textarea'
                },
            },
        },
    };
    source: ISystemParameter[] = [
        {
            code: 'fruit',
            value: '200',
        }
    ];

    constructor(private toastrService: NbToastrService) { }

    recordLength: number;
    ngOnInit(): void {
        this.recordLength = this.source.length;
    }

    private static codeRegex = /[A-Za-z][A-Za-z0-9_\-@]{1,9}/;
    private async checkData(row: ISystemParameter, toaster: boolean = true): Promise<boolean> //{
    {
        return true;
    } //}


    async onCreateConfirm(event: {
        newData: ISystemParameter,
        source: LocalDataSource,
        confirm: {resolve: (data: ISystemParameter) => void, reject: () => void},
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
        data: ISystemParameter,
        newData: ISystemParameter,
        source: LocalDataSource,
        confirm: {resolve: (data: ISystemParameter) => void, reject: () => void},
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
        data: ISystemParameter, 
        source: LocalDataSource,
        confirm: {resolve: (data: ISystemParameter) => void, reject: () => void},
    }) //{
    {
        console.log(await event.source.getAll(), this.source);
        this.recordLength--;
        event.confirm.resolve(event.data);
    } //}
}

