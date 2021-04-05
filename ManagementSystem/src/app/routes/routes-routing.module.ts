import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
    {
        path: 'auth',
        loadChildren: () => import('./password/password.module')
        .then(m => m.PasswordModule),
    },
    {

        path: 'dashboard',
        loadChildren: () => import('./dashboard/dashboard.module')
        .then(m => m.DashboardModule),
    },
    {

        path: 'exception',
        loadChildren: () => import('./exception/exception.module')
        .then(m => m.ExceptionModule),
    },
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'auth'
    },
    {
        path: '**',
        redirectTo: 'exception',
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DaoyunRoutingModule { }

