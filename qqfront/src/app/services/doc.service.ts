import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

// @ts-ignore
@Injectable({
  providedIn: 'root'
})

export class DocService {
  private apiUrl = 'http://0.0.0.0:8080/show_docs';
  constructor(private http: HttpClient) { }
  getDocs():Observable<Docs[]> {
    // @ts-ignore
    return this.http.get<Observable<Docs[]>>(this.apiUrl)
  }
}

export interface Docs{
  id: number,
  label: string,
  stat: string,
  description: string
}