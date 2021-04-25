import {Component, Input, OnInit} from '@angular/core';
import {Color, Label} from 'ng2-charts';
import { ChartDataSets, ChartOptions, ChartType } from 'chart.js';


@Component({
  selector: 'app-graph',
  templateUrl: './graph.component.html',
  styleUrls: ['./graph.component.css']
})
export class GraphComponent implements OnInit {

  test: any;
  @Input() backColor!: string;
  @Input() lineChartData!: ChartDataSets[];

  @Input() lineChartLabels!: Label[];

  lineChartOptions = {
    responsive: true,
  };

  lineChartColors: Color[] = [
    {
      borderColor: 'black',
      backgroundColor: 'rgba(255,255,0,0.28)'
    },
  ];

  lineChartLegend = true;
  lineChartPlugins = [];
  lineChartType: ChartType = 'line';
  constructor() { }

  ngOnInit(): void {
    this.lineChartData.map(m => {
      this.test = m.data;
    });
  }

}
