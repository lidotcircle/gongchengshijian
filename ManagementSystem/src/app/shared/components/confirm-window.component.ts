import { Component, Input, OnInit } from '@angular/core';
import { NbWindowRef } from '@nebular/theme';

@Component({
    template: `
        <div class="message" *ngIf='message'>{{ message }}</div>
        <div class="buttons">
            <button nbButton status='primary' (click)="confirm()" hero>确认</button>
            <button nbButton status='primary' (click)="cancel()"  outline>取消</button>
        </div>
    `,
    styles: [
        `
        .message {
            padding: 0em 0em 1.7em 0em;
            font-weight: bold;
        }
        .buttons {
            display: flex;
            flex-direction: row;
            justify-content: space-between;
        }`,
    ],
})
export class ConfirmWindowComponent implements OnInit {
    @Input()
    message: string;

    constructor(private windowRef: NbWindowRef) {}

    ngOnInit() {
    }

    isConfirmed: boolean = false;
    confirm() {
        this.isConfirmed = true;
        this.windowRef.config.context['isConfirmed'] = true;
        this.windowRef.close();
    }

    cancel() {
        this.isConfirmed = false;
        this.windowRef.config.context['isConfirmed'] = false;
        this.windowRef.close();
    }
}

