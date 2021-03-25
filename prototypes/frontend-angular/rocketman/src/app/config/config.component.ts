import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-config',
  templateUrl: './config.component.html',
  styleUrls: ['./config.component.css']
})
export class ConfigComponent implements OnInit {
  constructor() { }

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
}
