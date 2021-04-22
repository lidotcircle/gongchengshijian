

export interface Student {
    userName: string; 
    name: string; 
    studentTeacherId: string
}

interface Teacher {
    userName: string;
    name: string;
}

export type CourseCheckIn = string;
export type CourseTask = string;
export type CourseInfo = string;

export class Course {
    courseExId: string;
    courseName: string;
    briefDescription: string;

    teacher: Teacher;
    students: Student[];

    checkInList: CourseCheckIn[];
    taskList: CourseTask[];
    infoList: CourseInfo[];

    get Students(): Student[]          {return this.students || [];}
    get CheckInList(): CourseCheckIn[] {return this.checkInList || [];}
    get TaskList(): CourseTask[]       {return this.taskList || [];}
    get InfoList(): CourseInfo[]       {return this.infoList || [];}

    get BriefDescription(): string {
        if(!this.briefDescription) return '';

        if(this.briefDescription.length < 20) {
            return this.briefDescription;
        } else {
            return this.briefDescription.substr(0, 18) + '...';
        }
    }

    get Teacher(): string {
        if(!this.teacher) return '';
        return this.teacher.name || this.teacher.userName;
    }
}

