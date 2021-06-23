import { Component, Input, OnInit } from '@angular/core';
import { NbWindowRef } from '@nebular/theme';
import { CourseInfo } from 'src/app/entity/Course';
import { CourseTaskService } from 'src/app/service/course/course-task.service';
import { CourseService } from 'src/app/service/course/course.service';
import { Pattern } from 'src/app/shared/utils';

@Component({
    template: `
    <form (ngSubmit)="confirm()" #form="ngForm" aria-labelledby="title">
        <nb-card accent='info'>
            <nb-card-header>
                <div class="title">{{ courseInfo.taskTitle }} </div>
                <div class="date">发布日期: {{ courseInfo.releaseDate && courseInfo.releaseDate.toLocaleString() }}</div>
            </nb-card-header>
            <nb-card-body>
                {{ courseInfo.content }}
            </nb-card-body>
            <nb-card-footer class='buttons'>
                <div *ngIf='!deletable'></div>
                <button *ngIf='deletable' type='button' nbButton status='danger' (click)='deleteInfo()' outline>删除</button>
                <button nbButton status='primary' hero [disabled]='!form.valid'>确认</button>
            </nb-card-footer>
        </nb-card>
    </form>`,
    styles: [
        `
            nb-card {
                min-width: 50vw;
            }
            nb-card-header {
                display: flex;
                flex-direction: column;
                align-items: center;
            }
            .buttons {
                display: flex;
                flex-direction: row;
                justify-content: space-between;
            }
            .date {
                font-size: small;
            }
            .title {
                font-size: large;
            }
        `,
    ],
})
export class CourseInfoViewComponent implements OnInit {
    @Input()
    courseInfo: CourseInfo;
    deletable: boolean = true;

    constructor(private windowRef: NbWindowRef) {}

    ngOnInit() {
    }

    confirm() {
        this.windowRef.close();
    }

    deleteInfo() {
        this.windowRef.config.context['delete'] = true;
        this.windowRef.close();
    }
}

