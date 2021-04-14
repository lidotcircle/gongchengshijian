import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { RESTfulAPI } from '../restful';
import { LocalStorageService, SessionStorageService, StorageKeys } from '../storage';
import { JwtHelperService } from '@auth0/angular-jwt';
import { JwtClaim } from './jwtClaim';


@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private refresh_token: string;
    private jwt_token: string;
    private jwtHelper: JwtHelperService;

    private jwtSubject: Subject<JwtClaim | null>;
    get JwtClaim(): Observable<JwtClaim | null> {return this.jwtSubject;}

    constructor(private localstorage: LocalStorageService,
                private sessionStorage: SessionStorageService,
                private http: HttpClient) {
        this.refresh_token = this.localstorage.get<string>(StorageKeys.REFRESH_TOKEN, null);
        this.refresh_token = this.refresh_token || this.sessionStorage.get<string>(StorageKeys.REFRESH_TOKEN, null);
        this.jwt_token = this.sessionStorage.get<string>(StorageKeys.JWT_TOKEN, null);
        this.jwtSubject = new Subject();
        this.jwtHelper = new JwtHelperService();

        if(this.refresh_token && this.jwt_token) {
            if(!this.jwtHelper.isTokenExpired(this.jwt_token)) {
                this.jwtSubject.next(this.jwtToClaim(this.jwt_token));
            } else {
                this.jwt_token = null;
            }
        }
    }

    private async handleLoginResp(refreshToken: string): Promise<void> {
        this.refresh_token = refreshToken;
        if(this.refresh_token == null) {
            throw new Error('recieve bad response');
        } else {
            this.localstorage.set<string>(StorageKeys.REFRESH_TOKEN, this.refresh_token);
            await this.refreshJWT();
        }
    }

    async loginByUsername(loginreq: {userName: string, password: string, captcha: string}): Promise<void> {
        const ans = await this.http.post(RESTfulAPI.Auth.login, loginreq).toPromise() as {token: string};

        await this.handleLoginResp(ans.token);
    }

    async loginByMessage(loginreq: {phone: string, messageCode: string, messageCodeToken: string}): Promise<void> {
        const ans = await this.http.post(RESTfulAPI.Auth.loginByMessage, loginreq).toPromise() as {token: string};

        await this.handleLoginResp(ans.token);
    }

    async logout() {
        try {
            await this.http.delete(RESTfulAPI.Auth.login, {
                params: {
                    "refreshToken": this.refresh_token
                }
            }).toPromise();
        } finally {
            this.localstorage.remove(StorageKeys.REFRESH_TOKEN);
            this.sessionStorage.remove(StorageKeys.JWT_TOKEN);
            this.refresh_token = null;
            this.jwt_token = null;
            this.jwtSubject.next(null);
        }
    }

    async requestReset(req: {phone: string; messageCode: string; messageCodeToken: string}): Promise<String> {
        return (await this.http.post(RESTfulAPI.Auth.requestReset, 
                                     req).toPromise() as {resetToken: string}).resetToken;
    }

    async reset(req: {resetToken: string, password: string}) {
        await this.http.put(RESTfulAPI.Auth.reset, req).toPromise();
    }

    async signup(req: {phone: string; userName: string; password: string; messageCode: string; messageCodeToken: string}) {
        await this.http.post(RESTfulAPI.Auth.signup, req).toPromise();
    }

    async refreshJWT() {
        const ans = await this.http.get(RESTfulAPI.Auth.jwt, {
            params: {
                "refreshToken": this.refresh_token
            }
        }).toPromise() as {jwtToken: string};
        this.jwt_token = ans.jwtToken;
        this.sessionStorage.set<string>(StorageKeys.JWT_TOKEN, this.jwt_token);
        this.jwtSubject.next(this.jwtToClaim(this.jwt_token));
    }

    private jwtToClaim(jwt: string): JwtClaim {
        const obj = this.jwtHelper.decodeToken(jwt) as object;
        return obj && Object.create(JwtClaim.prototype, Object.getOwnPropertyDescriptors(obj));
    }

    trigger() {
        this.jwtSubject.next(this.jwt_token && this.jwtToClaim(this.jwt_token));
    }

    forgetLogin() {
        this.localstorage.remove(StorageKeys.REFRESH_TOKEN);
        this.sessionStorage.set<string>(StorageKeys.REFRESH_TOKEN, this.refresh_token);
    }

    get jwtToken() {
        if(this.jwtHelper.isTokenExpired(this.jwt_token)) {
            this.jwt_token = null;
        }

        return this.jwt_token;
    }
    get isLogin()  {return this.refresh_token != null;}
}

