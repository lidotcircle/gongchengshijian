
import { Component, Input, OnInit } from '@angular/core';
import { ViewCell } from 'ng2-smart-table';

@Component({
    template: `
        <div class="normal"  *ngIf='value!=null && value!=""'> {{value}} </div>
        <div class="nothing" *ngIf='value==null || value==""'> 未填写 </div>
    `,
    styles: [
        `
        .nothing {
            color: gray;
        }`,
    ],
})
export class OptionalCellComponent implements ViewCell, OnInit {
    constructor() {}

    @Input() value: string | number;
    @Input() rowData: any;

    ngOnInit() {
    }
}

