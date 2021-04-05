import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ParameterListComponent } from './parameter-list/parameter-list.component';

const routes: Routes = [
    {
        path: 'parameter-list',
        component: ParameterListComponent,
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class SystemParameterRoutingModule { }

