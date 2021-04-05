import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { LayoutModule } from 'src/app/layout/layout.module';
import { ThemeModule } from 'src/app/@theme/theme.module';
import { NbMenuModule } from '@nebular/theme';


@NgModule({
    declarations: [ DashboardComponent ],
    imports: [
        CommonModule,
        SharedModule,
        LayoutModule,
        ThemeModule,
        NbMenuModule,
        DashboardRoutingModule,
    ]
})
export class DashboardModule { }
