import { Component, Input, OnInit } from '@angular/core';
import { NbWindowRef } from '@nebular/theme';
import { Pattern } from 'src/app/shared/utils';

@Component({
    template: `
    <form (ngSubmit)="confirm()" #form="ngForm" aria-labelledby="title">
        <div class="message" *ngIf='message'>{{ message }}</div>
        <div class="inputs">
            <input type="text" required pattern='.{2,}'
                   [(ngModel)]='courseName' nbInput fullWidth placeholder="课程名称"
                   name='courseName'>
            <textarea rows="5" nbInput fullWidth placeholder="课程介绍"
                   name='briefDescription' [(ngModel)]='briefDescription'></textarea>
        </div>
        <div class="buttons">
            <button nbButton status='primary' (click)="confirm()" hero [disabled]='!form.valid'>确认</button>
            <button nbButton status='primary' (click)="cancel()"  outline>取消</button>
        </div>
    </form>`,
    styles: [
        `
        .message, .inputs {
            padding: 0em 0em 1.7em 0em;
            font-weight: bold;
        }
        .inputs input {
            margin: 0.3em 0em;
        }
        .buttons {
            display: flex;
            flex-direction: row;
            justify-content: space-between;
        }`,
    ],
})
export class BasicInfoEditorComponent implements OnInit {
    @Input()
    message: string;
    @Input()
    courseName: string;
    @Input()
    briefDescription: string;

    constructor(private windowRef: NbWindowRef) {}

    ngOnInit() {
    }

    isConfirmed: boolean = false;
    confirm() {
        this.isConfirmed = true;
        this.windowRef.config.context['isConfirmed'] = true;
        this.windowRef.config.context['courseName'] = this.courseName;
        this.windowRef.config.context['briefDescription'] = this.briefDescription;
        this.windowRef.close();
    }

    cancel() {
        this.isConfirmed = false;
        this.windowRef.config.context['isConfirmed'] = false;
        this.windowRef.close();
    }
}

