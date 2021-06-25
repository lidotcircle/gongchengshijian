import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { concatMap, map } from 'rxjs/operators';
import { User } from 'src/app/entity';
import { RESTfulAPI } from 'src/app/service/restful';
import { UserService } from 'src/app/service/user/user.service';
import { parse, expreval } from 'src/app/shared/utils';


@Injectable({
    providedIn: 'root'
})
export class DYMenuService {
    private permitContext: object;
    private refresh: Subject<void>;

    constructor(private http: HttpClient,
                private userService: UserService) {
        this.refresh = new Subject();

        this.userService.getUser()
            .pipe(concatMap(user => this.http.get(RESTfulAPI.User.descriptors) as Observable<string[]>))
            .pipe(map(descs => {
                this.permitContext = {};

                for(const desc of descs) {
                    let obj = this.permitContext;
                    const pps = desc.split('.');
                    for(let i=0;i<pps.length;i++) {
                        const id = pps[i];
                        obj = obj[id] = obj[id] || {};
                        if (i == pps.length - 1) {
                            obj['enable'] = true;
                        }
                    }
                }
            })).subscribe(this.refresh);
    }

    public menuOnReady(): Observable<void> {
        return new Observable(subscriber => {
            if(this.permitContext) {
                subscriber.next();
            }

            return this.refresh.subscribe(subscriber);
        });
    }

    testDescriptorExpr(expr: string): boolean {
        const ast = parse(expr);
        return !!expreval(ast, this.permitContext);
    }
}

