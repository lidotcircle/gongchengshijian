
export class Course {
    courseExId: number;
    courseName: string;
    briefDescription: string;

    teacher: {userName: string; name: string;};
    students: {userName: string; name: string; studentTeacherId: string}[];

    checkInList: string[];
    taskList: string[];
    infoList: string[];

    get BriefDescription(): string {
        if(!this.briefDescription) return '';

        if(this.briefDescription.length < 20) {
            return this.briefDescription;
        } else {
            return this.briefDescription.substr(0, 18) + '...';
        }
    }

    get Teacher(): string {return this.teacher.name || this.teacher.userName;}
}

