import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class RegService {

  private apiUrl = 'http://0.0.0.0:8080/reggg'
  constructor(private http: HttpClient) { }

  // @ts-ignore
  Reg(cat,dtype,dnumber,downer):Observable<regService>{
    var url=this.apiUrl+"?category="+cat+"&dtype="+dtype+"&downer="+downer+"&dnumber="+dnumber
    return this.http.get<regService>(url);
  }
}


export interface regService {
  hash:string
  category: number
}
