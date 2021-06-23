import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardHeaderComponent } from './dashboard-header/dashboard-header.component';
import { NbAlertModule, NbButtonModule, NbCardModule, NbCheckboxModule, NbIconModule, NbInputModule } from '@nebular/theme';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ThemeModule } from '../../@theme/theme.module';
import { OptionalCellComponent } from './optional-cell.component';
import { ConfirmWindowComponent } from './confirm-window.component';
import { SearchBarComponent } from './search-bar/search-bar.component';
import { SearchBarFloatComponent } from './search-bar-float/search-bar-float.component';



@NgModule({
    declarations: [ DashboardHeaderComponent, OptionalCellComponent, ConfirmWindowComponent, SearchBarComponent, SearchBarFloatComponent ],
    imports: [
        CommonModule,
        FormsModule,
        HttpClientModule,
        ThemeModule,
        NbCardModule,
        NbAlertModule,
        NbIconModule,
        NbInputModule,
        NbButtonModule,
        NbCheckboxModule,
    ],
    exports: [
        DashboardHeaderComponent,
        OptionalCellComponent,
        SearchBarComponent,
        SearchBarFloatComponent
    ]
})
export class ComponentsModule { }
