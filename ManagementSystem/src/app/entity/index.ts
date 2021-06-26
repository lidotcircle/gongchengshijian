
export function Deserialize<T extends {new(...args: any[])}>(json: string, constructor: T): InstanceType<T> {
    const obj = JSON.parse(json);
    return Object.create(constructor.prototype, Object.getOwnPropertyDescriptors(obj));
}

export { Role } from './Role';
export { PermMenu } from './Menu';
export { Course } from './Course';
export { SystemParameter } from './System-Parameter';

export { DictionaryData } from './DictionaryData';
export { DictionaryType } from './DictionaryType';

export { User, defaultProfileImage } from './User';
export * from './CheckinAnwser';


