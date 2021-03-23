import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { NbToastrService } from '@nebular/theme';
import { LocalDataSource } from 'ng2-smart-table';
import { IDataDictionary, IDataDictionaryRecord } from '../../../shared/utils';

@Component({
    selector: 'ngx-dictionary-info',
    templateUrl: './dictionary-info.component.html',
    styleUrls: ['./dictionary-info.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class DictionaryInfoComponent implements OnInit {
    settings = {
        actions: {
            columnTitle: '操作',
            add: true,
            edit: true,
            delete: true,
            position: 'right',
        },
        noDataMessage: '找不到字典数据项',
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
            internalValue: {
                title: '关键字',
                type: 'text',
            },
            displayValue: {
                title: '值',
                type: 'text',
            },
            isDefault: {
                title: '是否默认',
                type: 'text',
                editor: {
                    type: 'checkbox'
                }
            },
            orderNo: {
                title: '序号',
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
    source: IDataDictionaryRecord[] = [
        {
            code: 'fruit',
            internalValue: 'apple',
            displayValue: '苹果',
            isDefault: true,
            orderNo: 1,
        }
    ];

    constructor(private toastrService: NbToastrService) { }

    recordLength: number;
    ngOnInit(): void {
        this.recordLength = this.source.length;
    }

    private static internalValueRegex = /.{2,10}/;
    private static dispalyValueRegex = /[A-Za-z][A-Za-z0-9_\-@]{1,9}/;
    private async checkData(row: IDataDictionaryRecord, toaster: boolean = true): Promise<boolean> //{
    {
        return true;
    } //}


    async onCreateConfirm(event: {
        newData: IDataDictionaryRecord,
        source: LocalDataSource,
        confirm: {resolve: (data: IDataDictionaryRecord) => void, reject: () => void},
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
        data: IDataDictionary,
        newData: IDataDictionaryRecord,
        source: LocalDataSource,
        confirm: {resolve: (data: IDataDictionaryRecord) => void, reject: () => void},
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
        data: IDataDictionaryRecord, 
        source: LocalDataSource,
        confirm: {resolve: (data: IDataDictionaryRecord) => void, reject: () => void},
    }) //{
    {
        console.log(await event.source.getAll(), this.source);
        this.recordLength--;
        event.confirm.resolve(event.data);
    } //}
}

