import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs/operators';
import { RESTfulAPI } from '../restful';

export enum MessageType {
    signup = 'signup',
    login = 'login',
    reset = 'reset'
}

@Injectable({
    providedIn: 'root'
})
export class MessageService {
    constructor(private http: HttpClient) {
    }

    async sendMessageTo(req: {phone: string, captcha: string, type: MessageType}): Promise<string> {
        const ans = await this.http.post(RESTfulAPI.Auth.message, 
                                         req).toPromise() as {codeToken: string};

        return ans.codeToken;
    }
}

