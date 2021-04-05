import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SystemParameterRoutingModule } from './system-parameter-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { ParameterListComponent } from './parameter-list/parameter-list.component';


@NgModule({
    declarations: [ParameterListComponent],
    imports: [
        CommonModule,
        SystemParameterRoutingModule,
        SharedModule,
    ]
})
export class SystemParameterModule { }
