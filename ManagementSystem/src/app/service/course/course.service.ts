import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { CheckinAnwser, Course, User } from 'src/app/entity';
import { RESTfulAPI } from 'src/app/service/restful';
import { UserService } from 'src/app/service/user/user.service';
import { DYMenuService } from '../menu/dy-menu.service';


export interface CoursePageResp {
    pairs: Course[];
    total: number;
}

@Injectable({
    providedIn: 'root'
})
export class CourseService {
    private user: User;
    private suffix: string = '';
    private initd: Promise<void>;
    private supermereciver: Subject<boolean>;

    constructor(private http: HttpClient,
                private userService: UserService,
                private dymenuService: DYMenuService) {
        this.user = new User();
        this.supermereciver = new Subject();

        this.userService.getUser()
            .subscribe(user => this.user = user);

        let resolved = false;
        let resolve;
        this.initd = new Promise((rf) => {
            if(!resolved) {
                resolve = rf;
            } else {
                rf();
            }
        });

        this.dymenuService.menuOnReady()
            .subscribe(() => {
                const superme = this.dymenuService.testDescriptorExpr(`
                    coursesuper.page    &&
                    coursesuper.get     &&
                    coursesuper.put     &&
                    coursesuper.post    &&
                    coursesuper.delete  &&
                    coursesuper.checkin &&
                    coursesuper.task
                `);
                this.suffix = superme ? '/super' : '';
                this.supermereciver.next(superme);

                if(!resolved) {
                    resolve();
                    resolved = true;
                }
            }, () => {
                if(!resolved) {
                    resolve();
                    resolved = true;
                }
            });
    }

    getSuperme(): Observable<boolean> {
        return new Observable(subscriber => {
            subscriber.next(this.suffix != '');
            return this.supermereciver.subscribe(subscriber);
        });
    }

    async get(courseExId: string): Promise<Course> {
        await this.initd;
        const ans = await this.http.get(RESTfulAPI.Course.get + this.suffix, {
            params: {
                courseExId: courseExId
            }
        }).toPromise();

        return Course.fromObject(ans);
    }

    async delete(courseExId: string): Promise<void> {
        await this.initd;
        await this.http.delete(RESTfulAPI.Course.deleteCourse + this.suffix, {
            params: {
                courseExId: courseExId
            }
        }).toPromise();
    }

    async post(course: {
        courseName: string;
        briefDescription?: string;
    }): Promise<string> {
        const ans = await this.http.post(RESTfulAPI.Course.post, {
            courseName: course.courseName, 
            briefDescription: course.briefDescription
        }).toPromise();
        return ans['courseExId'];
    }

    async put(course: {
        courseExId: string;
        courseName?: string;
        briefDescription?: string;
    }): Promise<void> {
        await this.initd;
        const ans = await this.http.put(RESTfulAPI.Course.put + this.suffix, course).toPromise();
    }


    async getPage(request: {
        pageno?: number;
        size?: number;
        sortDir?: string;
        sortKey?: string;
        searchWildcard?: string;
    }): Promise<CoursePageResp> {
        await this.initd;
        const req = Object.assign({}, request) as {[key: string]: string};
        req.pageno = req.pageno && `${req.pageno}`;
        req.size   = req.size && `${req.size}`;

        const resp = await this.http.get(RESTfulAPI.Course.getPage + this.suffix, {
            params: req
        }).toPromise() as CoursePageResp;

        if(resp.pairs) {
            resp.pairs = resp.pairs
                .map(value => Course.fromObject(value));
        }

        return resp;
    }

    async invite(courseExId: string, studentName: string) {
        await this.initd;
        await this.http.post(RESTfulAPI.Course.invite + this.suffix, {
            courseExId: courseExId,
            studentName: studentName,
        }).toPromise();
    }

    async deleteStudent(courseExId: string, studentName: string) {
        await this.initd;
        await this.http.delete(RESTfulAPI.Course.deleteStudent + this.suffix, {
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

    async getCheckinList(checkinId: number) {
        return await this.http.get(RESTfulAPI.Course.Checkin.anwserList, {
            params: {
                checkinId: `${checkinId}`
            }
        }).toPromise() as CheckinAnwser[];
    }
}

