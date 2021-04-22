import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CourseManagementRoutingModule } from './course-management-routing.module';
import { CourseListComponent } from './course-list/course-list.component';
import { CourseInfoComponent } from './course-info/course-info.component';
import { CourseCreateComponent } from './course-create/course-create.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { NgxPaginationModule } from 'ngx-pagination';
import { BasicInfoEditorComponent } from './course-info/basic-info-editor.component';
import { JustInputComponent } from './course-info/just-input.component';


@NgModule({
    declarations: [CourseListComponent, CourseInfoComponent, CourseCreateComponent, BasicInfoEditorComponent, JustInputComponent],
    imports: [
        CommonModule,
        SharedModule,
        NgxPaginationModule,
        CourseManagementRoutingModule
    ]
})
export class CourseManagementModule { }
