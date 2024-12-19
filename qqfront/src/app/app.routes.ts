import { Routes } from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {GreetingComponent} from './pages/greeting/greeting.component';
import {ErrorComponent} from './pages/error/error.component';
import {SoonComponent} from './pages/soon/soon.component';
import {SelectComponent} from './pages/select/select.component';
import {StartComponent} from './pages/start/start.component';
import {ReadyComponent} from './pages/ready/ready.component';
import {AppComponent} from './app.component';

export const routes: Routes = [
  {path:'greeting',component :GreetingComponent},
  { path: 'login', component: LoginComponent},
    {path: 'ensure', component: StartComponent},
  {path: 'error', component: ErrorComponent},
  {path:'soon', component:SoonComponent},
  {path:'select',component:SelectComponent},
  {path:'ready',component:ReadyComponent},
  {path:'',component:AppComponent},
];
