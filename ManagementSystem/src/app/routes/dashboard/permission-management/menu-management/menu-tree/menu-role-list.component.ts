import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NbToastrService, NbWindowRef, NbWindowService } from '@nebular/theme';
import { from } from 'rxjs';
import { PermMenu } from 'src/app/entity';
import { PermMenuService } from 'src/app/service/role/perm-menu.service';
import { httpErrorHandler } from 'src/app/shared/utils';

@Component({
    selector: 'ngx-menu-role-list',
    template: `
    <nb-card size='medium' accent='primary'>
      <nb-card-header>
        <div class="kv">
            <div class="key">名称</div>
            <div class="value">{{ menu.permEntryName }}</div>
        </div>
        <div class="kv">
            <div class="key">描述</div>
            <div class="value">{{ menu.descriptor }}</div>
        </div>
        <div class="kv">
            <div class="key">链接</div>
            <div class="value">{{ menu.link }}</div>
        </div>
      </nb-card-header>

      <nb-card-body>
      <nb-list>
        <nb-list-item *ngFor="let role of roleList; let i=index">
          <nb-user [name]="role" [title]="role">
          </nb-user>
          <div class="take-space"></div>
          <button nbButton status='primary' hero (click)='viewRole(i)'>查看</button>
          <button nbButton status='danger' hero (click)='deleteRole(i)'
                  [nbSpinner]='inDelete' nbSpinnerSatus='info' nbSpinnerSize='medium'>删除</button>
        </nb-list-item>
      </nb-list>
      </nb-card-body>
    </nb-card>
    `,
    styles: [
    `
      nb-card {
        overflow: scroll;
        min-width: 25em;
      }

      .kv {
        display: flex;
        flex-direction: row;
        align-items: center;
      }

      .key {
        padding: 0em 1em 0em 0.3em;
      }

      nb-list-item {
        padding: 0.6em 0.8em;
        box-sizing: border-box;
        display: flex;
        flex-direction: row;
        align-items: center;
      }

      .take-space {
        flex-grow: 1;
      }

      button {
        margin: 0em 0em 0em 0.3em;
      }
    `
    ],
})
export class MenuRoleListComponent implements OnInit {
    @Input()
    menu: PermMenu;
    inDelete: boolean = false;

    roleList: string[];

    constructor(private windowService: NbWindowService,
                private windowRef: NbWindowRef,
                private permMenuService: PermMenuService,
                private toastrService: NbToastrService,
                private activatedRoute: ActivatedRoute,
                private router: Router) { }

    ngOnInit(): void {
        this.roleList = [];
        from(this.permMenuService.getRoles(this.menu.descriptor))
            .subscribe(list => this.roleList = list, 
                       err => httpErrorHandler(err, "菜单管理", "获取角色列表失败"));
    }

    async deleteRole(n: number) {
        if(this.inDelete) return;
        this.inDelete = true;
        const roleName = this.roleList[n];

        try {
            await this.permMenuService.disable(roleName, this.menu.descriptor);
            this.roleList.splice(n, 1);
        } catch (err) {
            httpErrorHandler(err, "菜单管理", "删除失败");
        } finally {
            this.inDelete = false;
        }
    }

    viewRole(n: number) {
        this.router.navigate([], {
            relativeTo: this.activatedRoute,
            queryParams: {
                roleName: this.roleList[n]
            },
        });

        this.windowRef.config.context['goto']=this.roleList[n];
        this.windowRef.close();
    }
}

