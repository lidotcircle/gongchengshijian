import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DaoyunRoutingModule } from './routes-routing.module';
import { DaoyunComponent } from './daoyun.component';
import { SharedModule } from '../shared/shared.module';
import { ThemeModule } from '../@theme/theme.module';
import { NbMenuModule } from '@nebular/theme';
import { LayoutModule } from '../layout/layout.module';


@NgModule({
    declarations: [DaoyunComponent],
    imports: [
        CommonModule,
        DaoyunRoutingModule,
        SharedModule,
        LayoutModule,

        ThemeModule,
        NbMenuModule,
    ]
})
export class DaoyunModule { }

