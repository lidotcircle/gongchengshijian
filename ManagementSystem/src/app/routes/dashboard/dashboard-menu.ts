import { NbMenuItem } from '@nebular/theme';

export const DaoyunMenu: NbMenuItem[] = [
    {
        title: '角色管理',
        icon: 'star-outline',
        children: [
            {
                title: '角色列表',
                link: '/daoyun/dashboard/role-management/role-list',
            },
            {
                title: '添加角色',
                link: '/daoyun/dashboard/role-manangement/add-role',
            }
        ]
    },
    {
        title: '权限管理',
        icon: 'menu-outline',
        children: [
            {
                title: '菜单查看',
                link: '/daoyun/dashboard/access-control/menu-list',
            }
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
            },
            {
                title: '添加用户',
                link: '/daoyun/dashboard/user-management/user-add',
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
                link: '/daoyun/dashboard/class-management/class-list',
            },
            {
                title: '添加班课',
                link: '/daoyun/dashboard/class-management/add-class',
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
            },
            {
                title: '系统参数',
                link: '/daoyun/dashboard/system-parameter/parameter-list',
            },
        ]
    },
];

