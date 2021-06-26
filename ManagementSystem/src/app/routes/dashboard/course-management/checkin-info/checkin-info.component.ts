import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NbToastrService } from '@nebular/theme';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { CheckinAnwser, Course, User } from 'src/app/entity';
import { CourseService } from 'src/app/service/course/course.service';
import { ObjectTransferService } from 'src/app/service/object-transfer.service';
import { UserService } from 'src/app/service/user/user.service';
import { httpErrorHandler } from 'src/app/shared/utils';


@Component({
    selector: 'ngx-checkin-info',
    templateUrl: './checkin-info.component.html',
    styleUrls: ['./checkin-info.component.scss']
})

export class CheckinInfoComponent implements OnInit, OnDestroy {
    private $destroy = new Subject<void>();
    private checkinId: number;
    pageno: number = 1;
    course: Course;
    total: number = 0;
    pagesize: number = 10;
    searchWildcard: string = '';
    issuper: boolean = false;
    anwserList: CheckinAnwser[];

    constructor(private courseService: CourseService,
                private router: Router,
                private toastrService: NbToastrService,
                private objectTransfer: ObjectTransferService,
                private activatedRoute: ActivatedRoute) {
        this.anwserList = [];
        this.course = new Course();
    }

    ngOnDestroy(): void {
        this.$destroy.next();
        this.$destroy.complete();
    }

    ngOnInit(): void {
        this.activatedRoute.queryParamMap.subscribe(params => {
            const n = parseInt(params.get("object"));
            this.course = this.objectTransfer.fetch(n) || this.course;
            this.checkinId = parseInt(params.get("checkinId"));
            this.fetchList();
        });

        this.courseService.getSuperme()
            .pipe(takeUntil(this.$destroy))
            .subscribe(s => this.issuper = s);
    }

    onsearchinput(pair: [string, (hints: string[]) => void]) //{
    {
        const input = pair[0].trim();
        const hook = pair[1];

        let ans = [];
        if(input.trim().length > 0) {
            this.anwserList.forEach(anwser => {
                if(anwser.studentInfo.name.match(input)) {
                    ans.push(anwser.studentInfo.name);
                }
                if(anwser.studentInfo.userName.match(input)) {
                    ans.push(anwser.studentInfo.userName);
                }
            });
        }

        hook(ans);
    } //}
    onsearchenter(search: string) //{
    {
        this.searchWildcard = search.trim();
        this.fetchList();
    } //}

    async fetchList() {
        try {
            this.anwserList = await this.courseService.getCheckinList(this.checkinId);
            this.pageno = 1;
            this.total = this.anwserList.length;
        } catch (err) {
            httpErrorHandler(err, "签到管理", "获取签到列表失败");
        }
    }
}

