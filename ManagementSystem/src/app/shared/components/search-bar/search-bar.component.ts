import { Component, ElementRef, OnInit, Output, ViewChild } from '@angular/core';
import { Subject } from 'rxjs';


export type SearchInputEvent = [string, (hints: string[]) => void];
@Component({
    selector: 'app-search-bar',
    templateUrl: './search-bar.component.html',
    styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent implements OnInit {
    @Output('searchinput')
    private _input: Subject<SearchInputEvent> = new Subject();
    @Output('searchenter')
    private _enter: Subject<string> = new Subject();

    @ViewChild('invalue', {static: true})
    private inputv: ElementRef;

    inputvalue: string;
    hints: string[];
    constructor() {
        this.inputvalue = '';
        this.hints = [];
    }

    ngOnInit(): void {}

    oninput(event: InputEvent) {
        let val = this.inputvalue;

        if(!this.inputvalue.endsWith(event.data) && event.data){
            if(event.data.match(/.*\p{Unified_Ideograph}.*/u)) {
                val += event.data;
            } else {
                return;
            }
        }

        let called = false;
        const hintsHook = (hints: string[]) => {
            hints = hints.filter((v, idx, self) => self.indexOf(v) === idx);
            if(called) {
                debugger;
                throw new Error('debug here');
            }
            called = true;
            this.hints = hints.slice(0);
        }
        this._input.next([val, hintsHook]);
    }

    onkeyup(ev: KeyboardEvent) {
        if(ev.key == 'Enter') {
            (this.inputv.nativeElement as HTMLElement).blur();
            this._enter.next(this.inputvalue);
        }
    }

    onblur() {
        this.hints = [];
    }

    onclear() {
        this.inputvalue = '';
        this._enter.next(this.inputvalue);
    }

    onhintclick(n: number) {
        this.inputvalue = this.hints[n];
        this.hints = [];
        this._enter.next(this.inputvalue);
    }
}

