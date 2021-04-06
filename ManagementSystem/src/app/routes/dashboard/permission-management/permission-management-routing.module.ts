import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ButtonManagementComponent } from './button-management/button-management.component';
import { FileManagementComponent } from './file-management/file-management.component';
import { MenuManagementComponent } from './menu-management/menu-management.component';
import { RoleListComponent } from './role-list/role-list.component';

const routes: Routes = [
    {
        path: 'roles',
        component: RoleListComponent,
    }, 
    {
        path: 'menu',
        component: MenuManagementComponent,
    },
    {
        path: 'button',
        component: ButtonManagementComponent,
    },
    {
        path: 'file',
        component: FileManagementComponent,
    },
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'roles',
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class PermissionManagementRoutingModule { }
