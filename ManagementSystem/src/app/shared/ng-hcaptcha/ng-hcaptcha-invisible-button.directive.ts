import { Directive, ElementRef, EventEmitter, HostListener, Inject, Input, NgZone, OnInit, Output, PLATFORM_ID, OnDestroy } from '@angular/core';
import { isPlatformBrowser, isPlatformServer } from '@angular/common';
import { Subscription } from 'rxjs';
import { CAPTCHA_CONFIG, CaptchaConfig } from './ng-hcaptcha-config';
import { loadHCaptcha } from './hcaptcha-utils';

declare const window: any;

@Directive({
    selector: '[ngHcaptchaInvisibleButton]'
})
export class NgHcaptchaInvisibleButtonDirective implements OnInit, OnDestroy {

    @Input() siteKey: string;
    @Input() languageCode: string;

    @Output() verify: EventEmitter<string> = new EventEmitter<string>();
    @Output() expired: EventEmitter<any> = new EventEmitter<any>();
    @Output() error: EventEmitter<any> = new EventEmitter<any>();

    private lastClickEvent: any;
    private captcha$: Subscription;
    private widgetId: string;

    constructor(private elRef: ElementRef,
        @Inject(CAPTCHA_CONFIG) private config: CaptchaConfig,
        private zone: NgZone,
        @Inject(PLATFORM_ID) private platformId) { }

    options: any;
    ngOnInit() {
        // Use language code from module config when input parameter is not set
        if (!this.languageCode) {
            this.languageCode = this.config.languageCode;
        }

        // Do not load hCaptcha if platform is server
        if (isPlatformServer(this.platformId)) {
            return;
        }

        // Load the hCaptcha script
        this.captcha$ = loadHCaptcha(this.languageCode).subscribe(
            () => {
                // Configure hCaptcha
                const options = {
                    sitekey: (this.siteKey || this.config.siteKey),
                    size: 'invisible',
                    callback: (res) => { this.zone.run(() => this.onVerify(res)); },
                    'expired-callback': (res) => { this.zone.run(() => this.onExpired(res)); },
                    'error-callback': (err) => { this.zone.run(() => this.onError(err)); }
                };
                this.options = options;
            }
        );
    }

    ngOnDestroy() {
        this.captcha$.unsubscribe();
    }

    @Input() set captchaEnabled(enabled: boolean) {
        if(enabled) {
            if (isPlatformBrowser(this.platformId)) {
                if(this.widgetId) {
                    window.hcaptcha.reset(this.widgetId);
                }
                this.widgetId = window.hcaptcha.render(this.elRef.nativeElement, this.options);
                window.hcaptcha.execute(this.widgetId);
            }
        } else {
            if(this.widgetId) {
                window.hcaptcha.reset(this.widgetId);
                this.widgetId = null;
            }
        }
    }

    /**
     * Is called when the verification was successful
     * @param response The verification token
     */
    private onVerify(response: string): void {
        const event = this.lastClickEvent || {};
        event.hCaptchaToken = response;
        this.verify.emit(response);
    }

    /**
     * Is called when the verification has expired
     * @param response The verification response
     */
    private onExpired(response: any): void {
        this.expired.emit(response);
    }

    /**
     * Is called when an error occurs during the verification process
     * @param error The error returned by hCaptcha
     */
    private onError(error: any): void {
        this.error.emit(error);
    }

}
