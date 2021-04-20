export const defaultProfileImage = require('!url-loader!./profile-image.png').default;

const genderMap = {
    "unknown": '隐藏',
    "male": '男',
    "female": '女'
};

export class User {
    userName: string;

    name: string;
    birthday: number;
    gender: string;
    photo: string;

    studentTeacherId: string;
    school: string;
    college: string;
    major: string;

    phone: string;
    email: string;

    roles: string[];

    get Birthday(): string {
        return this.birthday && (new Date(this.birthday)).toLocaleDateString();
    }

    get Gender(): string {
        return genderMap[this.gender || 'unknown'];
    }

    get Photo(): string {
        return this.photo || defaultProfileImage;
    }

    get Roles(): string {
        if(!this.roles || this.roles.length == 0) {
            return "无";
        } else {
            return this.roles.join(', ');
        }
    }
}

