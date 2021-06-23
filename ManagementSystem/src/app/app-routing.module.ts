import { ExtraOptions, RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';


export const routes: Routes = [
    {
        path: 'daoyun',
        loadChildren: () => import('./routes/routes.module')
        .then(m => m.DaoyunModule),
    },
    { path: '', redirectTo: 'daoyun', pathMatch: 'full' },
    { path: '**', redirectTo: 'daoyun' },
];

const config: ExtraOptions = {
  useHash: false,
};

@NgModule({
  imports: [RouterModule.forRoot(routes, config)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
