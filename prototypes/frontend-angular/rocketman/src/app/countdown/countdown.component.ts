import {Component, OnInit, Pipe, PipeTransform} from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {RocketmanService} from "../rocketman.service";
import {Subscription, timer} from "rxjs";

@Component({
  selector: 'app-countdown',
  templateUrl: './countdown.component.html',
  styleUrls: ['./countdown.component.css']
})
export class CountdownComponent implements OnInit {
  countDown: Subscription;
  counter: number;
  tick = 1000;

  ngOnInit() {
  }
  ngOnDestroy(){
    this.countDown.unsubscribe();
  }

  constructor(private router: Router,private httpClient: HttpClient, private readonly rocketman: RocketmanService) {
    this.counter = rocketman.config.countdown;
    console.log(rocketman.config)
    this.countDown = timer(0, this.tick).subscribe(() => {
      --this.counter
      if (this.counter == 0){
        this.countDown.unsubscribe();
        this.rocketman.start()
        this.router.navigate(['view'])
      }
    });
  }
}
@Pipe({
  name: "formatTime"
})
export class FormatTimePipe implements PipeTransform {
  transform(value: number): string {
    const minutes: number = Math.floor(value / 60);
    return (
      ("00" + minutes).slice(-2) +
      ":" +
      ("00" + Math.floor(value - minutes * 60)).slice(-2)
    );
  }
}
