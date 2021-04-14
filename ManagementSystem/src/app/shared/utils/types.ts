
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

