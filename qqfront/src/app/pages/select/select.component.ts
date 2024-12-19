import {Component, Input} from '@angular/core';
import {NgForOf} from "@angular/common";
import {ReactiveFormsModule} from "@angular/forms";
import {Router, RouterOutlet} from "@angular/router";
import {TimeComponent} from "../../components/time/time.component";
import {Service, ServicesService} from '../../services/services.service';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

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
  public defaultServices : Service[] = []
  public selectedServices : Service[] = []
  constructor(private router: Router, private servicesService: ServicesService) {}
  ngOnInit() {
    this.servicesService.getServices().pipe(
      map((services: Service[]) => {
        this.defaultServices = services;
      })
    ).subscribe();
  }

  selectService(service: Service) {
    if (!this.selectedServices.includes(service)) {
      this.selectedServices.push(service);
      this.removeFromDefault(service);
    }
  }

  deselectService(service: Service) {
    this.defaultServices.push(service);
    this.removeFromSelected(service);
  }
  private removeFromDefault(service: Service) {
    const index = this.defaultServices.indexOf(service);
    if (index !== -1) {
      this.defaultServices.splice(index, 1);
    }
  }

  private removeFromSelected(service: Service) {
    const index = this.selectedServices.indexOf(service);
    if (index !== -1) {
      this.selectedServices.splice(index, 1);
    }
  }

  navigateToMain() {
    this.router.navigate(['/']);
  }
}
