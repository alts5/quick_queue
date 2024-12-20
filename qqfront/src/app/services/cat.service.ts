import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Docs} from "./doc.service";

@Injectable({
  providedIn: 'root'
})
export class CatService {
  private apiUrl = 'http://0.0.0.0:8080/show_categories';
  constructor(private http: HttpClient) { }
  getCats():Observable<Docs[]> {
   // @ts-ignore
    return  this.http.get<Observable<Docs[]>>(this.apiUrl)
  }
}

export interface Cat{
  id:number
  name:string
  stat:string
  description:string
}