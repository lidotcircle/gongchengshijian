import { Component, Input, OnInit } from '@angular/core';
import { NbWindowService } from '@nebular/theme';
import { PermMenu } from 'src/app/entity';

@Component({
    selector: 'ngx-menu-role-list',
    template: `
    <nb-card size='medium'>
      <nb-list>
        <nb-list-item *ngFor="let role of roleList">
          <nb-user [name]="role" [title]="role">
          </nb-user>
          <div class="take-space"></div>
          <button nbButton status='danger' hero>删除</button>
        </nb-list-item>
      </nb-list>
    </nb-card>
    `,
    styles: [
    `
      nb-card {
        overflow: scroll;
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
    `
    ],
})
export class MenuRoleListComponent implements OnInit {
    @Input()
    data: { menu: PermMenu };

    roleList: string[];

    constructor(private windowService: NbWindowService) { }

    ngOnInit(): void {
        this.roleList = [
            '管理员', '不存在', '没道理', '吃饭啦',
            '管理员', '不存在', '没道理', '吃饭啦',
            '管理员', '不存在', '没道理', '吃饭啦',
            '管理员', '不存在', '没道理', '吃饭啦',
            '管理员', '不存在', '没道理', '吃饭啦',
        ];
    }
}

