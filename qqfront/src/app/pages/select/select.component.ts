import {Component, Input} from '@angular/core';
import {NgForOf} from "@angular/common";
import {ReactiveFormsModule} from "@angular/forms";
import {RouterOutlet} from "@angular/router";
import {TimeComponent} from "../../components/time/time.component";

@Component({
  selector: 'app-select',
  standalone: true,
    imports: [
        NgForOf,
        ReactiveFormsModule,
        RouterOutlet,
        TimeComponent
    ],
  templateUrl: './select.component.html',
  styleUrl: './select.component.scss'
})
export class SelectComponent {
  @Input() title: string = 'Пожалуйста, выберите интересующие вас услуги';
}
