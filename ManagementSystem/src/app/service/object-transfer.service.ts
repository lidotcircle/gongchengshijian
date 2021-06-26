import { Injectable } from '@angular/core';


@Injectable({
    providedIn: 'root'
})
export class ObjectTransferService {
    private n: number;
    private objs: {[ key: number ]: any};

    constructor() {
        this.n = 1;
        this.objs = {} as any;
    }

    store(obj: any): number {
        this.objs[++this.n] = obj;
        return this.n;
    }

    fetch(n: number): any {
        const ans = this.objs[n];
        delete this.objs[n];
        return ans;
    }
}

