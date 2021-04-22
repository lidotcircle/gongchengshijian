import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { Course } from 'src/app/entity';
import { CourseService } from 'src/app/service/course/course.service';

@Component({
    selector: 'ngx-course-info',
    templateUrl: './course-info.component.html',
    styleUrls: ['./course-info.component.scss']
})
export class CourseInfoComponent implements OnInit, OnDestroy {
    private $destroy = new Subject<void>();
    private courseUID: number;
    course: Course;

    constructor(private courseService: CourseService,
                private activatedRoute: ActivatedRoute) {
        this.course = new Course();
        this.activatedRoute.queryParamMap
        .subscribe(params => {
            this.courseUID = parseInt(params.get('courseUID'));
            this.refreshCourse();
        });
    }

    private refreshCourse() {
    }

    ngOnDestroy(): void {
        this.$destroy.next();
        this.$destroy.complete();
    }

    ngOnInit(): void {
    }
}

