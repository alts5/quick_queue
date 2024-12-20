import {Component, Input} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {Config, ConfigService} from './services/config.service';
import {map} from 'rxjs/operators';
import {config} from 'rxjs';
import {GreetingComponent} from './pages/greeting/greeting.component';
import {FormsModule} from '@angular/forms';
import {appConfig} from './app.config';
import {LoginComponent} from './pages/login/login.component';
import {SelectComponent} from './pages/select/select.component';
import {ReadyComponent} from './pages/ready/ready.component';
import {ErrorComponent} from './pages/error/error.component';
import {SoonComponent} from './pages/soon/soon.component';
import {LkComponent} from "./pages/lk/lk.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, GreetingComponent, FormsModule, LoginComponent, SelectComponent, ReadyComponent, ErrorComponent, SoonComponent, LkComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
appData= {
  startTime: new Date(),
  endTime: new Date(),
  mode: "",
  step: "",
  cat:0,
  doc:0,
  fullName:"",
  documentData:"",
  services:"",
}
  constructor(private configService: ConfigService) {
  }

  ngOnInit() {

    this.configService.getConfig().pipe(
      map((config:Config) => {
        this.appData.startTime = parseTime(config.startTime);
        this.appData.endTime = parseTime(config.endTime);
        this.appData.mode = config.systemMode;
        this.appData.step="root"
      })).subscribe()
  }
  protected readonly appConfig = appConfig;

  handleChildUpdate(updates: { [key: string]: any }) {
    Object.assign(this.appData, updates); // Обновляем несколько полей сразу
  }
}

function parseTime(timeString: string): Date {
  const [hours, minutes] = timeString.split(':').map(Number);
  const date = new Date()
  date.setHours(hours, minutes, 0, 0);
  return date;
}

