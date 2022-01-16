import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RocketmanService} from "../rocketman.service";
import {Router} from "@angular/router";
import { CsvModule } from '@ctrl/ngx-csv';

@Component({
  selector: 'app-download',
  templateUrl: './download.component.html',
  styleUrls: ['./download.component.css']
})
export class DownloadComponent implements OnInit {

  data: String[]
  out: string;
  constructor(private httpClient: HttpClient, private readonly rocketman: RocketmanService, private router: Router) {
    this.data = []
    this.out = ''
    rocketman.getFile().subscribe(res =>{
      this.data.push(res);
      this.out = 'Ready'
    })
  }

  ngOnInit(): void {
  }

}
