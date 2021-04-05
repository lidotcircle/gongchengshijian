import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { InterceptorAuth } from './interceptor-auth';

export const InterceptorProviders = [
    {provide: HTTP_INTERCEPTORS, useClass: InterceptorAuth, multi: true},
];

