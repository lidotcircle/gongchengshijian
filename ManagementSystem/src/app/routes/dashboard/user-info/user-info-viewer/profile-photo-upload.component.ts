import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { NbToastrService, NbWindowRef } from '@nebular/theme';
import { FileChooserService } from 'src/app/service/utils';
import { Pattern } from 'src/app/shared/utils';


@Component({
    template: `
    <div class="message" *ngIf='message'>{{ message }}</div>
    <div class="photo-upload">
        <div class="photo">
            <div class="photo-wrapper">
                <img [class]='isHeighter ? "photo-heighter" : "photo-widther"' (load)='imageLoaded()' [src]='photo' alt="无" #profileImg>
            </div>
        </div>
        <div class="upload">
            <button nbButton fullWidth status='info' (click)="uploadPhoto()">上传</button>
        </div>
    </div>
    <div class="buttons">
        <button nbButton status='primary' (click)="confirm()" hero [disabled]='!isUploaded'>确认</button>
        <button nbButton status='primary' (click)="cancel()"  outline>取消</button>
    </div>
    `,
    styles: [
        `
        .message, .photo-upload {
            padding: 0em 0em 1.7em 0em;
            font-weight: bold;
        }
        .photo-upload {
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .photo {
            padding: 2em;
        }
        .upload {
            width: 80%;
        }
        .photo .photo-wrapper {
            position: relative;
            width: 250px;
            height: 250px;
            overflow: hidden;
            border-radius: 50%;
            border: 1.5pt solid #ccc;
            display: flex;
            flex-direction: column;
            justify-content: space-around;
            align-items: center;
        }
        .photo .photo-wrapper img {
            position: absolute;
        }
        .photo-heighter {
            width: 100%;
        }
        .photo-widther {
            height: 100%;
        }
        .buttons {
            display: flex;
            flex-direction: row;
            justify-content: space-between;
        }`,
    ],
})
export class ProfilePhotoUploadComponent implements OnInit {
    @Input()
    message: string;
    @Input()
    photo: string;
    isUploaded: boolean = false;
    isHeighter: boolean = false;

    @ViewChild('profileImg', {static: true})
    private image: ElementRef;

    constructor(private windowRef: NbWindowRef,
                private fileChooser: FileChooserService,
                private toastService: NbToastrService) {}

    ngOnInit() {
        this.photo = this.photo;
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

    async uploadPhoto() {
        const file = await this.fileChooser.getFile(".png,.jpg,.gif");
        const buffer = await file.slice().arrayBuffer();
        if(buffer.byteLength > 5 * 1024 * 1024) {
            this.toastService.danger("图片过大");
        } else {
            this.isUploaded = true;
            this.photo = 'data:' + file.type + ';base64,' + this.arrayBufferToBase64(buffer);
            this.windowRef.config.context['photo'] = this.photo;
        }
    }

    private arrayBufferToBase64(buffer: ArrayBuffer): string {
        let binary = '';
        let bytes = new Uint8Array( buffer );
        let len = bytes.byteLength;
        for (let i = 0; i < len; i++) {
            binary += String.fromCharCode( bytes[ i ] );
        }
        return window.btoa( binary );
    }

    imageLoaded() {
        const html = this.image.nativeElement as HTMLElement;
        this.isHeighter = html.clientHeight > html.clientWidth;
    }
}

