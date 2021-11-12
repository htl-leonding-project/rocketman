import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RocketmanService} from "../rocketman.service";

@Component({
  selector: 'app-config-list',
  templateUrl: './config-list.component.html',
  styleUrls: ['./config-list.component.css']
})
export class ConfigListComponent implements OnInit {
  confs: any;
  constructor(private httpClient: HttpClient, private readonly rocketman: RocketmanService) { }

  ngOnInit(): void {
    this.confs = this.rocketman.getConfigs();
    console.log(this.confs);
  }

}
