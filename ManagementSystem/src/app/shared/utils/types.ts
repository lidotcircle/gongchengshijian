
export interface IDataDictionary {
    name: string;
    code: string;
    remark?: string;
}

export interface IDataDictionaryRecord {
    code:          string;
    internalValue: string;
    displayValue:  string;
    isDefault?:     boolean;
    orderNo:       number;
}

export interface ISystemParameter {
    code: string;
    value: string | number | null | boolean;
    remark?: string;
}

export interface ICommonUser {
    name: string;
    nickname?: string;
    role: string;
    gender?: string;
    birthdate?: number;
    usercode?: string;
    university?: string;
    college?: string;
    profession?: string;
    phoneno: string;
    email?: string;
}

