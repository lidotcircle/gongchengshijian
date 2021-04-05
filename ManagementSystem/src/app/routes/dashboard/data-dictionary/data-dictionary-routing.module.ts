import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AddDictionaryComponent } from './add-dictionary/add-dictionary.component';
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
        path: 'add-dict',
        component: AddDictionaryComponent,
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DataDictionaryRoutingModule { }

