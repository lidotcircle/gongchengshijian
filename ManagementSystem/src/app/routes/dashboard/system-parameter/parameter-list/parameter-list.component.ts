import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { NbToastrService, NbWindowService } from '@nebular/theme';
import { ServerDataSource } from 'ng2-smart-table';
import { interval, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { SystemParameter } from 'src/app/entity';
import { RESTfulAPI } from 'src/app/service/restful';
import { SysParamService } from 'src/app/service/system-param/system-parameter.service';
import { ConfirmWindowComponent } from 'src/app/shared/components/confirm-window.component';
import { httpErrorHandler, Pattern, UglyInputHint } from 'src/app/shared/utils';


function isEmpty(v: string | null) {return !v || v == '';}

@Component({
    selector: 'ngx-parameter-list',
    templateUrl: './parameter-list.component.html',
    styleUrls: ['./parameter-list.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class ParameterListComponent implements OnInit, OnDestroy {
    destroy$: Subject<void>;

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
            remark: {
                title: '名称',
                filter: false,
                type: 'text',
            },
            key: {
                title: '关键字',
                filter: false,
                type: 'text',
                editable: false,
                class: 'fucking-youyou',
            },
            value: {
                title: '值',
                filter: false,
                type: 'text',
            },
        },
    }; //}

    source: ServerDataSource;
    constructor(private toastrService: NbToastrService,
                private sysParamService: SysParamService,
                private http: HttpClient,
                private windowService: NbWindowService) {}

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    ngOnInit(): void {
        this.source = new ServerDataSource(this.http, {
            endPoint: RESTfulAPI.SysParam.getPage,
            pagerPageKey: 'pageno',
            pagerLimitKey: 'size',
            sortFieldKey: 'sort',
            sortDirKey: 'sortDir',
            filterFieldKey: '#field#',
            dataKey: 'pairs',
            totalKey: 'total',
        });

        (this.source as any).find = function (element: SystemParameter | string): Promise<any> {
            if (typeof element == 'object') {
                element = element.key;
            }
            const found = (this.data as SystemParameter[]).find(elem => elem.key == element);
            if(found) {
                return Promise.resolve(found);
            } else {
                return Promise.reject("NOT FOUND");
            }
        }

        this.destroy$ = new Subject();
        interval(1000).pipe(takeUntil(this.destroy$))
            .subscribe(() => {
                UglyInputHint(input => input.placeholder == '关键字',
                              async input => {
                                  if (isEmpty(input.value)) {
                                      return '此字段不为空';
                                  }

                                  if (!this.keyRegex.test(input.value)) {
                                      return this.keyRegex[Pattern.HintSym];;
                                  }

                                  try {
                                      if(await this.source.find(input.value) != null)
                                          return '系统参数已存在';
                                  } catch {}

                                  return null;
                              },
                              input => isEmpty(input.value) && '此字段不为空' || null,
                );
            });
    }

    onsearchinput(pair: [string, (hints: string[]) => void]) //{
    {
        const input = pair[0];
        const hook = pair[1];

        const data: SystemParameter[] = (this.source as any).data;
        let ans = [];
        if(data && input.trim().length > 0) {
            data.forEach((value) => {
                if(value.key.match(input)) {
                    ans.push(value.key);
                }
                if(value.remark.match(input)) {
                    ans.push(value.remark);
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

    private nameRegex = Pattern.Regex.uname;
    private keyRegex = Pattern.Regex.aname;
    private async checkData(row: SystemParameter, toastr: boolean = true): Promise<boolean> //{
    {
        if(isEmpty(row.remark) || isEmpty(row.key) || isEmpty(row.value)) {
            this.toastrService.warning(`名称、关键字和值必填`, "系统参数");
            return false;
        }

        if(!this.nameRegex.test(row.remark) && toastr) {
            this.toastrService.warning(`非法名称字段: ${this.nameRegex[Pattern.HintSym]}`, "系统参数");
            return false;
        }
        if(!this.keyRegex.test(row.key)) {
            this.toastrService.warning(`非法关键字字段: ${this.keyRegex[Pattern.HintSym]}`, "系统参数");
            return false;
        }
        return true
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
        } catch (e: any) {
            httpErrorHandler(e, "系统参数", `创建系统参数'${event.newData.key}'失败`);
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
        } catch (e: any) {
            httpErrorHandler(e, "系统参数", `编辑系统参数'${event.newData.key}'失败`);
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
            const win = this.windowService.open(ConfirmWindowComponent, {
                title: `删除系统参数`,
                context: {message: `确认删除关键字为 '${event.data.key}' 的系统参数?`}
            });
            await win.onClose.toPromise();
            if (!win.config.context['isConfirmed'])
                return;

            await this.sysParamService.delete(event.data.key);
            this.toastrService.success(`删除'${event.data.key}'`, "系统参数");
        } catch (e: any) {
            httpErrorHandler(e, "系统参数", `删除系统参数'${event.data.key}'失败`);
            return event.confirm.reject();
        }

        event.confirm.resolve(event.data);
    } //}
}

