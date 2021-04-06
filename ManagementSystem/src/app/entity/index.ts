
export function Deserialize<T extends {new(...args: any[])}>(json: string, constructor: T): InstanceType<T> {
    const obj = JSON.parse(json);
    return Object.create(constructor.prototype, Object.getOwnPropertyDescriptors(obj));
}

export { Role } from './Role';
export { PermMenu, ListToTree as PermMenuListToTree } from './Menu';

