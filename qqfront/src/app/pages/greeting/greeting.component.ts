import { Component } from '@angular/core';
import {RouterOutlet} from "@angular/router";
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

}
