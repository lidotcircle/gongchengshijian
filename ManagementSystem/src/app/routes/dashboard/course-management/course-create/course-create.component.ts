import { Component, OnInit } from '@angular/core';
import { NbToastrService } from '@nebular/theme';
import { Course } from 'src/app/entity';
import { CourseService } from 'src/app/service/course/course.service';

@Component({
    selector: 'ngx-course-create',
    templateUrl: './course-create.component.html',
    styleUrls: ['./course-create.component.scss']
})
export class CourseCreateComponent implements OnInit {
    course: Course;

    constructor(private courseService: CourseService,
                private toastrService: NbToastrService) {
        this.course = new Course();
    }

    ngOnInit(): void {
    }

    async addCourse() {
        try {
            await this.courseService.post(this.course);
            this.course.courseName = '';
            this.course.briefDescription = '';
            this.toastrService.info("创建班课成功", "班课管理");
        } catch (e) {
            this.toastrService.danger("创建班课失败", "班课管理");
        }
    }
}

