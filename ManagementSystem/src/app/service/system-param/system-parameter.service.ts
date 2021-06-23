import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { SystemParameter } from 'src/app/entity';
import { RESTfulAPI } from '../restful';

@Injectable({
    providedIn: 'root'
})
export class SysParamService {
    constructor(private http: HttpClient) {}

    async get(key: string): Promise<SystemParameter> {
        const ans = await this.http.get(RESTfulAPI.SysParam.getParam, {
            params: {
                key: key
            }
        }).toPromise();
        return Object.create(SystemParameter.prototype, Object.getOwnPropertyDescriptors(ans));
    }

    async put(SystemParameter: SystemParameter): Promise<void> {
        await this.http.put(RESTfulAPI.SysParam.putParam, SystemParameter).toPromise();
    }

    async post(SystemParameter: SystemParameter): Promise<void> {
        await this.http.post(RESTfulAPI.SysParam.putParam, SystemParameter).toPromise();
    }

    async delete(key: string): Promise<void> {
        await this.http.delete(RESTfulAPI.SysParam.deleteParam, {
            params: {
                key: key
            }
        }).toPromise();
    }
}

