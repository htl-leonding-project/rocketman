import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {IConfig, RocketmanService} from '../rocketman.service';

@Component({
  selector: 'app-config',
  templateUrl: './config.component.html',
  styleUrls: ['./config.component.css']
})
export class ConfigComponent implements OnInit {
  constructor(private router: Router, private httpClient: HttpClient, private readonly rocketman: RocketmanService) { }

  name = '';
  countdown = '00:00';
  igniter = 0;
  resistance = 0;
  useJoyStick = false;
  useVideo = false;
  conf!: IConfig;

  ngOnInit(): void {
  }

  changeJoyStickUse(state: boolean): void {
    this.useJoyStick = state;
  }
  changeVideo(state: boolean): void {
    this.useVideo = state;
  }
  start(): void{
    this.rocketman.config =  {
      name: this.name, // string
      countdown: Number(this.countdown), // zahl":"zahl
      igniter: this.igniter, // int
      resistance: this.resistance, // int
      useJoyStick: this.useJoyStick, // Boolean
      useVideo: this.useVideo  // Boolean
    };
    this.router.navigate(['/view']);
  }

  save(): void{
    this.rocketman.saveConfig({
      name: this.name, // string
      countdown: Number(this.countdown), // zahl":"zahl
      igniter: this.igniter, // int
      resistance: this.resistance, // int
      useJoyStick: this.useJoyStick, // Boolean
      useVideo: this.useVideo  // Boolean
    });
  }

  loadConf(): void {
    this.router.navigate(['/confs']);
  }
}
