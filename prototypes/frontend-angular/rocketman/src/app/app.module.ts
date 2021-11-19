import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FlexLayoutModule } from '@angular/flex-layout';
import { AppComponent } from './app.component';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ConfigComponent } from './config/config.component';
import { ArchiveComponent } from './archive/archive.component';
import {FormsModule} from '@angular/forms';
import { ViewComponent } from './view/view.component';
import {HttpClientModule} from '@angular/common/http';
import { ConfigListComponent } from './config-list/config-list.component';
import {ChartCommonModule, ChartComponent, LineChartModule} from "@swimlane/ngx-charts";
import {GraphComponent} from "./graph/graph.component";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: 'home', component: HomeComponent },
  { path: 'config', component: ConfigComponent },
  { path: 'archive', component: ArchiveComponent },
  { path: 'view', component: ViewComponent },
  { path: 'confs', component: ConfigListComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ConfigComponent,
    ArchiveComponent,
    ViewComponent,
    GraphComponent,
    ConfigListComponent,
  ],
  imports: [
    BrowserModule,
    FlexLayoutModule,
    RouterModule.forRoot(routes),
    FormsModule,
    ChartCommonModule,
    HttpClientModule,
    LineChartModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
