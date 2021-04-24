

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

export type CourseCheckIn = string;

function briefy(text: string): string {
    if(!text) return '';

    if(text.length < 20) {
        return text;
    } else {
        return text.substr(0, 18) + '...';
    }
}

export class Course {
    courseExId: string;
    courseName: string;
    briefDescription: string;

    teacher: Teacher;
    students: Student[] = [];

    checkInList: CourseCheckIn[];
    tasks: CourseTask[] = [];

    get Students(): Student[]          {return this.students;}
    get CheckInList(): CourseCheckIn[] {return this.checkInList || [];}
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

    static fromObject(obj: object) {
        const ans = Object.create(Course.prototype, Object.getOwnPropertyDescriptors(obj)) as Course;
        ans.tasks = ans.tasks || [];
        ans.students = ans.students || [];

        ans.tasks.forEach(task => Course.obj2Task(task));

        return ans;
    }
}

