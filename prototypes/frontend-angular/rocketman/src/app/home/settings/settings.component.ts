import {Component, OnInit} from '@angular/core';
import {RocketmanService} from '../../../shared/rocketman.service';
import {Router} from '@angular/router';
import {root} from 'rxjs/internal-compatibility';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  name: string | null;
  countdown: number | null;
  igniter: number | null;
  resistance: number | null;
  useJoyStick: boolean | null;
  useVideo: boolean | null;
  savedTemplate = false;

  constructor(private readonly rocketman: RocketmanService, private readonly router: Router) {
    this.name = null;
  }

  ngOnInit(): void {
    if (this.rocketman.config == null) {
      this.name = 'Config';
      this.countdown = 10;
      this.igniter = 4;
      this.resistance = 1.4;
      this.useJoyStick = false;
      this.useVideo = false;
    }
    else {
      this.name = this.rocketman.config.name;
      this.countdown = this.rocketman.config.countdown;
      this.igniter = this.rocketman.config.igniter;
      this.resistance = this.rocketman.config.resistance;
      this.useVideo = this.rocketman.config.useVideo;
      this.useJoyStick = this.rocketman.config.useJoyStick;
    }
  }

  countdownUp(up: boolean): void {
    if (up) {
      this.countdown++;
    } else {
      this.countdown--;
    }
    this.changeDetect();
  }

  igniterUp(up: boolean): void {
    if (up) {
      this.igniter++;
    } else {
      this.igniter--;
    }
    this.changeDetect();
  }

  resistanceUp(up: boolean): void {
    if (up) {
      this.resistance = Number((this.resistance + 0.1).toFixed(1));
    } else {
      this.resistance = Number((this.resistance - 0.1).toFixed(1));
    }
    this.changeDetect();
  }

  joyStickUse(): void {
    this.useJoyStick = !this.useJoyStick;
    this.changeDetect();
  }

  videoUse(): void {
    this.useVideo = !this.useVideo;
    this.changeDetect();
  }

  saveTemplate(): void {
    this.savedTemplate = true;
    this.rocketman.saveConfig({
        name: this.name,
        countdown: this.countdown,
        igniter: this.igniter,
        resistance: this.resistance,
        useJoyStick: this.useJoyStick,
        useVideo: this.useVideo
      }
    );
  }

  loadTemplate(): void {
    this.router.navigate(['templates']);
  }

  changeDetect(): void {
    this.savedTemplate = false;
    this.storeConfig();
    this.rocketman.warnings = [];
  }

  back(): void {
    this.router.navigate(['home']);
  }
  storeConfig(): void{
    this.rocketman.config = {
      name: this.name,
      countdown: this.countdown,
      igniter: this.igniter,
      resistance: this.resistance,
      useJoyStick: this.useJoyStick,
      useVideo: this.useVideo
    };
  }
}
