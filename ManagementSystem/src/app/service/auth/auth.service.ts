import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { RESTfulAuth } from '../restful';
import { LocalStorageService, StorageKeys } from '../storage';


@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private token: string;

    private jwtSubject: Subject<string | null>;
    get JWT(): Observable<string | null> {return this.jwtSubject;}

    constructor(private localstorage: LocalStorageService,
                private http: HttpClient) {
        this.token = this.localstorage.get<string>(StorageKeys.JWT_TOKEN, null);
        if(this.token) {
            this.jwtSubject.next(this.token);
        }
    }

    async loginByUsername(username: string, password: string): Promise<void> {
        const ans = await this.http.post(RESTfulAuth.login, {
            username: username,
            password: password,
        }).toPromise() as {jwtToken: string};

        this.token = ans.jwtToken;
        if(this.token == null) {
            throw new Error('recieve bad response');
        } else {
            this.jwtSubject.next(this.token);
        }
        this.localstorage.set<string>(StorageKeys.JWT_TOKEN, this.token);
    }

    async logout() {
        this.localstorage.set<string>(StorageKeys.JWT_TOKEN, null);
        this.token = null;

        this.jwtSubject.next(this.token);
        await this.http.delete(RESTfulAuth.login).toPromise();
    }

    get jwtToken() {return this.token;}
}

