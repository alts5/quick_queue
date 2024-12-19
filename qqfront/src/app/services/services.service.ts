import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';


export interface Service{
  id:string;
  name:string;
  stat:string;
  description:string;
}

@Injectable({
  providedIn: 'root'
})

export class ServicesService {
  private apiUrl = 'http://0.0.0.0:8080/get_services';

    constructor(private http: HttpClient) {

    }

    getServices(): Observable<Service[]> {
      return this.http.get<Service[]>(this.apiUrl);
    }
}
