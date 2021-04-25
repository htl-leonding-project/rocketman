import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-config',
  templateUrl: './config.component.html',
  styleUrls: ['./config.component.css']
})
export class ConfigComponent implements OnInit {
  constructor(private router: Router, private httpClient: HttpClient) { }

  countdown = '00:00';
  igniter = 0;
  resistance = 0;

  useJoyStick = true;
  useVideo = false;

  ngOnInit(): void {
  }

  changeJoyStickUse(state: boolean): void {
    this.useJoyStick = state;
  }
  changeVideo(state: boolean): void {
    this.useVideo = state;
  }
  start(): void{
    this.httpClient.post<any>('http://localhost:8080/api/config/addConf', [
      this.countdown, // zahl":"zahl
      this.igniter, // int
      this.resistance, // int
      this.useJoyStick, // Boolean
      this.useVideo  // Boolean
    ]);
    this.router.navigate(['/view']);
  }
}
