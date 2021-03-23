import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardHeaderComponent } from './dashboard-header/dashboard-header.component';
import { NbAlertModule, NbButtonModule, NbCardModule, NbCheckboxModule, NbInputModule } from '@nebular/theme';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ThemeModule } from '../../@theme/theme.module';
import { OptionalCellComponent } from './optional-cell.component';



@NgModule({
    declarations: [DashboardHeaderComponent, OptionalCellComponent],
    imports: [
        CommonModule,
        FormsModule,
        HttpClientModule,
        ThemeModule,
        NbCardModule,
        NbAlertModule,
        NbInputModule,
        NbButtonModule,
        NbCheckboxModule,
    ],
    exports: [
        DashboardHeaderComponent,
        OptionalCellComponent,
    ]
})
export class ComponentsModule { }
