import { NbMenuItem } from '@nebular/theme';

export class NbMenuItemEx extends NbMenuItem {
    descriptorExpr?: string;
    children?: NbMenuItemEx[];
}

export const DaoyunMenu: NbMenuItemEx[] = [
    {
        title: '角色权限',
        icon: 'star-outline',
        children: [
            {
                title: '角色列表',
                link: '/daoyun/dashboard/permission-management/roles',
                descriptorExpr: 'role.list',
            },
            {
                title: '菜单查看',
                link: '/daoyun/dashboard/permission-management/menu',
                descriptorExpr: 'perm.tree && perm.role',
            },
        ]
    },
    {
        title: '用户管理',
        link: '/daoyun/dashboard/user-management',
        icon: 'person-outline',
        children: [
            {
                title: '用户查看',
                link: '/daoyun/dashboard/user-management/user-list',
                descriptorExpr: 'adminuser.get && adminuser.put && adminuser.delete && adminuser.page',
            },
            {
                title: '添加用户',
                link: '/daoyun/dashboard/user-management/user-add',
                descriptorExpr: 'adminuser.post',
            },
        ]
    },
    {
        title: '班课管理',
        link: '/daoyun/dashboard/class-management',
        icon: 'book-open-outline',
        children: [
            {
                title: '班课查看',
                link: '/daoyun/dashboard/class-management/course-list',
                descriptorExpr: `(course.page && course.get && course.put && course.delete) || 
                                 (coursesuper.get && coursesuper.post && coursesuper.put && coursesuper.delete)`,
            },
            {
                title: '添加班课',
                link: '/daoyun/dashboard/class-management/course-create',
                descriptorExpr: 'course.post || coursesuper.post',
            },
        ]
    },
    {
        title: '数据工具',
        icon: 'map-outline',
        children: [
            {
                title: '数据字典',
                link: '/daoyun/dashboard/data-dictionary/dict-list',
                descriptorExpr: 'datadict.type',
            },
            {
                title: '系统参数',
                link: '/daoyun/dashboard/system-parameter/parameter-list',
                descriptorExpr: 'sysparam.page',
            },
        ]
    },
];

