import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DataDictionaryRoutingModule } from './data-dictionary-routing.module';
import { DictionaryListComponent } from './dictionary-list/dictionary-list.component';
import { DictionaryInfoComponent } from './dictionary-info/dictionary-info.component';
import { AddDictionaryComponent } from './add-dictionary/add-dictionary.component';
import { ClickCellComponent } from './dictionary-list/click-cell.component';
import { SharedModule } from 'src/app/shared/shared.module';


@NgModule({
    declarations: [
        DictionaryListComponent, 
        ClickCellComponent,
        DictionaryInfoComponent, 
        AddDictionaryComponent],
    imports: [
        CommonModule,
        DataDictionaryRoutingModule,
        SharedModule,
    ]
})
export class DataDictionaryModule { }

