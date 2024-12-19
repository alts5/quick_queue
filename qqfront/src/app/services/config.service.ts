import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Config {
  footerName: string;
  startTime: string;
  endTime: string;
  systemMode: 'single' | 'multi';
  logoPath: string;
}

@Injectable({
  providedIn: 'root',
})
export class ConfigService {
  private apiUrl = 'http://0.0.0.0:8080/system_settings';

  constructor(private http: HttpClient) {}

  getConfig(): Observable<Config> {
    return this.http.get<Config>(this.apiUrl);
  }
}
