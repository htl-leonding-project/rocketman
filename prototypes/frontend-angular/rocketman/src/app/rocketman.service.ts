import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Label} from 'ng2-charts';
import {JsonObject} from '@angular/compiler-cli/ngcc/src/packages/entry_point';

@Injectable({
  providedIn: 'root'
})
export class RocketmanService {

  private conf!: IConfig;
  timestamps?: Observable<any>;
  values?: Observable<any>;
  unit?: Observable<any>;

  constructor(private httpClient: HttpClient) {
  }

  loadData(desc: string): Observable<HttpData> {
    return new Observable(observer => {
      this.timestamps = this.getData('http://localhost:8080/api/dataset/timesSinceStart/' + desc);
      this.timestamps.subscribe((timeStamps: Label[]) => {

        this.values = this.getData('http://localhost:8080/api/dataset/values/' + desc);
        this.values.subscribe((values: JsonObject[]) => {

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
    return this.httpClient.get<IConfig[]>('http://localhost:8080/api/config/')
  }

  get config(): IConfig {
    return this.conf;
  }

  set config(value: IConfig) {
    this.conf = value;
  }

  start() {
    this.httpClient.post<any>('http://localhost:8080/api/start/', {
      comment: '',
      startDate: new Date().toDateString(),
      endDate: null
    }).subscribe(data => {
      console.log(data);
    });
  }

  endFlight() {
    this.httpClient.put('http://localhost:8080/api/start/', {}).subscribe(data => {
      console.log(data);
    });
  }

  getFile() {
    return this.httpClient.get<String>('http://localhost:8080/api/dataset/get-file')
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
  timeStamps: Label[];
  values: JsonObject[];
  unit: string;
}
