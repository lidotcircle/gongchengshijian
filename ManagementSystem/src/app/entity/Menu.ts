import { assert } from 'src/app/shared/utils';

export class PermMenu {
    menuId?: number;
    name: string;
    icon?: string;
    link: string;
    parentId?: number;
    order?: number;
    isMenu?: boolean;
    isPage?: boolean;
    isButton?: boolean;
    isShow?: boolean;

    parent?: PermMenu;
    children?: PermMenu[];
}

export function ListToTree(menus: PermMenu[]): PermMenu {
    const ans = new PermMenu();
    ans.children = [];

    const idMap: Map<number, PermMenu> = new Map();
    for(const menu of menus) {
        assert(!idMap.has(menu.menuId) && typeof menu.menuId === 'number');
        idMap.set(menu.menuId, menu);
    }

    for(const menu of menus) {
        if (idMap.has(menu.parentId) && menu.parentId != menu.menuId) {
            const par = idMap.get(menu.parentId);
            par.children = par.children || [];
            par.children.push(menu);
            menu.parent = par;
        } else {
            ans.children.push(menu);
        }
    }

    for(const menu of menus) {
        if(menu.children) {
            menu.children.sort((a, b) => a.order < b.order ? -1 : 1);
        }
    }

    return ans;
}

