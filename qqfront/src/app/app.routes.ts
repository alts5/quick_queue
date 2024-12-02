import { Routes } from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {GreetingComponent} from './pages/greeting/greeting.component';
import {ErrorComponent} from './pages/error/error.component';
import {SoonComponent} from './pages/soon/soon.component';
import {SelectComponent} from './pages/select/select.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {path: 'greeting', component: GreetingComponent},
  {path: 'error', component: ErrorComponent},
  {path:'soon', component:SoonComponent},
  {path:'select',component:SelectComponent},
];
