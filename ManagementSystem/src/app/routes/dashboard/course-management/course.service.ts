import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { Course } from 'src/app/entity';


@Injectable({
    providedIn: 'root'
})
export class CourseService {
    private courses_subject: Subject<Course[] | null>;
    get courses(): Observable<Course[] | null> {return this.courses_subject;}

    private _courses: Course[] = [];

    constructor(private http: HttpClient) {
        this.courses_subject = new Subject();
        this.courses_subject.subscribe(courses => this._courses = courses);
    }

    refresh() {
        const courses: Course[] = [
            Object.create(Course.prototype, Object.getOwnPropertyDescriptors({
                teacher: '张老师',
                courseDescription: '上课地点  西三201',
                courseUID: 1003320,
                courseName: '高等数学B下',
                students: ['张三', '李四', '王五'],
                checkInList: ['位置签到'],
                taskList: ['第一周作业'],
                infoList: ['周一交作业'],
            })),
        ];

        this.courses_subject.next(courses);
    }

    getCourseByUID(courseUID: number): Course {
        for(const course of this._courses) {
            if(course.courseUID == courseUID) {
                return course;
            }
        }
    }
}

