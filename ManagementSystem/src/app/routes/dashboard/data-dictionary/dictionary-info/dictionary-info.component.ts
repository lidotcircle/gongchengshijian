import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NbToastrService } from '@nebular/theme';
import { ServerDataSource } from 'ng2-smart-table';
import { interval, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { DictionaryData, DictionaryType } from 'src/app/entity';
import { DataDictionaryService } from 'src/app/service/datadict/data-dictionary.service';
import { RESTfulAPI } from 'src/app/service/restful';
import { Pattern, UglyInputHint } from 'src/app/shared/utils';
import { getElementWait } from 'src/app/shared/utils/get-util';


function isEmpty(str: string | null) {return str == null || str == '';}

@Component({
    selector: 'ngx-dictionary-info',
    templateUrl: './dictionary-info.component.html',
    styleUrls: ['./dictionary-info.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class DictionaryInfoComponent implements OnInit, OnDestroy {
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
                editable: false,
                type: 'text',
            },
            value: {
                title: '值',
                filter: false,
                editable: false,
                type: 'text',
            },
            isDefault: {
                title: '是否默认',
                filter: false,
                type: 'text',
                editor: {
                    type: 'checkbox',
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
    destroy$: Subject<void>;
    constructor(private toastrService: NbToastrService,
                private activatedRoute: ActivatedRoute,
                private datadictService: DataDictionaryService,
                private http: HttpClient) {
        this.dictType = {} as any;
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
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

        this.setupUgly();
        this.destroy$ = new Subject();
        interval(1000).pipe(takeUntil(this.destroy$))
            .subscribe(() => {
                UglyInputHint(input => input.placeholder == '值' || input.placeholder == '关键字',
                    async input => {
                        if (isEmpty(input.value)) {
                            return '此字段不为空';
                        }

                        const [k, v, _] = await this.contain_keyword_value_order(input.value, input.value, 0);
                        if(k && input.placeholder == '关键字') {
                            return '关键字重复';
                        }
                        if (v && input.placeholder == '值') {
                            return '值重复';
                        }

                        return null;
                    },
                    input => isEmpty(input.value) && '此字段不为空' || null
                );
            });
    } //}

    private async setupUgly() //{
    {
        let el = await getElementWait(() => document.getElementsByClassName("ng2-smart-action-add-add"), 2000);

        el[0].addEventListener("click", async (ev: MouseEvent) => {
            const inputs = await getElementWait(() => {
                const evv = document.querySelector("[ng2-st-thead-form-row]");
                if (evv != null) {
                    const vve = evv.querySelectorAll("input");
                    if (vve.length > 0) return vve;
                }

                return null;
            }, 1000);

            if(inputs != null) {
                let [v, o, d] = await this.max_value_order_default();
                (inputs[1] as HTMLInputElement).value = `${v + 1}`;
                (inputs[1] as HTMLInputElement).dispatchEvent(new Event('input'));
                (inputs[3] as HTMLInputElement).value = `${o + 1}`;
                (inputs[3] as HTMLInputElement).dispatchEvent(new Event('input'));
            }
        });
    } //}

    private async max_value_order_default(): Promise<[number, number, string]> //{
    {
        let list = await this.source.getAll() as DictionaryData[];
        let v = -1;
        let o = 0;
        let d = "";
        for (const item of list) {
            v = Math.max(v, parseInt(item.value));
            o = Math.max(o, item.order);
            if (item.isDefault) d = item.value;
        }

        return [v, o, d];
    } //}

    private async contain_keyword_value_order(keyword: string, value: string, order: number): Promise<[string, string, string]> //{
    {
        let list = await this.source.getAll() as DictionaryData[];
        let k = null;
        let v = null;
        let o = null;
        for (const item of list) {
            if (item.keyword == keyword) k = item.value;
            if (item.value == value) v = item.value;
            if (item.order == order) o = item.value;
        }
        return [k, v, o];
    } //}

    private initSource() //{
    {
        const http = new Proxy(this.http, {
            get: (target, prop) => {
                if(prop === 'get') {
                    const _this = this;
                    return function (url, options) {
                        const params: HttpParams = options.params;
                        options.params = params.set('typeCode', _this.dictType.typeCode);
                        return target.get(url, options);
                    }
                } else {
                    return target[prop];
                }
            }
        });

        this.source = new ServerDataSource(http, {
            endPoint: RESTfulAPI.DataDictionary.getDataPage,
            pagerPageKey: 'pageno',
            pagerLimitKey: 'size',
            sortFieldKey: 'sort',
            sortDirKey: 'sortDir',
            filterFieldKey: '#field#',
            dataKey: 'pairs',
            totalKey: 'total',
        });

        (this.source as any).find = function (element: DictionaryData | string): Promise<any> {
            if (typeof element == 'object') {
                element = element.value;
            }
            const found = (this.data as DictionaryData[]).find(elem => elem.value == element);
            if(found) {
                return Promise.resolve(found);
            } else {
                return Promise.resolve(null);
            }
        }

        this.refresh();
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
        this.refresh();
    } //}

    private refresh() {
        this.source.refresh();
    }

    private keywordRegex = Pattern.Regex.uname1;
    private valueRegex = Pattern.Regex.dvalue;
    private async checkData(row: DictionaryData): Promise<boolean> //{
    {
        if(!this.keywordRegex.test(row.keyword)) {
            this.toastrService.danger("关键字字段非法: " + this.keywordRegex[Pattern.HintSym], "数据字典");
            return false;
        }

        if(!this.valueRegex.test(row.value)) {
            this.toastrService.danger("值字段非法: " + this.valueRegex[Pattern.HintSym], "数据字典");
            return false;
        }

        if(row.order != null && !this.valueRegex.test(`${row.order}`)) {
            this.toastrService.danger("顺序字段非法: " + this.valueRegex[Pattern.HintSym], "数据字典");
            return false;
        }

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

        const [k, v, o] = await this.contain_keyword_value_order(event.newData.keyword, event.newData.value, event.newData.order);
        let err = ''
        if (k) err = '关键字重复';
        if (v) err = '值字段重复';
        if (o) err = '顺序字段重复';
        if(event.newData.isDefault) {
            const [v, o, d] = await this.max_value_order_default();
            if (d != '') {
                err = '已存在默认字段';
            }
        }
        if(err != '') {
            this.toastrService.danger(`创建字典数据'${event.newData.keyword}'失败, ${err}`, "数据字典");
            return event.confirm.reject();
        }

        try {
            event.newData.typeCode = this.dictType.typeCode;
            await this.datadictService.postData(event.newData);
            this.toastrService.success(`新建'${event.newData.keyword}'`, "数据字典");
        } catch {
            this.toastrService.danger(`创建字典数据'${event.newData.keyword}'失败`, "数据字典");
            return event.confirm.reject();
        }

        // TODO 
        event.confirm.resolve(event.newData);
        this.refresh();
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

        if (event.newData.isDefault && !event.data.isDefault) {
            const [_, $, d] = await this.max_value_order_default();
            const v = await this.source.find(d) as DictionaryData;
            if (v != null) {
                v.isDefault = false;
                try {
                    await this.datadictService.putData(v);
                } catch {
                    this.toastrService.danger(`编辑字典树'${event.newData.keyword}'失败`, "数据字典");
                    return event.confirm.reject();
                }
            }
        }

        if(event.newData.order != event.data.order) {
            const [_, $, o] = await this.contain_keyword_value_order('', '', event.newData.order)
            const dt = await this.source.find(o) as DictionaryData;
            if (dt != null) {
                dt.order = event.data.order;
                try {
                    await this.datadictService.putData(dt);
                } catch {
                    this.toastrService.danger(`编辑字典树'${event.newData.keyword}'失败`, "数据字典");
                    return event.confirm.reject();
                }
            }
        }

        try {
            await this.datadictService.putData(event.newData);
            this.toastrService.success(`修改'${event.data.keyword}'`, "数据字典");
        } catch {
            this.toastrService.danger(`编辑字典树'${event.newData.keyword}'失败`, "数据字典");
            return event.confirm.reject();
        }

        event.confirm.resolve(event.newData);
        this.refresh();
    } //}

    async onDeleteConfirm(event: {
        data: DictionaryData, 
        source: ServerDataSource,
        confirm: {resolve: (data: DictionaryData) => void, reject: () => void},
    }) //{
    {
        try {
            await this.datadictService.deleteData(event.data.typeCode, event.data.keyword);
            this.toastrService.success(`删除'${event.data.keyword}'`, "数据字典");
        } catch {
            this.toastrService.danger(`删除字典数据'${event.data.keyword}'失败`, "数据字典");
            return event.confirm.reject();
        }

        event.confirm.resolve(event.data);
        this.refresh();
    } //}
}

