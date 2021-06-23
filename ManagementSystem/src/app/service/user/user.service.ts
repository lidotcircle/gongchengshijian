import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { concatMap, map, tap } from 'rxjs/operators';
import { User } from 'src/app/entity/User';
import { AuthService } from '../auth';
import { RESTfulAPI } from '../restful';
import { LocalStorageService, SessionStorageService, StorageKeys } from '../storage';


@Injectable({
    providedIn: 'root'
})
export class UserService {
    private userSub: Subject<User>;
    private user: User;
    public getUser(): Observable<User> {
        return new Observable(subscriber => {
            if(this.user) {
                subscriber.next(this.user);
            }

            return this.userSub.subscribe(subscriber);
        });
    }

    constructor(private authService: AuthService,
                private http: HttpClient) 
    {
        this.userSub = new Subject();

        this.authService.getJwtClaim()
            .pipe(concatMap(() => this.http.get(RESTfulAPI.User.info)))
            .pipe(map(value => Object.create(User.prototype, Object.getOwnPropertyDescriptors(value))))
            .pipe(tap(user => this.user = user))
            .subscribe(this.userSub);
    }

    private trigger() {
        this.userSub.next(this.user);
    }

    async updateUser(diff: object) {
        const updated = await this.http.put(RESTfulAPI.User.update, diff).toPromise() as string[];
        for(const prop of updated) {
            this.user[prop] = diff[prop];
        }
        this.trigger();
    }

    async updateUserPrivileged(password: string, diff: object) {
        diff['requiredPassword'] = password;
        const updated = await this.http.put(RESTfulAPI.User.updatePrivileged, diff).toPromise() as string[];
        diff['password'] = null;
        diff['requiredPassword'] = null;
        for(const prop of updated) {
            this.user[prop] = diff[prop];
        }
        this.trigger();
    }
}

