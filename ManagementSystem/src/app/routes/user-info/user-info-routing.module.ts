import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserInfoViewerComponent } from './user-info-viewer/user-info-viewer.component';

const routes: Routes = [
    {
        path: 'viewer',
        component: UserInfoViewerComponent
    },
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'viewer'
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class UserInfoRoutingModule { }
