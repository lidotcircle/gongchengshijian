import { API_ADDRESS } from 'src/app/shared/utils';

export module API {

    export module Auth {
        export const login = API_ADDRESS + '/apis/auth/login';
        export const jwt   = API_ADDRESS + '/apis/auth/jwt';
    }

    export module User {
        export const info = API_ADDRESS + '/apis/user';
        export const update = API_ADDRESS + '/apis/user';
        export const updatePrivileged = API_ADDRESS + '/apis/user/privileged';
    }

}

