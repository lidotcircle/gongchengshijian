import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpEvent, HttpResponse, HttpRequest, HttpHandler } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';

@Injectable()
export class InterceptorExceptionRedirect implements HttpInterceptor {
    constructor(private router: Router) {
    }

    intercept(httpRequest: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(httpRequest).pipe(
            tap((event: HttpEvent<any>) => {
                if (event instanceof HttpResponse) {
                    switch (event.status) {
                        case 403:
                            this.goto403(); break;
                        case 404:
                            this.goto404(); break;
                        case 500:
                            this.goto500(); break;
                    }
                }
            })
        );
    }

    private goto403() {
        this.router.navigate(['/daoyun/exception/forbidden']);
    }
    private goto404() {
        this.router.navigate(['/daoyun/exception/not-found']);
    }
    private goto500() {
        this.router.navigate(['/daoyun/exception/internal-server-error']);
    }
}

