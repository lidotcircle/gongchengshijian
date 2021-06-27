import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NbToastrService } from '@nebular/theme';
import { from, Subject } from 'rxjs';
import { concatMap, takeUntil } from 'rxjs/operators';
import { CheckinAnwser, Course, User } from 'src/app/entity';
import { CourseCheckin } from 'src/app/entity/Course';
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
    checkin: CourseCheckin;
    total: number = 0;
    pagesize: number = 10;
    searchWildcard: string = '';
    issuper: boolean = false;
    originalAnwserList: CheckinAnwser[];
    anwserList: CheckinAnwser[];

    constructor(private courseService: CourseService,
                private router: Router,
                private toastrService: NbToastrService,
                private objectTransfer: ObjectTransferService,
                private activatedRoute: ActivatedRoute) {
        this.anwserList = [];
        this.course = new Course();
        this.checkin = {} as any;
    }

    ngOnDestroy(): void {
        this.$destroy.next();
        this.$destroy.complete();
    }

    ngOnInit(): void {
        this.activatedRoute.queryParamMap.subscribe(params => {
            const courseId = params.get("courseExId");
            this.checkinId = parseInt(params.get("checkinId"));
            from(this.courseService.get(courseId))
                .pipe(concatMap(course => {
                    this.course = course;
                    for(const checkin of this.course.checkins) {
                        if(this.checkinId == checkin.checkinId) {
                            this.checkin = checkin;
                            break;
                        }
                    }

                    return this.fetchList();
                }))
            .subscribe(() => {}, err => {
                httpErrorHandler(err, "签到管理", "获取数据失败");
            });
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
                if(anwser.studentInfo?.name && anwser.studentInfo.name.match(input)) {
                    ans.push(anwser.studentInfo.name);
                }
                if(anwser.studentInfo?.userName && anwser.studentInfo.userName.match(input)) {
                    ans.push(anwser.studentInfo.userName);
                }
            });
        }

        hook(ans);
    } //}
    private _filterStr: string;
    onsearchenter(search: string) //{
    {
        this.searchWildcard = search.trim();
        this._filterStr = search;
        this.fetchList();
    } //}

    async fetchList() {
        if(this.originalAnwserList == null) {
            try {
                this.originalAnwserList = await this.courseService.getCheckinList(this.checkinId);
            } catch (err) {
                httpErrorHandler(err, "签到管理", "获取签到列表失败");
            }
        }

        this.anwserList = this.originalAnwserList.filter(anwser => {
            if(anwser.studentInfo?.name && anwser.studentInfo.name.match(this._filterStr)) {
                return true;
            }
            if(anwser.studentInfo?.userName && anwser.studentInfo.userName.match(this._filterStr)) {
                return true;
            }
        });

        this.pageno = 1;
        this.total = this.anwserList.length;
    }
}

