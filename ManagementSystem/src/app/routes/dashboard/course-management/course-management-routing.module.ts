import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CheckinInfoComponent } from './checkin-info/checkin-info.component';
import { CourseCreateComponent } from './course-create/course-create.component';
import { CourseInfoComponent } from './course-info/course-info.component';
import { CourseListComponent } from './course-list/course-list.component';

const routes: Routes = [
    {
        path: 'course-list',
        component: CourseListComponent,
    },
    {
        path: 'course-create',
        component: CourseCreateComponent,
    },
    {
        path: 'course-info',
        component: CourseInfoComponent,
    },
    {
        path: 'checkin-info',
        component: CheckinInfoComponent,
    },
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'course-list',
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class CourseManagementRoutingModule { }
