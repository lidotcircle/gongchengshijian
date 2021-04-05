import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { NbToastrService } from '@nebular/theme';
import { LocalDataSource, Ng2SmartTableComponent } from 'ng2-smart-table';
import { IDataDictionary } from 'src/app/shared/utils';
import { ClickCellComponent } from './click-cell.component';

@Component({
    selector: 'ngx-dictionary-list',
    templateUrl: './dictionary-list.component.html',
    styleUrls: ['./dictionary-list.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class DictionaryListComponent implements OnInit {
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
            clickColumn: {
                title: '数据',
                width: '4em',
                class: 'goto-dict-info-column',
                editable: false,
                type: 'custom',
                renderComponent: ClickCellComponent,
            },
            name: {
                title: '名称',
                type: 'text',
            },
            code: {
                title: '编码',
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
    source: IDataDictionary[] = [
        {name: '水果', code: 'fruit', remark: '水果可以吃'},
    ];

    constructor(private toastrService: NbToastrService) { }

    recordLength: number;
    ngOnInit(): void {
        this.recordLength = this.source.length;
    }

    private static nameRegex = /.{2,10}/;
    private static codeRegex = /[A-Za-z][A-Za-z0-9_\-@]{1,9}/;
    private async checkData(name: string, code: string, toaster: boolean = true): Promise<boolean> //{
    {
        const nameTestFail = name == null || !DictionaryListComponent.nameRegex.test(name);
        const codeTestFail = code == null || !DictionaryListComponent.codeRegex.test(code);

        if(toaster && (nameTestFail || codeTestFail)) {
            let message = (nameTestFail ? 
                '数据字典类型中的名称字段的字符长度在2~10之间' : 
                '字典数据类型中的编码字段需由2~10长度的字母 数字 "_", "-", "@"组成');
            this.toastrService.show(message, '数据字典类型', {
                duration: 2000,
                destroyByClick: true,
                status: 'warning'
            });
        }

        return !nameTestFail && !codeTestFail;
    } //}


    async onCreateConfirm(event: {
        newData: IDataDictionary, 
        source: LocalDataSource,
        confirm: {resolve: (data: IDataDictionary) => void, reject: () => void},
    }) //{
    {
        if(!(await this.checkData(event.newData.name, event.newData.code))) {
            event.confirm.reject();
            return;
        }

        console.log(await event.source.getAll());
        this.recordLength++;
        event.confirm.resolve(event.newData);
    } //}

    async onEditConfirm(event: {
        data: IDataDictionary,
        newData: IDataDictionary,
        source: LocalDataSource,
        confirm: {resolve: (data: IDataDictionary) => void, reject: () => void},
    }) //{
    {
        if(!(await this.checkData(event.newData.name, event.newData.code))) {
            event.confirm.reject();
            return;
        }

        console.log(await event.source.getAll());
        event.confirm.resolve(event.newData);
    } //}

    async onDeleteConfirm(event: {
        data: IDataDictionary, 
        source: LocalDataSource,
        confirm: {resolve: (data: IDataDictionary) => void, reject: () => void},
    }) //{
    {
        console.log(await event.source.getAll(), this.source);
        this.recordLength--;
        event.confirm.resolve(event.data);
    } //}
}

