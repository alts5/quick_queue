import { Component } from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {TimeComponent} from "../../components/time/time.component";

@Component({
  selector: 'app-error',
  standalone: true,
    imports: [
        RouterOutlet,
        TimeComponent
    ],
  templateUrl: './error.component.html',
  styleUrl: './error.component.scss'
})
export class ErrorComponent {

}
