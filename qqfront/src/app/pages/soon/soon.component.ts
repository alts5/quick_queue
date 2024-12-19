import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Router, RouterOutlet} from "@angular/router";
import {TimeComponent} from "../../components/time/time.component";
import {map} from 'rxjs/operators';
import {ConfigService} from '../../services/config.service';
import {config} from 'rxjs';


@Component({
  selector: 'app-soon',
  standalone: true,
  imports: [
    RouterOutlet,
    TimeComponent
  ],
  templateUrl: './soon.component.html',
  styleUrl: './soon.component.scss'
})
export class SoonComponent {
   startTime: string = 'ЧЧ:ММ'
  @Input() data!: { [key: string]: any };
  @Output() update = new EventEmitter<{ [key: string]: any }>()
  constructor(private router: Router, private configService: ConfigService) {
    this.configService.getConfig().pipe(
      map((config) => {
        console.log(config);
        this.startTime = config.startTime;
      })
    ).subscribe();

  }

  refresh() {
    if(parseTime(this.startTime) < new Date()) {}
    this.data["step"]="root"
    this.update.emit({"step":this.data["step"]});
  }
}
function parseTime(timeString: string): Date {
  const [hours, minutes] = timeString.split(':').map(Number);
  const date = new Date()
  date.setHours(hours, minutes, 0, 0);
  return date;
}
