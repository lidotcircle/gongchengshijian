import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { Course, User } from 'src/app/entity';
import { CourseInfo, CourseCheckin } from 'src/app/entity/Course';
import { RESTfulAPI } from 'src/app/service/restful';
import { UserService } from 'src/app/service/user/user.service';


@Injectable({
    providedIn: 'root'
})
export class CourseCheckinService {
    constructor(private http: HttpClient) {
    }

    async getCheckin(checkinId: number): Promise<CourseCheckin> {
        const ans = await this.http.get(RESTfulAPI.Course.Checkin.get, {
            params: {
                checkinId: `${checkinId}`
            }
        }).toPromise();

        return Course.obj2Checkin(ans);
    }

    async deleteCheckin(checkinId: number): Promise<void> {
        await this.http.delete(RESTfulAPI.Course.Checkin.deleteCheckin, {
            params: {
                checkinId: `${checkinId}`
            }
        }).toPromise();
    }

    async postCheckin(courseCheckin: CourseCheckin): Promise<number> {
        const postObj = Object.assign({}, courseCheckin);
        delete postObj.checkinId;
        delete postObj.releaseDate;
        const ans = await this.http.post(RESTfulAPI.Course.Checkin.post, postObj).toPromise();
        return ans['checkinId'];
    }

    async putCheckin(checkin: CourseCheckin): Promise<void> {
        const putObj = Object.assign({}, checkin);
        delete putObj.releaseDate;

        const ans = await this.http.put(RESTfulAPI.Course.Checkin.put, putObj).toPromise();
    }
}

