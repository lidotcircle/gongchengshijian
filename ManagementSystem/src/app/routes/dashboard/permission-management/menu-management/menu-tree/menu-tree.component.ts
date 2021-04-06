import { Component, Input, OnInit } from '@angular/core';
import { PermMenu } from 'src/app/entity';

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

    constructor() { }

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
}

