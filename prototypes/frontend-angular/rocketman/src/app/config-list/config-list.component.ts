import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {IConfig, RocketmanService} from "../rocketman.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-config-list',
  templateUrl: './config-list.component.html',
  styleUrls: ['./config-list.component.css']
})
export class ConfigListComponent implements OnInit {
  confs: IConfig[];
  constructor(private router: Router,private httpClient: HttpClient, private readonly rocketman: RocketmanService) {
    this.confs = [];
    this.rocketman.getConfigs().subscribe((data) => {
      this.confs = data.reverse();
      console.log(this.confs);
    });
  }

  ngOnInit(): void {
  }

  useConf(conf: IConfig) {
    this.rocketman.config = conf;
    this.router.navigate(['config'])
  }

  convertCountdownToDate(countdown: number): Date{
    var m = Math.floor(countdown % 3600 / 60);
    var s = Math.floor(countdown % 3600 % 60);
    return new Date(2017, 10, 13, m, s, 0);
  }
}
