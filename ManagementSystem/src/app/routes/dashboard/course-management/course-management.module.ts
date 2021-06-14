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
import { CourseInfoViewComponent } from './course-info/course-info-viewer.component';
import { CourseTaskEditorComponent } from './course-info/course-task-editor.component';
import { CourseCheckinEditorComponent } from './course-info/course-checkin-editor.component';


@NgModule({
    declarations: [
        CourseListComponent, CourseInfoComponent, CourseCreateComponent, 
        BasicInfoEditorComponent, JustInputComponent,
        CourseInfoViewComponent, CourseTaskEditorComponent,
        CourseCheckinEditorComponent,
    ],
    imports: [
        CommonModule,
        SharedModule,
        NgxPaginationModule,
        CourseManagementRoutingModule
    ]
})
export class CourseManagementModule { }
