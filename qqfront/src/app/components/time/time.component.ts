import { Component } from '@angular/core';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-time',
  standalone: true,
  imports: [
    DatePipe
  ],
  templateUrl: './time.component.html',
  styleUrl: './time.component.scss'
})
export class TimeComponent {
  currentTime: Date;

  constructor() {
    this.currentTime = new Date();
    setInterval(() => {
      this.currentTime = new Date();
    }, 1000);
  }
}
