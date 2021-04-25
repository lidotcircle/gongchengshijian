import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { PermMenu, Role } from 'src/app/entity';
import { RESTfulAPI } from '../restful';


@Injectable({
    providedIn: 'root'
})
export class PermMenuService {
    private duk: Subject<void>;
    get menuDuk(): Observable<void> {
        return new Observable(subscriber => {
            return this.duk.subscribe(subscriber);
        });
    }

    constructor(private http: HttpClient) {
        this.duk = new Subject<void>();
    }

    async getTree(): Promise<PermMenu[]> //{
    {
        const menus = await this.http.get(RESTfulAPI.Role.Perm.getTree).toPromise() as PermMenu[];
        const ans: PermMenu[] = [];
        for(const menu of menus) {
            ans.push(PermMenu.fromObject(menu));
        }

        return ans;
    } //}

    async getEntries(roleName: string): Promise<PermMenu[]> //{
    {
        const ans: PermMenu[] = [];
        const menus = await this.http.get(RESTfulAPI.Role.Perm.getRolePerms, {
            params: {
                roleName: roleName
            }
        }).toPromise() as PermMenu[];

        for(const menu of menus) {
            ans.push(PermMenu.fromObject(menu));
        }

        return ans;
    } //}

    async getRoles(descriptor: string): Promise<string[]> //{
    {
        const ans = await this.http.get(RESTfulAPI.Role.Perm.getPermRoles, {
            params: {
                descriptor: descriptor
            }
        }).toPromise() as string[];
        return ans;
    } //}
    
    async create(parentDescriptor: string, newPE: PermMenu) //{
    {
        if(parentDescriptor) {
            newPE['parentDescriptor'] = parentDescriptor;
        }
        await this.http.post(RESTfulAPI.Role.Perm.post, newPE).toPromise();
        this.duk.next();
    } //}

    async update(PE: PermMenu): Promise<void> //{
    {
        delete PE.children;
        await this.http.put(RESTfulAPI.Role.Perm.put, PE).toPromise();
        this.duk.next();
    } //}

    async delete(descriptor: string): Promise<void> //{
    {
        await this.http.delete(RESTfulAPI.Role.Perm.deletePerm, {
            params: {
                descriptor: descriptor
            }
        }).toPromise();
        this.duk.next();
    } //}

    async test(roleName: string, descriptor: string): Promise<boolean> //{
    {
        try {
            await this.http.get(RESTfulAPI.Role.Perm.hasPerm, {
                params: {
                    descriptor: descriptor,
                    roleName: roleName,
                }
            }).toPromise();
            return true;
        } catch (err) {
            if(err instanceof HttpErrorResponse && err.status == 404) {
                return false;
            }

            throw err;
        }
    } //}

    async enable(roleName: string, descriptor: string, recursive: boolean = false): Promise<void> //{
    {
        await this.http.post(RESTfulAPI.Role.Perm.enable, {
            roleName: roleName,
            descriptor: descriptor,
            recursive: recursive,
        }).toPromise();
    } //}
    async disable(roleName: string, descriptor: string, recursive: boolean = false): Promise<void> //{
    {
        await this.http.delete(RESTfulAPI.Role.Perm.disable, {
            params: {
                roleName: roleName,
                descriptor: descriptor,
                recursive: `${recursive}`,
            }
        }).toPromise();
    } //}
}

