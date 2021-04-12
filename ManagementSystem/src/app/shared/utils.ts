
export * from './utils/types';

export function assert(expr: boolean, msg?: string) {
    if(!expr) {
        if(msg) {
            console.error(msg);
        }
        throw new Error("assert fail");
    }
}

export const API_ADDRESS = "http://192.168.44.43:8099";

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

    export const password: String = new String('.{6,}');
    password[HintSym] = '密码至少拥有6个字符';

    export const username: String = new String('[a-zA-Z][\w_]{4,19}');
    username[HintSym] = '用户名由字母数字下划线组成且首字符为字母, 不少于5个字符不多于20字符';

    export const name: String = new String('^([A-Za-z]|\p{Unified_Ideograph})([A-Za-z0-9_]|\p{Unified_Ideograph}){1,7}$');
    name[HintSym] = '名称由中文字母数字下划线组成, 2-8字符, 中文或者字母开头';

    export const email: String = new String('^.*[@].*\..*$');

    export const phone: String = new String('^\d{11}$');
    phone[HintSym] = '请输入合法的11位手机号';
}

