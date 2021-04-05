import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserAddComponent } from './user-add/user-add.component';
import { UserInfoComponent } from './user-info/user-info.component';
import { UserListComponent } from './user-list/user-list.component';

const routes: Routes = [
    {
        path: 'user-list',
        component: UserListComponent,
    },
    {
        path: 'user-info',
        component: UserInfoComponent,
    },
    {
        path: 'user-add',
        component: UserAddComponent,
    },
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'user-list',
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class UserManagementRoutingModule { }

