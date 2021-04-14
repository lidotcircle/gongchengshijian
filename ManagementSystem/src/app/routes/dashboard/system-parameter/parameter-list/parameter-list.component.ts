import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { NbToastrService } from '@nebular/theme';
import { ServerDataSource } from 'ng2-smart-table';
import { SystemParameter } from 'src/app/entity';
import { RESTfulAPI } from 'src/app/service/restful';
import { SysParamService } from 'src/app/service/system-param/system-parameter.service';
import { Pattern } from 'src/app/shared/utils';

@Component({
    selector: 'ngx-parameter-list',
    templateUrl: './parameter-list.component.html',
    styleUrls: ['./parameter-list.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class ParameterListComponent implements OnInit {

    // settings //{
    settings = {
        // hideSubHeader: true,
        pager: {
            perPage: 10,
            display: true,
        },
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
            key: {
                title: '名称',
                filter: false,
                type: 'text',
                editable: false,
            },
            value: {
                title: '值',
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

    source: ServerDataSource;
    constructor(private toastrService: NbToastrService,
                private sysParamService: SysParamService,
                private http: HttpClient) {}

    ngOnInit(): void {
        this.source = new ServerDataSource(this.http, {
            endPoint: RESTfulAPI.SysParam.getPage,
            pagerPageKey: 'pageno',
            pagerLimitKey: 'size',
            sortFieldKey: 'sort',
            sortDirKey: 'sortDir',
            filterFieldKey: 'filter',
            dataKey: 'pairs',
            totalKey: 'total',
        });

        (this.source as any).find = function (element: SystemParameter): Promise<any> {
            const found = (this.data as SystemParameter[]).find(elem => elem.key == element.key);
            if(found) {
                return Promise.resolve(found);
            } else {
                return Promise.reject("NOT FOUND");
            }
        }
    }

    onsearchinput(pair: [string, (hints: string[]) => void]) {
        const input = pair[0];
        const hook = pair[1];

        const data: SystemParameter[] = (this.source as any).data;
        let ans = [];
        if(data && input.trim().length > 0) {
            data.forEach((value) => {
                if(value.key.match(input)) {
                    ans.push(value.key);
                }
            })
        }

        hook(ans);
    }

    onsearchenter(search: string) {
        this.source.addFilter({
            field: 'filter',
            search: search.trim()
        });
        this.source.refresh();
    }

    private keyRegex= new RegExp(Pattern.username.toString());
    private async checkData(row: SystemParameter, toastr: boolean = true): Promise<boolean> //{
    {
        if(this.keyRegex.test(row.key)) {
            return true;
        } else {
            if(toastr) {
                this.toastrService.warning("非法系统参数键", "系统参数");
            }
            return false;
        }
    } //}


    async onCreateConfirm(event: {
        newData: SystemParameter,
        source: ServerDataSource,
        confirm: {resolve: (data: SystemParameter) => void, reject: () => void},
    }) //{
    {
        if(!(await this.checkData(event.newData))) {
            event.confirm.reject();
            return;
        }

        try {
            await this.sysParamService.post(event.newData);
            this.toastrService.success(`新建'${event.newData.key}'`, "系统参数");
        } catch {
            this.toastrService.danger(`创建系统参数'${event.newData.key}'失败`, "系统参数");
            return event.confirm.reject();
        }

        event.confirm.resolve(event.newData);
    } //}

    async onEditConfirm(event: {
        data: SystemParameter,
        newData: SystemParameter,
        source: ServerDataSource,
        confirm: {resolve: (data: SystemParameter) => void, reject: () => void},
    }) //{
    {
        if(event.data.value == event.newData.value && event.data.remark == event.newData.remark) {
            event.confirm.resolve(event.data);
            this.source.refresh();
            return;
        }

        try {
            await this.sysParamService.put(event.newData);
            this.toastrService.success(`修改'${event.data.key}'`, "系统参数");
        } catch {
            this.toastrService.danger(`编辑系统参数'${event.newData.key}'失败`, "系统参数");
            return event.confirm.reject();
        }

        event.confirm.resolve(event.newData);
        this.source.refresh();
    } //}

    async onDeleteConfirm(event: {
        data: SystemParameter, 
        source: ServerDataSource,
        confirm: {resolve: (data: SystemParameter) => void, reject: () => void},
    }) //{
    {
        try {
            await this.sysParamService.delete(event.data.key);
            this.toastrService.success(`删除'${event.data.key}'`, "系统参数");
        } catch {
            this.toastrService.danger(`删除系统参数'${event.data.key}'失败`, "系统参数");
            return event.confirm.reject();
        }

        event.confirm.resolve(event.data);
    } //}
}

