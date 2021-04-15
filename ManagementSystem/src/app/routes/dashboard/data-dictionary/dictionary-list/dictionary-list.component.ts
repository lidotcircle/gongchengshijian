import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { NbToastrService } from '@nebular/theme';
import { Ng2SmartTableComponent, ServerDataSource } from 'ng2-smart-table';
import { DictionaryType } from 'src/app/entity';
import { DataDictionaryService } from 'src/app/service/datadict/data-dictionary.service';
import { RESTfulAPI } from 'src/app/service/restful';
import { Pattern } from 'src/app/shared/utils';
import { ClickCellComponent } from './click-cell.component';

@Component({
    selector: 'ngx-dictionary-list',
    templateUrl: './dictionary-list.component.html',
    styleUrls: ['./dictionary-list.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class DictionaryListComponent implements OnInit {

    // settings //{
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
        columns: {
            clickColumn: {
                title: '数据',
                width: '4em',
                editable: false,
                filter: false,
                type: 'custom',
                renderComponent: ClickCellComponent,
            },
            typeCode: {
                title: '编码',
                type: 'text',
                filter: false,
                editable: false,
            },
            typeName: {
                title: '名称',
                type: 'text',
                filter: false,
            },
            remark: {
                title: '备注',
                type: 'text',
                filter: false,
                editor: {
                    type: 'textarea'
                },
            },
        },
    }; //}

    source: ServerDataSource;

    constructor(private toastrService: NbToastrService,
                private http: HttpClient,
                private datadictService: DataDictionaryService) {
    }

    ngOnInit(): void {
        this.source = new ServerDataSource(this.http, {
            endPoint: RESTfulAPI.DataDictionary.getTypePage,
            pagerPageKey: 'pageno',
            pagerLimitKey: 'size',
            sortFieldKey: 'sort',
            sortDirKey: 'sortDir',
            filterFieldKey: '#field#',
            dataKey: 'pairs',
            totalKey: 'total',
        });

        (this.source as any).find = function (element: DictionaryType): Promise<any> {
            const found = (this.data as DictionaryType[]).find(elem => elem.typeCode == element.typeCode);
            if(found) {
                return Promise.resolve(found);
            } else {
                return Promise.reject("NOT FOUND");
            }
        }
    }

    private static nameRegex = Pattern.Regex.uname;
    private static codeRegex = Pattern.Regex.aname;
    private async checkData(name: string, code: string, toaster: boolean = true): Promise<boolean> //{
    {
        const nameTestFail = name == null || !DictionaryListComponent.nameRegex.test(name);
        const codeTestFail = code == null || !DictionaryListComponent.codeRegex.test(code);

        if(toaster && (nameTestFail || codeTestFail)) {
            let message = (nameTestFail ? 
                `名称字段: ${DictionaryListComponent.nameRegex[Pattern.HintSym]}` : 
                `编码字段: ${DictionaryListComponent.codeRegex[Pattern.HintSym]}`);
            this.toastrService.warning(message, '数据字典', );
        }

        return !nameTestFail && !codeTestFail;
    } //}


    onsearchinput(pair: [string, (hints: string[]) => void]) {
        const input = pair[0];
        const hook = pair[1];

        const data: DictionaryType[] = (this.source as any).data;
        let ans = [];
        if(data && input.trim().length > 0) {
            data.forEach((value) => {
                if(value.typeName.match(input)) {
                    ans.push(value.typeName);
                }
            })
        }

        hook(ans);
    }

    onsearchenter(search: string) {
        this.source.addFilter({
            field: 'searchWildcard',
            search: search.trim()
        });
        this.source.refresh();
    }

    async onCreateConfirm(event: {
        newData: DictionaryType, 
        source: ServerDataSource,
        confirm: {resolve: (data: DictionaryType) => void, reject: () => void},
    }) //{
    {
        if(!(await this.checkData(event.newData.typeName, event.newData.typeCode))) {
            event.confirm.reject();
            return;
        }

        try {
            await this.datadictService.postType(event.newData);
            this.toastrService.success(`新建'${event.newData.typeName}'`, "数据字典");
        } catch {
            this.toastrService.danger(`创建数据字典'${event.newData.typeName}'失败`, "数据字典");
            return event.confirm.reject();
        }

        event.confirm.resolve(event.newData);
        this.source.refresh();
    } //}

    async onEditConfirm(event: {
        data: DictionaryType,
        newData: DictionaryType,
        source: ServerDataSource,
        confirm: {resolve: (data: DictionaryType) => void, reject: () => void},
    }) //{
    {
        if(!(await this.checkData(event.newData.typeName, event.newData.typeCode))) {
            event.confirm.reject();
            return;
        }

        try {
            await this.datadictService.putType(event.newData);
            this.toastrService.success(`修改'${event.data.typeName}'`, "数据字典");
        } catch {
            this.toastrService.danger(`编辑数据字典'${event.newData.typeName}'失败`, "数据字典");
            return event.confirm.reject();
        }

        event.confirm.resolve(event.newData);
        this.source.refresh();
    } //}

    async onDeleteConfirm(event: {
        data: DictionaryType, 
        source: ServerDataSource,
        confirm: {resolve: (data: DictionaryType) => void, reject: () => void},
    }) //{
    {
        try {
            await this.datadictService.deleteType(event.data.typeCode);
            this.toastrService.success(`删除'${event.data.typeName}'`, "数据字典");
        } catch {
            this.toastrService.danger(`删除数据字典'${event.data.typeName}'失败`, "数据字典");
            return event.confirm.reject();
        }

        event.confirm.resolve(event.data);
        this.source.refresh();
    } //}
}

