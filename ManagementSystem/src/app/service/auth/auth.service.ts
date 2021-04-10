import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { RESTfulAuth } from '../restful';
import { LocalStorageService, SessionStorageService, StorageKeys } from '../storage';


@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private refresh_token: string;
    private jwt_token: string;

    private jwtSubject: Subject<string | null>;
    get JWT(): Observable<string | null> {return this.jwtSubject;}

    constructor(private localstorage: LocalStorageService,
                private sessionStorage: SessionStorageService,
                private http: HttpClient) {
        this.refresh_token = this.localstorage.get<string>(StorageKeys.REFRESH_TOKEN, null);
        this.jwt_token = this.sessionStorage.get<string>(StorageKeys.JWT_TOKEN, null);
        this.jwtSubject = new Subject();

        if(this.refresh_token && this.jwt_token) {
            this.jwtSubject.next(this.jwt_token);
        }
    }

    async loginByUsername(username: string, password: string): Promise<void> {
        const ans = await this.http.post(RESTfulAuth.login, {
            userName: username,
            password: password,
        }).toPromise() as {token: string};

        this.refresh_token = ans.token;
        if(this.refresh_token == null) {
            throw new Error('recieve bad response');
        } else {
            this.localstorage.set<string>(StorageKeys.REFRESH_TOKEN, this.refresh_token);
            await this.refreshJWT();
        }
    }

    async logout() {
        this.localstorage.set<string>(StorageKeys.REFRESH_TOKEN, null);

        await this.http.delete(RESTfulAuth.login, {
            params: {
                "refreshToken": this.refresh_token
            }
        }).toPromise();
        this.refresh_token = null;
        this.jwt_token = null;
        this.jwtSubject.next(this.jwt_token);
    }

    async refreshJWT() {
        const ans = await this.http.get(RESTfulAuth.jwt, {
            params: {
                "refreshToken": this.refresh_token
            }
        }).toPromise() as {jwtToken: string};
        this.jwt_token = ans.jwtToken;
        this.sessionStorage.set<string>(StorageKeys.JWT_TOKEN, this.jwt_token);
        this.jwtSubject.next(this.jwt_token);
    }

    get jwtToken() {return this.jwt_token;}
    get isLogin()  {return this.refresh_token != null;}
}

