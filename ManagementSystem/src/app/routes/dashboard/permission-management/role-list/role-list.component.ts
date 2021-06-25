import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { NbToastrService, NbWindowService } from '@nebular/theme';
import { LocalDataSource } from 'ng2-smart-table';
import { Observable, from } from 'rxjs';
import { Role } from 'src/app/entity';
import { RoleService } from 'src/app/service/role/role.service';
import { ConfirmWindowComponent } from 'src/app/shared/components/confirm-window.component';
import { httpErrorHandler } from 'src/app/shared/utils';
import { ButtonsCellComponent } from './buttons-cell.component';

@Component({
    selector: 'ngx-role-list',
    templateUrl: './role-list.component.html',
    styleUrls: ['./role-list.component.scss'],
    encapsulation: ViewEncapsulation.None,
})
export class RoleListComponent implements OnInit {

    // settings //{
    settings = {
        actions: {
            columnTitle: '操作',
            add: true,
            edit: false,
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
                filter: false,
                type: 'text',
            },
            createdDate: {
                title: '创建日期',
                filter: false,
                editable: false,
                type: 'text',
            },
            modifiedDate: {
                title: '修改日期',
                filter: false,
                editable: false,
                type: 'text',
            },
        },
    }; //}

    source: LocalDataSource;

    constructor(private toastrService: NbToastrService,
                private windowService: NbWindowService,
                private roleService: RoleService) {
        this.source = new LocalDataSource();
        this.refresh();
    }

    ngOnInit(): void {
    }

    private refresh() {
        from(this.roleService.getList()).subscribe(list => {
            this.source.load(list);
        }, error => {
            httpErrorHandler(error, "角色管理", "加载角色列表失败");
        });
    }

    private static nameRegex = /.{2,10}/;
    private async checkData(name: string): Promise<boolean> //{
    {
        const nameTestFail = name == null || !RoleListComponent.nameRegex.test(name);

        if(nameTestFail) {
            this.toastrService.danger("错误的角色名", '角色管理');
        }

        return !nameTestFail;
    } //}


    async onCreateConfirm(event: {
        newData: Role, 
        source: LocalDataSource,
        confirm: {resolve: (data: Role) => void, reject: () => void},
    }) //{
    {
        if(!(await this.checkData(event.newData.roleName))) {
            event.confirm.reject();
            return;
        }

        try {
            await this.roleService.post(event.newData.roleName); 
            event.confirm.resolve(event.newData);
        } catch (err) {
            httpErrorHandler(err, "角色管理", "新增角色失败");
            event.confirm.reject();
            event.source.remove(event.newData);
        } finally {
            this.refresh();
        }
    } //}


    async onDeleteConfirm(event: {
        data: Role, 
        source: LocalDataSource,
        confirm: {resolve: (data: Role) => void, reject: () => void},
    }) //{
    {
        const win = this.windowService.open(ConfirmWindowComponent, {
            title: `删除 ${event.data.roleName}`,
            context: {}
        });
        await win.onClose.toPromise();
        if(win.config.context['isConfirmed']) {
            try {
                await this.roleService.delete(event.data.roleName);
                event.confirm.resolve(event.data);
            } catch (err) {
                httpErrorHandler(err, "角色管理", "删除角色失败");
                event.confirm.reject();
            }
        }
    } //}
}

