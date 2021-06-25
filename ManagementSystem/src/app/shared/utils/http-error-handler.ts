import { HttpErrorResponse } from '@angular/common/http';
import { NbToastrService } from '@nebular/theme';
import { AppInjector } from 'src/app/@core/core.module';


export function httpErrorHandler(e: Error, title: string, msg?: string, danger: boolean = true,): string
{
    const toastService = AppInjector.get(NbToastrService);
    msg = msg || "请求错误";

    if (e instanceof HttpErrorResponse) {
        msg = e.error.reason || msg;
    }

    if( danger) {
        toastService.danger(msg, title);
    } else {
        toastService.warning(msg, title);
    }

    return msg;
}

