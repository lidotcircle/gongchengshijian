
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

