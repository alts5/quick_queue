import {Component} from '@angular/core';
import {Router, RouterOutlet} from "@angular/router";
import {TimeComponent} from "../../components/time/time.component";

@Component({
  selector: 'app-start',
  standalone: true,
  imports: [
    RouterOutlet,
    TimeComponent
  ],
  templateUrl: './start.component.html',
  styleUrl: './start.component.scss'
})
export class StartComponent {
  constructor(private router: Router) {
  }

  navigateToSelect() {
    this.router.navigate(['/select']);
  }
  navigateToMain(){
    this.router.navigate(['/'])
  }
}
