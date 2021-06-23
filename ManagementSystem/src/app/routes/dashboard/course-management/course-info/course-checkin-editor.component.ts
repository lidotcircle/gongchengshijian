import { Component, Input, OnInit } from '@angular/core';
import { NbWindowRef } from '@nebular/theme';
import { CourseCheckin } from 'src/app/entity/Course';
import { CourseCheckinService } from 'src/app/service/course/course-check-in.service';
import { Pattern } from 'src/app/shared/utils';

@Component({
    template: `
    <form (ngSubmit)="confirm()" #form="ngForm" aria-labelledby="title">
        <nb-card accent='primary'>
            <nb-card-header>
                <div class="kv">
                    <div class='key'>签到类别</div>
                    <nb-select class='value' [(ngModel)]="checkinType" name='checkintype'>
                      <nb-option value="simple">  一键签到</nb-option>
                      <nb-option value="location">位置签到</nb-option>
                      <nb-option value="gesture"> 手势签到</nb-option>
                      <nb-option value="key">     密码签到</nb-option>
                    </nb-select>
                </div>
                <div class="kv">
                    <div class='key'>截至时间</div>
                    <div class='value'>
                        <input nbInput fullWidth shape="rectangle" [(ngModel)]="date" name='deadlineDate'
                               placeholder="日期"
                               [nbDatepicker]="datepickerxdeadline">
                        <nb-datepicker #datepickerxdeadline></nb-datepicker>
                    </div>
                    <div class='value'>
                        <input nbInput fullWidth shape="rectangle" [(ngModel)]="time" name='deadlineTime'
                               placeholder="时间"
                               [nbTimepicker]="timepickerxdeadline">
                        <nb-timepicker #timepickerxdeadline></nb-timepicker>
                    </div>
                </div>
            </nb-card-header>

            <nb-card-body>
                <div class="kv" *ngIf='checkinType=="key"'>
                    <div class="key"  >签到密码</div>
                    <input class="value" nbInput shape="rectangle" pattern="[0-9]{4,6}" required
                     [(ngModel)]="extraData" name='extraKey' placeholder="4-6位签到密码, 纯数字">
                </div>
                <div class="kv" *ngIf='checkinType=="gesture"'>
                    <div class="key">签到手势</div>
                    <input class="value" nbInput shape="rectangle" pattern="[0-9]{3,9}" required
                     [(ngModel)]="extraData" name='extraGes' placeholder="3-9位签到手势, 纯数字无连续">
                </div>
            </nb-card-body>

            <nb-card-footer class='buttons'>
                <button nbButton status='primary' hero [disabled]='!form.valid || !isValid()'>确认</button>
                <button type='button' nbButton status='primary' (click)='cancel()' hero>取消</button>
            </nb-card-footer>
        </nb-card>
    </form>`,
    styles: [
        `
        form {
            min-width: 50vw;
        }
        .kv {
            display: flex;
            flex-direction: row;
            align-items: center;
        }
        .kv .key {
            min-width: 6em;
            padding: 0em 0.8em;
        }
        .kv .value {
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
export class CourseCheckinEditorComponent implements OnInit {
    @Input()
    checkin: CourseCheckin;
    @Input()
    checkinType: string;
    date: Date;
    time: Date;
    extraData: string;

    constructor(private windowRef: NbWindowRef,
                private courserCheckinService: CourseCheckinService) {}

    ngOnInit() {
        this.checkin = this.checkin || {} as any;
        this.checkinType = this.checkinType || 'simple';
        if(this.checkin.deadline == null) {
            this.checkin.deadline = new Date();
        }

        this.date = new Date(this.checkin.deadline);
        this.time = new Date(this.checkin.deadline);
    }

    isConfirmed: boolean = false;
    confirm() {
        this.isConfirmed = true;
        this.windowRef.config.context['isConfirmed'] = true;
        const data = { type: this.checkinType };
        switch(this.checkinType) {
            case "key":
                data["key"] = this.extraData;
                break;
            case "gesture":
                data["gesture"] = this.extraData;
                break;
        }
        this.checkin.jsonData = JSON.stringify(data);
        this.checkin.deadline = new Date(this.date.toDateString() + ' ' + this.time.toTimeString());
        this.windowRef.config.context['checkin'] = this.checkin;
        this.windowRef.close();
    }

    isValid(): boolean //{
    {
        if(this.checkinType != "gesture" || this.extraData == null) return true;

        let v = '';
        for(const f of this.extraData) {
            if(f == v) return false;
            v = f;
        }

        return true;
    } //}

    cancel() {
        this.windowRef.config.context['isConfirmed'] = false;
        this.windowRef.close();
    }
}

