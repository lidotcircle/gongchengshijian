
const genderMap = {
    "unknown": '隐藏',
    "male": '男',
    "female": '女'
};

export class User {
    userName: String;

    name: string;
    birthday: number;
    gender: string;

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
}

