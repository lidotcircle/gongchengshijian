import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DaoyunRoutingModule } from './routes-routing.module';
import { SharedModule } from '../shared/shared.module';
import { ThemeModule } from '../@theme/theme.module';
import { NbMenuModule } from '@nebular/theme';
import { LayoutModule } from '../layout/layout.module';


@NgModule({
    declarations: [],
    imports: [
        CommonModule,
        DaoyunRoutingModule,
    ]
})
export class DaoyunModule { }

