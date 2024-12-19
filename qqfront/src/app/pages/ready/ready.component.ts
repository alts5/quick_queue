import { Component } from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {TimeComponent} from '../../components/time/time.component';
import {QRCodeModule} from 'angularx-qrcode';

@Component({
  selector: 'app-ready',
  standalone: true,
  imports: [
    TimeComponent,
    RouterOutlet,
    QRCodeModule
  ],
  templateUrl: './ready.component.html',
  styleUrl: './ready.component.scss'
})
export class ReadyComponent {
constructor() {

}


}

