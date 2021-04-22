import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { Course, User } from 'src/app/entity';
import { RESTfulAPI } from 'src/app/service/restful';
import { UserService } from 'src/app/service/user/user.service';


export interface CoursePageResp {
    pairs: Course[];
    total: number;
}

@Injectable({
    providedIn: 'root'
})
export class CourseService {
    private user: User;

    constructor(private http: HttpClient,
                private userService: UserService) {
        this.user = new User();

        this.userService.getUser()
            .subscribe(user => this.user = user);
    }

    async get(courseExId: string): Promise<Course> {
        const ans = await this.http.get(RESTfulAPI.Course.get, {
            params: {
                courseExId: courseExId
            }
        }).toPromise();

        return Object.create(Course.prototype, Object.getOwnPropertyDescriptors(ans)) as Course;
    }

    async delete(courseExId: string): Promise<void> {
        await this.http.delete(RESTfulAPI.Course.get, {
            params: {
                courseExId: courseExId
            }
        }).toPromise();
    }

    async post(course: {
        courseName: string;
        briefDescription?: string;
    }): Promise<string> {
        const req = Object.assign({teacherName: this.userService.getUser});

        const ans = await this.http.post(RESTfulAPI.Course.get, req).toPromise();
        return ans['courseExId'];
    }

    async put(course: {
        courseExId: string;
        courseName?: string;
        briefDescription?: string;
    }): Promise<void> {
        const ans = await this.http.put(RESTfulAPI.Course.get, course).toPromise();
    }


    async getPage(request: {
        pageno?: number;
        size?: number;
        sortDir?: string;
        sortKey?: string;
        searchWildcard?: string;
    }): Promise<CoursePageResp> {
        const req = Object.assign({}, request) as {[key: string]: string};
        req.pageno = req.pageno && `${req.pageno}`;
        req.size   = req.size && `${req.size}`;

        // TODO role
        const resp = await this.http.get(RESTfulAPI.Course.getPage, {
            params: req
        }).toPromise() as CoursePageResp;

        if(resp.pairs) {
            resp.pairs = resp.pairs
                .map(value => Object.create(Course.prototype, Object.getOwnPropertyDescriptors(value)));
        }

        return resp;
    }

    async invite(courseExId: string, studentName: string) {
        await this.http.post(RESTfulAPI.Course.invite, {
            courseExId: courseExId,
            studentName: studentName,
        }).toPromise();
    }

    async deleteStudent(courseExId: string, studentName: string) {
        await this.http.delete(RESTfulAPI.Course.deleteStudent, {
            params: {
                courseExId: courseExId,
                studentName: studentName,
            }
        }).toPromise();
    }

    async join(courseExId: string) {
        await this.http.post(RESTfulAPI.Course.join, {
            courseExId: courseExId,
        }).toPromise();
    }
    async exit(courseExId: string) {
        await this.http.delete(RESTfulAPI.Course.exit, {
            params: {
                courseExId: courseExId,
            }
        }).toPromise();
    }
}

