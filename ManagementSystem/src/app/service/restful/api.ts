import { API_ADDRESS } from 'src/app/shared/utils';

export module API {

    export module Auth {
        export const login = API_ADDRESS + '/apis/auth/refresh-token';
        export const loginByMessage = API_ADDRESS + '/apis/auth/refresh-token/message';
        export const jwt   = API_ADDRESS + '/apis/auth/jwt';
        export const signup = API_ADDRESS + '/apis/auth/user';
        export const message = API_ADDRESS + '/apis/message';

        export const requestReset = API_ADDRESS + '/apis/auth/password/reset-token';
        export const reset = API_ADDRESS + '/apis/auth/password';
    }

    export module User {
        export const info = API_ADDRESS + '/apis/user';
        export const update = API_ADDRESS + '/apis/user';
        export const updatePrivileged = API_ADDRESS + '/apis/user/privileged';
    }

    export module SysParam {
        export const getParam = API_ADDRESS + '/apis/sysparam';
        export const putParam = API_ADDRESS + '/apis/sysparam';
        export const postParam = API_ADDRESS + '/apis/sysparam';
        export const deleteParam = API_ADDRESS + '/apis/sysparam';
        export const getPage = API_ADDRESS + '/apis/sysparam/page';
    }

    export module DataDictionary {
        export const getType = API_ADDRESS + '/apis/datadict/type';
        export const putType = API_ADDRESS + '/apis/datadict/type';
        export const postType = API_ADDRESS + '/apis/datadict/type';
        export const deleteType = API_ADDRESS + '/apis/datadict/type';
        export const getTypePage = API_ADDRESS + '/apis/datadict/type/page';

        export const getData = API_ADDRESS + '/apis/datadict/data';
        export const putData = API_ADDRESS + '/apis/datadict/data';
        export const postData = API_ADDRESS + '/apis/datadict/data';
        export const deleteData = API_ADDRESS + '/apis/datadict/data';
        export const getDataPage = API_ADDRESS + '/apis/datadict/data/page';
    }

}

