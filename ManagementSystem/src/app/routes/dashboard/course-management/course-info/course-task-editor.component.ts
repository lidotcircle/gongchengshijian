import { Component, Input, OnInit } from '@angular/core';
import { NbWindowRef } from '@nebular/theme';
import { CourseTask } from 'src/app/entity/Course';
import { CourseTaskService } from 'src/app/service/course/course-task.service';
import { CourseService } from 'src/app/service/course/course.service';
import { Pattern } from 'src/app/shared/utils';

@Component({
    template: `
    <form (ngSubmit)="confirm()" #form="ngForm" aria-labelledby="title">
        <nb-card accent='primary'>
            <nb-card-header>
                <div class="kv">
                    <div class='key'>标题</div>
                    <input type="text" required patten='.{2,}' nbInput [(ngModel)]='courseTask.taskTitle' name='title'>
                </div>

                <div *ngIf='committable' class="kv">
                    <div class="key">提交日期</div>
                    <input [nbDatepicker]='submitdate' type="text" nbInput [(ngModel)]='courseTask.deadline' name='title'>
                    <nb-datepicker #submitdate></nb-datepicker>
                </div>
            </nb-card-header>
            <nb-card-body>
                <textarea nbInput fullWidth [(ngModel)]='courseTask.content' name='content' rows="10" placeholder='任务内容'>
                </textarea>
            </nb-card-body>
            <nb-card-footer class='buttons'>
                <button nbButton status='primary' hero [disabled]='!form.valid'>确认</button>
                <button type='button' nbButton status='primary' (click)='cancel()' hero>取消</button>
            </nb-card-footer>
        </nb-card>
    </form>`,
    styles: [
        `
            nb-card-header .kv {
                display: flex;
                flex-direction: row;
                align-items: center;
            }
            .kv .key {
                min-width: 6em;
                padding: 0em 0.8em;
            }
            .kv input {
                flex-grow: 1;
            }
            .buttons {
                display: flex;
                flex-direction: row;
                justify-content: space-between;
            }
        `,
    ],
})
export class CourseTaskEditorComponent implements OnInit {
    @Input()
    courseTask: CourseTask;
    @Input()
    committable: boolean;

    constructor(private windowRef: NbWindowRef,
                private courserTaskService: CourseTaskService) {}

    ngOnInit() {
        this.courseTask = this.courseTask || {} as any;
        this.courseTask.committable = this.committable;
    }

    isConfirmed: boolean = false;
    confirm() {
        this.isConfirmed = true;
        this.windowRef.config.context['isConfirmed'] = true;
        this.windowRef.config.context['courseTask'] = this.courseTask;
        this.windowRef.close();
    }

    cancel() {
        this.windowRef.config.context['isConfirmed'] = false;
        this.windowRef.close();
    }
}

