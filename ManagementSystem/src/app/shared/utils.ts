
export * from './utils/types';

export function assert(expr: boolean, msg?: string) {
    if(!expr) {
        if(msg) {
            console.error(msg);
        }
        throw new Error("assert fail");
    }
}

