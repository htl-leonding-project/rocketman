import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {JsonObject} from '@angular/compiler-cli/ngcc/src/packages/entry_point';

@Injectable({
  providedIn: 'root'
})
export class RocketmanService {

  private conf: IConfig | null;
  warnings: string[];
  timestamps?: Observable<any>;
  values?: Observable<any>;
  unit?: Observable<any>;

  constructor(private httpClient: HttpClient) {
    this.config = {
      name: 'Default Config',
      countdown: 10,
      igniter: 4,
      resistance: 1.4,
      useVideo: false,
      useJoyStick: false
    };
    this.warnings = [];
    this.warnings.push('Standard Config!');
  }

  loadData(desc: string): Observable<HttpData> {
    return new Observable(observer => {
      this.timestamps = this.getData('http://localhost:8080/api/dataset/timesSinceStart/' + desc);
      this.timestamps.subscribe((timeStamps: string[]) => {

        this.values = this.getData('http://localhost:8080/api/dataset/values/' + desc);
        this.values.subscribe((values: string[]) => {
          this.unit = this.getData('http://localhost:8080/api/dataset/unit/' + desc);
          this.unit.subscribe((unit: string) => {
            observer.next({timeStamps, values, unit});
          });
        });
      });
    });
  }

  getData(url: string): Observable<any> {
    return this.httpClient.get(url);
  }

  saveConfig(conf: IConfig): void {
    this.conf = conf;

    this.httpClient.post<any>('http://localhost:8080/api/config/', conf).subscribe(data => {
      console.log(data);
    });
  }

  getConfigs(): Observable<IConfig[]> {
    return this.httpClient.get<IConfig[]>('http://localhost:8080/api/config/');
  }

  get config(): IConfig {
    return this.conf;
  }

  set config(value: IConfig) {
    this.conf = value;
  }

  start(): void {
    this.httpClient.post<any>('http://localhost:8080/api/start/', {
      comment: '',
      startDate: new Date().toDateString(),
      endDate: null
    }).subscribe(data => {
      console.log(data);
    });
  }

  endFlight(): void {
    this.httpClient.put('http://localhost:8080/api/start/', {}).subscribe(data => {
      console.log(data);
    });
  }

  getFile(): Observable<string> {
    return this.httpClient.get<string>('http://localhost:8080/api/dataset/get-file')
  }
}

export interface IConfig {
  name: string;
  countdown: number;
  igniter: number;
  resistance: number;
  useJoyStick: boolean;
  useVideo: boolean;
}

export interface HttpData {
  timeStamps: string[];
  values: string[];
  unit: string;
}
