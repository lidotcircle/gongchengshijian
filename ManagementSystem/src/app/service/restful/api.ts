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
        export const descriptors = API_ADDRESS + '/apis/user/descriptor';
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

    export module AdminUser {
        export const get = API_ADDRESS + '/apis/admin/user';
        export const put = API_ADDRESS + '/apis/admin/user';
        export const post = API_ADDRESS + '/apis/admin/user';
        export const deleteUser = API_ADDRESS + '/apis/admin/user';
        export const getPage = API_ADDRESS + '/apis/admin/user/page';
    }

    export module Course {
        export const get = API_ADDRESS + '/apis/course';
        export const put = API_ADDRESS + '/apis/course';
        export const post = API_ADDRESS + '/apis/course';
        export const deleteCourse = API_ADDRESS + '/apis/course';

        export const getPage = API_ADDRESS + '/apis/course/page';

        export const invite = API_ADDRESS + '/apis/course/student';
        export const deleteStudent = API_ADDRESS + '/apis/course/student';
        export const join = API_ADDRESS + '/apis/course/student/me';
        export const exit = API_ADDRESS + '/apis/course/student/me';

        export module Task {
            export const get = API_ADDRESS + '/apis/course/task';
            export const post = API_ADDRESS + '/apis/course/task';
            export const put = API_ADDRESS + '/apis/course/task';
            export const deleteTask = API_ADDRESS + '/apis/course/task'
        }

        export module Checkin {
            export const get = API_ADDRESS + '/apis/course/check-in';
            export const post = API_ADDRESS + '/apis/course/check-in';
            export const put = API_ADDRESS + '/apis/course/check-in';
            export const deleteCheckin = API_ADDRESS + '/apis/course/check-in'

            export const anwserList = API_ADDRESS + '/apis/course/check-in/anwser-list';
        }
    }

    export module Role {
        export const post = API_ADDRESS + '/apis/role';
        export const deleteRole = API_ADDRESS + '/apis/role';
        export const getList = API_ADDRESS + '/apis/role/list';

        export module Perm {
            export const get = API_ADDRESS + '/apis/perm';
            export const post = API_ADDRESS + '/apis/perm';
            export const put = API_ADDRESS + '/apis/perm';
            export const deletePerm = API_ADDRESS + '/apis/perm';

            export const getTree = API_ADDRESS + '/apis/perm/tree';
            export const getRolePerms = API_ADDRESS + '/apis/role/perm/list';

            export const getPermRoles = API_ADDRESS + '/apis/perm/role';

            export const enable  = API_ADDRESS + '/apis/role/perm';
            export const disable = API_ADDRESS + '/apis/role/perm';
            export const hasPerm = API_ADDRESS + '/apis/role/perm';
        }
    }
}

