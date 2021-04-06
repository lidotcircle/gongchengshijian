import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { PermMenu } from 'src/app/entity';


@Injectable({
    providedIn: 'root'
})
export class MenuService {
    private _menus: Subject<PermMenu[] | null>;
    get menus(): Observable<PermMenu[] | null> {return this._menus;}

    constructor(private http: HttpClient) {
        this._menus = new Subject();
    }

    refresh() {
        const menus: PermMenu[] = [
            {
                name: 'menu1',
                link: '/apis/auth',
                isMenu: true,
                children: [
                    {
                        name: 'menu11', 
                        link: '/apis/auth/login',
                        isMenu: true,
                        children: [
                            {
                                name: 'menu111',
                                link: '/apis/auth/login/create',
                                isPage: true,
                                children: [
                                    {
                                        name: 'button1',
                                        link: '/apis/auth/login/delete',
                                        isButton: true
                                    }
                                ],
                            },
                        ],
                    },
                ],
            },
            {
                name: 'menu2',
                link: '/apis/auth/signup',
                isMenu: true,
                children: [
                    {
                        name: 'menu21', 
                        link: '/apis/auth/signup',
                        isMenu: true,
                        children: [
                            {
                                name: 'menu211',
                                link: '/apis/auth/signup',
                                isPage: true,
                                children: [
                                    {
                                        name: 'button2',
                                        link: '/apis/auth/signup',
                                        isButton: true
                                    }
                                ],
                            },
                        ],
                    },
                ],
            }
        ];

        this._menus.next(menus);
    }
}

