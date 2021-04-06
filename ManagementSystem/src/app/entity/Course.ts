
export class Course {
    courseName: string;
    courseDescription: string;

    teacher: string;
    students: string[];

    courseUID: number;

    checkInList: string[];
    taskList: string[];
    infoList: string[];

    get briefDescription(): string {
        if(this.courseDescription.length < 20) {
            return this.courseDescription;
        } else {
            return this.courseDescription.substr(0, 18) + '...';
        }
    }
}

