import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DictionaryInfoComponent } from './dictionary-info/dictionary-info.component';
import { DictionaryListComponent } from './dictionary-list/dictionary-list.component';

const routes: Routes = [
    {
        path: 'dict-list',
        component: DictionaryListComponent,
    },
    {
        path: 'dict-info',
        component: DictionaryInfoComponent,
    },
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'dict-list',
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DataDictionaryRoutingModule { }

