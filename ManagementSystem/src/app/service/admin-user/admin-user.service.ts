import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { User } from 'src/app/entity';
import { AuthService } from '../auth';
import { RESTfulAPI } from '../restful';
import { LocalStorageService, SessionStorageService, StorageKeys } from '../storage';


@Injectable({
    providedIn: 'root'
})
export class AdminUserService {
    constructor(private http: HttpClient) {}

    async get(userName: string): Promise<User> {
        const ans = await this.http.get(RESTfulAPI.AdminUser.get, {
            params: {
                userName: userName
            }
        }).toPromise();
        return Object.create(User.prototype, Object.getOwnPropertyDescriptors(ans));
    }

    async put(User: User): Promise<void> {
        await this.http.put(RESTfulAPI.AdminUser.put, User).toPromise();
    }

    async post(User: User): Promise<void> {
        await this.http.post(RESTfulAPI.AdminUser.put, User).toPromise();
    }

    async delete(userName: string): Promise<void> {
        await this.http.delete(RESTfulAPI.AdminUser.deleteUser, {
            params: {
                userName: userName
            }
        }).toPromise();
    }
}

