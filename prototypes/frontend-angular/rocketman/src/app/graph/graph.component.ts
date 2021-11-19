import {Component, Input, OnInit} from '@angular/core';
import {Color, Label} from 'ng2-charts';
import { ChartDataSets, ChartOptions, ChartType } from 'chart.js';
import {ScaleType} from "@swimlane/ngx-charts";


@Component({
  selector: 'app-graph',
  templateUrl: './graph.component.html',
  styleUrls: ['./graph.component.css']
})
export class GraphComponent implements OnInit {

  view: any[] = [700, 300];
  colorScheme = {
    name: 'natural',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#5AA454']
  };
  legend: boolean = true;
  showXAxisLabel: boolean = true;
  showYAxisLabel: boolean = true;
  xAxis: boolean = true;
  yAxis: boolean = true;
  xAxisLabel: string = 'Time';
  yAxisLabel: string = '';
  timeline: boolean = true;
  multi: any[] = [];

  onSelect(data: any): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  onActivate(data: any): void {
    console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data: any): void {
    console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }

  test: any;
  @Input() backColor!: string;
  @Input() lineChartData!: ChartDataSets[];

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


  ngOnInit(): void {
    var newD = this.lineChartData[0];
    // @ts-ignore
    this.yAxisLabel = newD.name;
  }

}
