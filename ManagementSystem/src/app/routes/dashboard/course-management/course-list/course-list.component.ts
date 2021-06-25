import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NbToastrService } from '@nebular/theme';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { Course, User } from 'src/app/entity';
import { CourseService } from 'src/app/service/course/course.service';
import { UserService } from 'src/app/service/user/user.service';
import { httpErrorHandler } from 'src/app/shared/utils';

@Component({
    selector: 'ngx-course-list',
    templateUrl: './course-list.component.html',
    styleUrls: ['./course-list.component.scss']
})
export class CourseListComponent implements OnInit, OnDestroy {
    private $destroy = new Subject<void>();
    private _pageno: number = 1;
    total: number = 0;
    courses: Course[] = [];
    pagesize: number = 10;
    searchWildcard: string = '';
    get pageno(): number {return this._pageno;}
    set pageno(no: number) {
        this.gotoPage(no);
    }

    constructor(private courseService: CourseService,
                private router: Router,
                private toastrService: NbToastrService,
                private activatedRoute: ActivatedRoute) {
    }

    ngOnDestroy(): void {
        this.$destroy.next();
        this.$destroy.complete();
    }

    ngOnInit(): void {
        this.gotoPage(1);
    }

    onsearchinput(pair: [string, (hints: string[]) => void]) //{
    {
        const input = pair[0].trim();
        const hook = pair[1];

        let ans = [];
        if(input.trim().length > 0) {
            this.courses.forEach(course => {
                if(course.courseName.match(input)) {
                    ans.push(course.courseName);
                }
                if(course.teacher.userName.match(input)) {
                    ans.push(course.teacher.userName);
                }
                if(course.teacher.name && course.teacher.name.match(input)) {
                    ans.push(course.teacher.name);
                }
            });
        }

        hook(ans);
    } //}
    onsearchenter(search: string) //{
    {
        this.searchWildcard = search.trim();
        this.gotoPage(1);
    } //}

    async gotoPage(pageno: number) {
        try {
            const pages = await this.courseService.getPage({
                pageno: pageno,
                size: this.pagesize,
                searchWildcard: this.searchWildcard,
            });
            this.total= pages.total;
            this.courses = pages.pairs;
            this._pageno = pageno;
        } catch (err) {
            httpErrorHandler(err, "课程管理", `获取页面失败`);
        }
    }

    async gotoCourseDetail(courseExId: string) {
        await this.router.navigate(['../course-info'], {
            relativeTo: this.activatedRoute,
            queryParams: { courseExId: courseExId }
        });
    }
}

