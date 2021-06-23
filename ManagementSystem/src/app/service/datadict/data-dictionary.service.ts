import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs/operators';
import { DictionaryData, DictionaryType } from 'src/app/entity';
import { RESTfulAPI } from '../restful';

@Injectable({
    providedIn: 'root'
})
export class DataDictionaryService {
    constructor(private http: HttpClient) {
    }

    async getType(typeCode: string): Promise<DictionaryType> {
        const ans = await this.http.get(RESTfulAPI.DataDictionary.getType, {
            params: {
                typeCode: typeCode
            }
        }).toPromise();
        return Object.create(DictionaryType.prototype, Object.getOwnPropertyDescriptors(ans));
    }

    async putType(SystemParameter: DictionaryType): Promise<void> {
        await this.http.put(RESTfulAPI.DataDictionary.getType, SystemParameter).toPromise();
    }

    async postType(SystemParameter: DictionaryType): Promise<void> {
        await this.http.post(RESTfulAPI.DataDictionary.getType, SystemParameter).toPromise();
    }

    async deleteType(typeCode: string): Promise<void> {
        await this.http.delete(RESTfulAPI.DataDictionary.getType, {
            params: {
                typeCode: typeCode
            }
        }).toPromise();
    }


    async getData(typeCode: string, keyword: string): Promise<DictionaryData> {
        const ans = await this.http.get(RESTfulAPI.DataDictionary.getData, {
            params: {
                typeCode: typeCode,
                keyword: keyword,
            }
        }).toPromise();
        return Object.create(DictionaryData.prototype, Object.getOwnPropertyDescriptors(ans));
    }

    async putData(SystemParameter: DictionaryData): Promise<void> {
        await this.http.put(RESTfulAPI.DataDictionary.getData, SystemParameter).toPromise();
    }

    async postData(SystemParameter: DictionaryData): Promise<void> {
        await this.http.post(RESTfulAPI.DataDictionary.getData, SystemParameter).toPromise();
    }

    async deleteData(typeCode: string, keyword: string): Promise<void> {
        await this.http.delete(RESTfulAPI.DataDictionary.getData, {
            params: {
                typeCode: typeCode,
                keyword: keyword,
            }
        }).toPromise();
    }
}

