import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { Course } from 'src/app/entity';
import { CourseService } from '../course.service';

@Component({
    selector: 'ngx-course-list',
    templateUrl: './course-list.component.html',
    styleUrls: ['./course-list.component.scss']
})
export class CourseListComponent implements OnInit, OnDestroy {
    private $destroy = new Subject<void>();
    courses: Course[] = [];

    constructor(private courseService: CourseService,
                private router: Router,
                private activatedRoute: ActivatedRoute) {
        this.courseService.courses
        .pipe(takeUntil(this.$destroy))
        .subscribe(courses => this.courses = courses);
        this.courseService.refresh();
    }

    ngOnDestroy(): void {
        this.$destroy.next();
        this.$destroy.complete();
    }

    ngOnInit(): void {
    }

    async gotoCourseDetail(n: number) {
        await this.router.navigate(['../course-info'], {
            relativeTo: this.activatedRoute,
            queryParams: { courseUID: this.courses[n].courseUID }
        });
    }
}

