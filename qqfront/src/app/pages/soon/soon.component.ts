import {Component, Input} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {TimeComponent} from "../../components/time/time.component";

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
  @Input() startTime: string = 'ЧЧ:ММ'
}
