

export interface Student {
    userName: string; 
    name: string; 
    studentTeacherId: string
    score: string;
}

interface Teacher {
    userName: string;
    name: string;
}

export interface CourseInfo {
    courseExId?: string;
    taskId: number;
    taskTitle: string;
    content: string;
    releaseDate: Date;

    Content: String;
}

export interface CourseTask extends CourseInfo {
    deadline: Date;
    committable: boolean;
}

export interface CourseCheckin {
    courseExId?: string;
    checkinId: number;
    jsonData: string;
    deadline: Date;
    releaseDate: Date;
}

function briefy(text: string): string {
    if(!text) return '';

    if(text.length < 20) {
        return text;
    } else {
        return text.substr(0, 18) + '...';
    }
}

const typeMap = {
    "simple":  "一键签到",
    "location": "位置签到",
    "gesture": "手势签到",
    "key":     "密码签到",
    "unknown": "未定义",
};

export class Course {
    courseExId: string;
    courseName: string;
    briefDescription: string;

    teacher: Teacher;
    students: Student[] = [];

    checkins: CourseCheckin[];
    tasks: CourseTask[] = [];

    get Students(): Student[]          {return this.students;}
    get CheckinList(): CourseCheckin[] {return this.checkins || [];}
    get Tasks(): CourseTask[] {
        return this.tasks.filter(task => task.committable);
    }
    get Infos(): CourseInfo[] {
        return this.tasks.filter(task => !task.committable);
    }

    get BriefDescription(): string {
        return briefy(this.briefDescription);
    }

    get Teacher(): string {
        if(!this.teacher) return '';
        return this.teacher.name || this.teacher.userName;
    }

    static obj2Task(obj: object) {
        const task = obj as CourseTask;
        task.taskId = (obj as any).id;
        task.deadline = task.deadline && new Date(task.deadline);
        task.releaseDate = task.releaseDate && new Date(task.releaseDate);
        task.Content = briefy(task.content);
        return task;
    }

    static obj2Checkin(obj: object) {
        const checkin = obj as CourseCheckin;
        checkin.checkinId = (obj as any).id;
        checkin.deadline = checkin.deadline && new Date(checkin.deadline);
        checkin.releaseDate = checkin.releaseDate && new Date(checkin.releaseDate);
        try {
            const oo = JSON.parse(checkin.jsonData);
            const t = oo['type'] || 'unknown';
            checkin['getType'] = () => typeMap[t] || typeMap['unknown'];
        } catch {}
        return checkin;
    }

    static fromObject(obj: object) {
        const ans = Object.create(Course.prototype, Object.getOwnPropertyDescriptors(obj)) as Course;
        ans.tasks = ans.tasks || [];
        ans.students = ans.students || [];

        ans.tasks.forEach   (task => Course.obj2Task(task));
        ans.checkins.forEach(checkin => Course.obj2Checkin(checkin));

        return ans;
    }
}

