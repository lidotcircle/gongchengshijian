import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { Course, User } from 'src/app/entity';
import { CourseInfo, CourseTask } from 'src/app/entity/Course';
import { RESTfulAPI } from 'src/app/service/restful';
import { UserService } from 'src/app/service/user/user.service';


@Injectable({
    providedIn: 'root'
})
export class CourseTaskService {
    constructor(private http: HttpClient) {
    }

    async getTask(taskId: number): Promise<CourseTask> {
        const ans = await this.http.get(RESTfulAPI.Course.Task.get, {
            params: {
                taskId: `${taskId}`
            }
        }).toPromise();

        return Course.obj2Task(ans);
    }

    async deleteTask(taskId: number): Promise<void> {
        await this.http.delete(RESTfulAPI.Course.Task.deleteTask, {
            params: {
                taskId: `${taskId}`
            }
        }).toPromise();
    }

    async postTask(courseTask: CourseTask): Promise<number> {
        const ans = await this.http.post(RESTfulAPI.Course.Task.post, courseTask).toPromise();
        return ans['taskId'];
    }

    async putTask(course: CourseTask): Promise<void> {
        const ans = await this.http.put(RESTfulAPI.Course.Task.put, course).toPromise();
    }

    async getInfo(taskId: number): Promise<CourseInfo> {
        return await this.getTask(taskId);
    }
    async deleteInfo(taskId: number): Promise<void> {
        return await this.deleteTask(taskId);
    }
    async postInfo(info: CourseInfo): Promise<number> {
        return await this.postTask(info as any);
    }
    async putInfo(info: CourseInfo): Promise<void> {
        await this.putTask(info as any);
    }
}

