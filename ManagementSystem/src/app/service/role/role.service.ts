import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs/operators';
import { Role } from 'src/app/entity';
import { RESTfulAPI } from '../restful';


@Injectable({
    providedIn: 'root'
})
export class RoleService {
    constructor(private http: HttpClient) {
    }

    async post(roleName: string) {
        await this.http.post(RESTfulAPI.Role.post, {
            roleName: roleName
        }).toPromise();
    }

    async delete(roleName: string) {
        await this.http.delete(RESTfulAPI.Role.deleteRole, {
            params: {
                roleName: roleName
            }
        }).toPromise();
    }

    async getList(): Promise<Role[]> {
        let ans = 
            await this.http.get(RESTfulAPI.Role.getList).toPromise() as Role[];

        ans = ans.map(role => Role.fromObject(role));
        return ans;
    }
}

