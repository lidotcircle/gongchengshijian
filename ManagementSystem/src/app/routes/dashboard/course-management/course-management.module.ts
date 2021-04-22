import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CourseManagementRoutingModule } from './course-management-routing.module';
import { CourseListComponent } from './course-list/course-list.component';
import { CourseInfoComponent } from './course-info/course-info.component';
import { CourseCreateComponent } from './course-create/course-create.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { NgxPaginationModule } from 'ngx-pagination';


@NgModule({
    declarations: [CourseListComponent, CourseInfoComponent, CourseCreateComponent],
    imports: [
        CommonModule,
        SharedModule,
        NgxPaginationModule,
        CourseManagementRoutingModule
    ]
})
export class CourseManagementModule { }
