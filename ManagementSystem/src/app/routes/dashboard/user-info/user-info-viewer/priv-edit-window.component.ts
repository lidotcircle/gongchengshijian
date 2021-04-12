import { Component, Input, OnInit } from '@angular/core';
import { NbWindowRef } from '@nebular/theme';
import { Pattern } from 'src/app/shared/utils';

@Component({
    template: `
    <form (ngSubmit)="confirm()" #form="ngForm" aria-labelledby="title">
        <div class="message" *ngIf='message'>{{ message }}</div>
        <div class="inputs">
            <input type="password" required [pattern]='pwpattern'
                   [(ngModel)]='password' nbInput fullWidth placeholder="密码"
                   name='passwordx'>
            <input type="{{type}}" required [pattern]='pattern' 
                   [(ngModel)]='value' nbInput fullWidth placeholder="{{name}}"
                   name='valux'>
        </div>
        <div class="buttons">
            <button nbButton status='primary' (click)="confirm()" hero [disabled]='!form.valid'>确认</button>
            <button nbButton status='primary' (click)="cancel()"  outline>取消</button>
        </div>
    </form>
    `,
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
export class PrivEditWindowComponent implements OnInit {
    @Input()
    message: string;
    @Input()
    pattern: string;
    @Input()
    type: string;
    @Input()
    name: string;
    @Input()
    value: string;
    
    password: string;
    pwpattern: string;

    constructor(private windowRef: NbWindowRef) {}

    ngOnInit() {
        this.pwpattern = Pattern.password.toString();
    }

    isConfirmed: boolean = false;
    confirm() {
        this.isConfirmed = true;
        this.windowRef.config.context['isConfirmed'] = true;
        this.windowRef.config.context['password'] = this.password;
        this.windowRef.config.context['value'] = this.value;
        this.windowRef.close();
    }

    cancel() {
        this.isConfirmed = false;
        this.windowRef.config.context['isConfirmed'] = false;
        this.windowRef.close();
    }
}

