import { Component, OnInit } from '@angular/core';
import {IConfig, RocketmanService} from '../../../../shared/rocketman.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-config-templates',
  templateUrl: './config-templates.component.html',
  styleUrls: ['./config-templates.component.scss']
})
export class ConfigTemplatesComponent implements OnInit {
  configs: IConfig[] | null;

  constructor(private readonly rocketman: RocketmanService, private readonly router: Router) { }
  ngOnInit(): void {
    this.rocketman.getConfigs().subscribe(r => {
      this.configs = r;
    });
  }

  useConfig(config: IConfig): void {
    this.rocketman.config = config;
    this.router.navigate(['settings']);
  }

  back(): void {
    this.router.navigate(['settings']);
  }
}
