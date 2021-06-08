export * from './utils/ugly-hint';


export function assert(expr: boolean, msg?: string) {
    if(!expr) {
        if(msg) {
            console.error(msg);
        }
        throw new Error("assert fail");
    }
}

export const API_ADDRESS = window.location.protocol.toLowerCase() == 'http:' ? "http://192.168.44.43:8099" : window.location.origin;

export function computeDifference(newObj: object, oldObj: object): object | null {
    const ans = Object.create({});
    let i=0;

    for(const prop of Object.getOwnPropertyNames(newObj)) {
        if(newObj[prop] != oldObj[prop]) {
            ans[prop] = newObj[prop];
            i++;
        }
    }

    return i==0 ? null : ans;
}

export function createNodeFromHtmlString(htmlText: string): HTMLElement
{
    let div = document.createElement("div");
    div.innerHTML = htmlText.trim();
    return div.firstChild as HTMLElement;
}

export module Pattern {
    export const HintSym = Symbol('Hint');

    export const password: String = new String(`.{6,}`);
    password[HintSym] = '密码至少拥有6个字符';

    export const username: String = new String(`[a-zA-Z][\\w_]{1,32}`);
    username[HintSym] = '用户名由字母数字下划线组成且首字符为字母, 不少于2个字符不多于32字符';

    export const name: String = new String(`^([A-Za-z]|\\p{Unified_Ideograph})([A-Za-z0-9_]|\\p{Unified_Ideograph}){1,7}$`);
    name[HintSym] = '名称由中文字母数字下划线组成, 2-8字符, 中文或者字母开头';

    export const email: String = new String(`^.*[@].*\\..*$`);

    export const phone: String = new String(`^\\d{11}$`);
    phone[HintSym] = '请输入合法的11位手机号';

    export module Regex {
        export const uname = /^([a-zA-Z]|\p{Unified_Ideograph})(\w|\p{Unified_Ideograph}){1,7}$/u;
        uname[HintSym] = '长度为2~8的中文 字母 数字 下划线组合, 首字符为中文或字母';
        export const uname1 = /^([a-zA-Z]|\p{Unified_Ideograph})(\w|\p{Unified_Ideograph}){0,7}$/u;
        uname1[HintSym] = '长度为1~8的中文 字母 数字 下划线组合, 首字符为中文或字母';
        export const aname = /^[a-zA-Z]\w{1,9}$/;
        aname[HintSym] = '长度为2~10的字母 数字 下划线组合, 首字符为中文或字母';
        export const dvalue = /[0-9]{1,}/;
        dvalue[HintSym] = '从0开始的整数';
    }
}

export function sortObject(props: {prop: string; asc?: boolean;}[]) {
    return function(obja: object, objb: object): number {
        for(const {prop, asc} of props) {
            const v1 = obja[prop];
            const v2 = objb[prop];
            if(v1 === v2) continue;
            const one = asc ? -1 : 1;
            if(v1 == null && v2 != null) {
                return -one;
            }
            if(v2 == null) {
                return one;
            }
            switch(typeof v1) {
                case 'boolean':
                case 'string':
                case 'bigint':
                case 'number':
                    return v1 > v2 ? one : -one;
                case 'object':
                case 'symbol':
                case 'function':
                case 'undefined':
                    continue;
            }
        }

        return 0;
    }
}

