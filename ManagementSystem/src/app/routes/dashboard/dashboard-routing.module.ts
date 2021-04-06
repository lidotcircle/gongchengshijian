import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './dashboard.component';

const routes: Routes = [
    {

        path: '',
        component: DashboardComponent,
        children: [
            {
                path: '',
                pathMatch: 'full',
                redirectTo: 'user-info'
            },
            {
                path: 'user-info',
                loadChildren: () => import('./user-info/user-info.module')
                .then(m => m.UserInfoModule),
            },
            {
                path: 'data-dictionary',
                loadChildren: () => import('./data-dictionary/data-dictionary.module')
                .then(m => m.DataDictionaryModule),
            },
            {
                path: 'system-parameter',
                loadChildren: () => import('./system-parameter/system-parameter.module')
                .then(m => m.SystemParameterModule),
            },
            {
                path: 'user-management',
                loadChildren: () => import('./user-management/user-management.module')
                .then(m => m.UserManagementModule),
            },
            {
                path: 'permission-management',
                loadChildren: () => import('./permission-management/permission-management.module')
                .then(m => m.PermissionManagementModule),
            },
            {
                path: 'class-management',
                loadChildren: () => import('./course-management/course-management.module')
                .then(m => m.CourseManagementModule),
            },
        ]
    },

];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DashboardRoutingModule { }
