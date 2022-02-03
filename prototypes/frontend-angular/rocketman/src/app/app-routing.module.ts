import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {SettingsComponent} from './home/settings/settings.component';
import {ConfigTemplatesComponent} from './home/settings/config-templates/config-templates.component';
import {DataViewComponent} from './home/data-view/data-view.component';
import {CountdownComponent} from './home/data-view/countdown/countdown.component';

const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'settings', component: SettingsComponent},
  {path: 'templates', component: ConfigTemplatesComponent},
  {path: 'view', component: DataViewComponent},
  {path: 'countdown', component: CountdownComponent},
  {path: '**', pathMatch: 'full', redirectTo: 'home'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
