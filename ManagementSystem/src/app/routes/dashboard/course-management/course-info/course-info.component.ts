import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NbToastrService, NbWindowService } from '@nebular/theme';
import { Observable, Subject, from, of } from 'rxjs';
import { concatMap, map, takeUntil, tap } from 'rxjs/operators';
import { Course } from 'src/app/entity';
import { CourseCheckIn, CourseInfo, CourseTask, Student } from 'src/app/entity/Course';
import { CourseService } from 'src/app/service/course/course.service';
import { ConfirmWindowComponent } from 'src/app/shared/components/confirm-window.component';
import { sortObject } from 'src/app/shared/utils';
import { BasicInfoEditorComponent } from './basic-info-editor.component';
import { JustInputComponent } from './just-input.component';

@Component({
    selector: 'ngx-course-info',
    templateUrl: './course-info.component.html',
    styleUrls: ['./course-info.component.scss']
})
export class CourseInfoComponent implements OnInit, OnDestroy {
    private $destroy = new Subject<void>();
    course: Course;
    students: Student[] = [];
    tasks: CourseTask[] = [];
    infos: CourseInfo[] = [];
    checkIns: CourseCheckIn[] = [];
    private studentsFilter: string = '';
    private tasksFilter: string = '';
    private infosFilter: string = '';
    private checkInsFilter: string = '';
    private runStudentsFilter() {
        this.students = this.course.Students
            .sort(sortObject([{prop: 'studentTeacherId'}, {prop: 'name'}, {prop: 'userName'}]))
            .filter(student => {
                return this.studentsFilter.trim().length == 0 ||
                    student.userName.match(this.studentsFilter) ||
                    (student.studentTeacherId && student.studentTeacherId.match(this.studentsFilter)) ||
                    (student.name && student.name.match(this.studentsFilter));
            });
    }
    private runTasksFilter() {
        this.tasks = this.course.TaskList.filter(task => {
            return this.tasksFilter.trim().length == 0 ||
                task.match(this.tasksFilter);
        });
    }
    private runInfosFilter() {
        this.infos = this.course.InfoList.filter(info => {
            return this.infosFilter.trim().length == 0 ||
                info.match(this.infosFilter);
        });
    }
    private runCheckInsFilter() {
        this.checkIns = this.course.CheckInList.filter(checkIns => {
            return this.checkInsFilter.trim().length == 0 ||
                checkIns.match(this.checkInsFilter);
        });
    }
    onStudentsEnter(input: string) {
        this.studentsFilter = input;
        this.runStudentsFilter();
    }
    onTasksEnter(input: string) {
        this.tasksFilter = input;
        this.runTasksFilter();
    }
    onInfosEnter(input: string) {
        this.infosFilter = input;
        this.runInfosFilter();
    }
    onCheckInsEnter(input: string) {
        this.checkInsFilter = input;
        this.runCheckInsFilter();
    }

    private async refreshCourseFrom(courseExId: string) //{
    {
        return await of(courseExId)
            .pipe(concatMap(courseExId => this.courseService.get(courseExId)))
            .pipe(tap(course => {
                this.course = course;
                this.runInfosFilter();
                this.runTasksFilter();
                this.runStudentsFilter();
                this.runCheckInsFilter();
            })).toPromise();
    } //}

    constructor(private courseService: CourseService,
                private windowService: NbWindowService,
                private toastrService: NbToastrService,
                private activatedRoute: ActivatedRoute) {
        this.course = new Course();

        this.activatedRoute.queryParamMap
            .pipe(map(params => params.get('courseExId')))
            .subscribe(courseExId => this.refreshCourseFrom(courseExId));
    }

    ngOnDestroy(): void {
        this.$destroy.next();
        this.$destroy.complete();
    }

    ngOnInit(): void {
    }

    async editBasic() //{
    {
        const win = this.windowService.open(BasicInfoEditorComponent, {
            title: '修改课程信息',
            context: {
                courseName: this.course.courseName,
                briefDescription: this.course.briefDescription,
            }
        });
        await win.onClose.toPromise();

        if(win.config.context['isConfirmed']) {
            const { courseName, briefDescription } = win.config.context as any;

            try {
                const req = {
                    courseExId: this.course.courseExId,
                };
                if(courseName) req['courseName'] = courseName;
                req['briefDescription'] = briefDescription || ' ';
                await this.courseService.put(req);
                this.course.courseName = courseName;
                this.course.briefDescription = briefDescription;
                this.toastrService.info('修改成功', '课程管理');
            } catch {
                this.toastrService.danger('修改失败', '课程管理');
            }
        }
    } //}

    async addStudent() //{
    {
        const win = this.windowService.open(JustInputComponent, {
            title: '添加学生',
            context: {
                type: 'text',
                pattern: '.{2,}',
                name: '学生用户名',
            }
        });
        await win.onClose.toPromise();

        if(win.config.context['isConfirmed']) {
            const { value } = win.config.context as any;

            try {
                await this.courseService.invite(this.course.courseExId, value);
                await this.refreshCourseFrom(this.course.courseExId);
                this.toastrService.info('添加学生: ' + value, '课程管理');
            } catch {
                this.toastrService.danger('添加失败', '课程管理');
            }
        }
    } //}
    async deleteStudent(n: number) //{
    {
        const student = this.students[n];
        const win = this.windowService.open(ConfirmWindowComponent, {
            title: `删除 ${student.name || student.userName}`,
            context: {}
        });
        await win.onClose.toPromise();

        if(win.config.context['isConfirmed']) {
            try {
                await this.courseService.deleteStudent(this.course.courseExId, student.userName);
                await this.refreshCourseFrom(this.course.courseExId);
            } catch {
                this.toastrService.info('删除失败 ', '课程管理');
            }
        }
    } //}

    async assignTask() {
    }

    async startCheckIn() {
    }

    async startInfo() {
    }
}

