import { Component, OnInit } from '@angular/core';
import {HttpData, RocketmanService} from '../../../shared/rocketman.service';
import {Data, Router} from '@angular/router';
import {interval, Observable} from 'rxjs';
import {JsonObject} from '@angular/compiler-cli/ngcc/src/packages/entry_point';

@Component({
  selector: 'app-data-view',
  templateUrl: './data-view.component.html',
  styleUrls: ['./data-view.component.scss']
})
export class DataViewComponent implements OnInit {
  descriptions: Observable<any>;
  graphs: Data[] = [];
  timestamps: Observable<any>|null;
  values: Observable<any>|null;
  unit: Observable<any |null>;
  desc: string | null;
  data: {[key: string]: HttpData};
  dataArray: any[];
  felixVariante: Map<string, HttpData>;
  constructor(private readonly rocketman: RocketmanService, private readonly router: Router) {
    this.dataArray = [];
    this.felixVariante = new Map<string, HttpData>();
  }

  ngOnInit(): void {
    interval(5000).subscribe(async x => {
      this.descriptions = await this.rocketman.getData('http://localhost:8080/api/dataset/descriptions');
      this.getData();
    });
  }
  getData(): void{
    this.descriptions?.subscribe(async (subscribtion: JsonObject[]) => {
      for (const sub of subscribtion) {
        this.loadData(sub.toString()).subscribe((data: HttpData) => {
          this.felixVariante.set(sub.toString(), data);
          console.log(data);
        });
      }
    });
  }

  loadData(desc: string): Observable<HttpData> {
    return this.rocketman.loadData(desc);
  }

  endFlight(): void {
    this.rocketman.endFlight();
    this.router.navigate(['home']);
  }

}
