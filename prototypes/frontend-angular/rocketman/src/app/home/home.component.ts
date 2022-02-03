import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {RocketmanService} from '../../shared/rocketman.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private readonly router: Router, readonly rocketman: RocketmanService) { }

  ngOnInit(): void {
  }

  toSettings(): void {
    this.router.navigate(['settings']);
  }

  toData(): void {
    this.router.navigate(['countdown']);
  }
}
