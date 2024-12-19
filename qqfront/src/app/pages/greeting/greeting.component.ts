import {Component, EventEmitter, Input, Output} from '@angular/core';
import { RouterOutlet} from "@angular/router";
import {TimeComponent} from "../../components/time/time.component";


@Component({
  selector: 'app-greeting',
  standalone: true,
  imports: [
    RouterOutlet,
    TimeComponent
  ],
  templateUrl: './greeting.component.html',
  styleUrl: './greeting.component.scss'
})
export class GreetingComponent {

  @Input() data!: { [key: string]: any };
  @Output() update = new EventEmitter<{ [key: string]: any }>()
  private nowTime: Date = new Date();
  constructor() {}
  updateFields(updates: { [key: string]: any }) {
    this.update.emit(updates); // Передаем объект изменений
  }

  navigateToNext() {
        if (this.nowTime < this.data['startTime'] || this.nowTime > this.data['endTime']) {
          this.data['step']="soon"
        }
        else if (this.data['mode']==="single"){
          this.data['step']="ready"
        }
        else if (this.data['mode']==="multi"){
          this.data['step']="login"
        }
        this.update.emit({"step":this.data["step"]});
      }

  }
