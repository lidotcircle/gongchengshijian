import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth';


/** password pages guard */
@Injectable({
    providedIn: 'root'
})
export class AuthDomainGuard implements CanActivate {
    constructor(private authService: AuthService,
                private router: Router) {}

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree 
    {
        return new Promise((resolve) => {
            if(this.authService.isLogin) {
                this.router.navigateByUrl('/daoyun/dashboard');
                resolve(false);
            } else {
                resolve(true);
            }
        });
    }
}

