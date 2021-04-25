import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import { interval } from 'rxjs';
import {JsonObject} from '@angular/compiler-cli/ngcc/src/packages/entry_point';
import {ChartDataSets} from 'chart.js';
import {Label} from 'ng2-charts';

interface Data{
  desc: string;
  bC: string;
  lineChartData: ChartDataSets[];
  lineChartLabels: Label[];
}

interface HttpData{
  timeStamps: Label[];
  values: JsonObject[];
  unit: string;
}
@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.css']
})
export class ViewComponent implements OnInit {
  graphs: Data[] = [];
  descriptions?: Observable<any>;
  timestamps?: Observable<any>;
  values?: Observable<any>;
  unit?: Observable<any>;
  desc?: string;
  constructor(private httpClient: HttpClient) { }

  async ngOnInit(): Promise<void> {
    interval(5000).subscribe(async x => {
      this.descriptions = await this.getData('http://localhost:8080/api/dataset/descriptions');
      await this.createCharts();
    });
  }

  async createCharts(): Promise<void>{
    this.descriptions?.subscribe(async (subscribtion: JsonObject[]) => {
      for (const sub of subscribtion) {
        this.loadData(sub.toString()).subscribe((data: HttpData) => {
          let found = false;

          for (let i = 0; i < this.graphs.length; i++) {
            if (this.graphs[i].desc === sub.toString()){
              found = true;

              this.graphs[i] = {
                desc: sub.toString(),
                bC: 'rgba(255, 255, 0, 0.28)',
                lineChartData:  [{ data: data.values, label: sub.toString() }],
                lineChartLabels: data.timeStamps
              };
            }
          }

          if (!found){
            this.graphs.push({
              desc: sub.toString(),
              bC: 'rgba(255, 255, 0, 0.28)',
              lineChartData:  [{ data: data.values, label: sub.toString() }],
              lineChartLabels: data.timeStamps
            });
          }
        });
      }
    });
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
}
