import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NbToastrService } from '@nebular/theme';
import { ServerDataSource } from 'ng2-smart-table';
import { DictionaryData, DictionaryType } from 'src/app/entity';
import { DataDictionaryService } from 'src/app/service/datadict/data-dictionary.service';
import { RESTfulAPI } from 'src/app/service/restful';

@Component({
    selector: 'ngx-dictionary-info',
    templateUrl: './dictionary-info.component.html',
    styleUrls: ['./dictionary-info.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class DictionaryInfoComponent implements OnInit {
    // settings //{
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
            keyword: {
                title: '关键字',
                filter: false,
                type: 'text',
            },
            value: {
                title: '值',
                filter: false,
                type: 'text',
            },
            isDefault: {
                title: '是否默认',
                filter: false,
                type: 'text',
                editor: {
                    type: 'checkbox'
                }
            },
            order: {
                title: '序号',
                filter: false,
                type: 'text',
            },
            remark: {
                title: '备注',
                filter: false,
                type: 'text',
                editor: {
                    type: 'textarea'
                },
            },
        },
    }; //}

    dictType: DictionaryType;
    source: ServerDataSource;
    constructor(private toastrService: NbToastrService,
                private activatedRoute: ActivatedRoute,
                private datadictService: DataDictionaryService,
                private http: HttpClient) {
        this.dictType = {} as any;
    }

    ngOnInit(): void //{
    {
        this.activatedRoute.queryParamMap.subscribe(async (params) => {
            const typeCode = params.get("typeCode");
            if(typeCode == null) {
                this.toastrService.danger("页面错误", "错误", {
                    duration: 60 * 1000
                });
            } else {
                try {
                    this.dictType = await this.datadictService.getType(typeCode);
                    this.initSource();
                } catch {
                    this.toastrService.danger("获取数据失败, 请刷新", "错误");
                }
            }
        });
    } //}

    private initSource() //{
    {
        this.source = new ServerDataSource(this.http, {
            endPoint: RESTfulAPI.DataDictionary.getDataPage,
            pagerPageKey: 'pageno',
            pagerLimitKey: 'size',
            sortFieldKey: 'sort',
            sortDirKey: 'sortDir',
            filterFieldKey: '#field#',
            dataKey: 'pairs',
            totalKey: 'total',
        });

        (this.source as any).find = function (element: DictionaryData): Promise<any> {
            const found = (this.data as DictionaryData[]).find(elem => elem.value == element.value);
            if(found) {
                return Promise.resolve(found);
            } else {
                return Promise.reject("NOT FOUND");
            }
        }

        this.source.addFilter({
            field: 'typeCode',
            search: this.dictType.typeCode
        }, true, true);
    } //}

    onsearchinput(pair: [string, (hints: string[]) => void]) //{
    {
        const input = pair[0];
        const hook = pair[1];

        const data: DictionaryData[] = (this.source as any).data;
        let ans = [];
        if(data && input.trim().length > 0) {
            data.forEach((value) => {
                if(value.value.match(input)) {
                    ans.push(value.value);
                }
                if(value.keyword.match(input)) {
                    ans.push(value.keyword);
                }
            });
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

    private static internalValueRegex = /.{2,10}/;
    private static dispalyValueRegex = /[A-Za-z][A-Za-z0-9_\-@]{1,9}/;
    private async checkData(row: DictionaryData, toaster: boolean = true): Promise<boolean> //{
    {
        return true;
    } //}

    async onCreateConfirm(event: {
        newData: DictionaryData,
        source: ServerDataSource,
        confirm: {resolve: (data: DictionaryData) => void, reject: () => void},
    }) //{
    {
        if(!(await this.checkData(event.newData))) {
            event.confirm.reject();
            return;
        }

        try {
            event.newData.typeCode = this.dictType.typeCode;
            await this.datadictService.postData(event.newData);
            this.toastrService.success(`新建'${event.newData.keyword}'`, "数据字典");
        } catch {
            this.toastrService.danger(`创建字典数据'${event.newData.keyword}'失败`, "数据字典");
            return event.confirm.reject();
        }

        event.confirm.resolve(event.newData);
        this.source.refresh();
    } //}

    async onEditConfirm(event: {
        data: DictionaryData,
        newData: DictionaryData,
        source: ServerDataSource,
        confirm: {resolve: (data: DictionaryData) => void, reject: () => void},
    }) //{
    {
        if(!(await this.checkData(event.newData))) {
            event.confirm.reject();
            return;
        }

        try {
            await this.datadictService.putData(event.newData);
            this.toastrService.success(`修改'${event.data.keyword}'`, "数据字典");
        } catch {
            this.toastrService.danger(`编辑字典树'${event.newData.keyword}'失败`, "数据字典");
            return event.confirm.reject();
        }

        event.confirm.resolve(event.newData);
        this.source.refresh();
    } //}

    async onDeleteConfirm(event: {
        data: DictionaryData, 
        source: ServerDataSource,
        confirm: {resolve: (data: DictionaryData) => void, reject: () => void},
    }) //{
    {
        try {
            await this.datadictService.deleteType(event.data.typeCode);
            this.toastrService.success(`删除'${event.data.keyword}'`, "数据字典");
        } catch {
            this.toastrService.danger(`删除字典数据'${event.data.keyword}'失败`, "数据字典");
            return event.confirm.reject();
        }

        event.confirm.resolve(event.data);
        this.source.refresh();
    } //}
}

