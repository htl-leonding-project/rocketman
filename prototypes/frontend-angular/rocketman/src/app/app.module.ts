import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { SettingsComponent } from './home/settings/settings.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {FormsModule} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {HttpClientModule} from '@angular/common/http';
import { ConfigTemplatesComponent } from './home/settings/config-templates/config-templates.component';
import {MatCardModule} from '@angular/material/card';
import { DataViewComponent } from './home/data-view/data-view.component';
import {CountdownComponent, FormatTimePipe} from './home/data-view/countdown/countdown.component';
import { DownloadComponent } from './home/download/download.component';
import {CsvModule} from "@ctrl/ngx-csv";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SettingsComponent,
    ConfigTemplatesComponent,
    DataViewComponent,
    CountdownComponent,
    FormatTimePipe,
    DownloadComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NoopAnimationsModule,
    MatIconModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    HttpClientModule,
    MatCardModule,
    CsvModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
