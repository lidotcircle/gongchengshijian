import { Component, Input, OnInit } from '@angular/core';
import { NbWindowService } from '@nebular/theme';
import { PermMenu } from 'src/app/entity';
import { MenuRoleListComponent } from './menu-role-list.component';

@Component({
    selector: 'ngx-menu-tree',
    templateUrl: './menu-tree.component.html',
    styleUrls: ['./menu-tree.component.scss']
})
export class MenuTreeComponent implements OnInit {
    @Input()
    menu: PermMenu;
    @Input()
    roleName: string;

    icon: string = 'menu-outline';
    show: boolean = false;

    menuIsDisabled: boolean = false;
    menuDefaultIsDisabled: boolean = false;

    constructor(private windowService: NbWindowService) { }

    ngOnInit(): void {
        if(this.menu.isPage) {
            this.icon = 'book-open-outline';
        }
        if(this.menu.isButton) {
            this.icon = 'radio-button-on-outline';
        }
    }

    get hasChild(): boolean {return this.menu.children && this.menu.children.length > 0;}

    async openSubmenu() {
        this.show = true;
    }

    async closeSubmenu() {
        this.show = false;
    }

    async showRoleList() {
        await this.windowService.open(
            MenuRoleListComponent, {
                title: `${this.menu.name} |  ${this.menu.link} | 角色列表`,
                context: { menu: this.menu },
            }).onClose.toPromise();
    }
}

