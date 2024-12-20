import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";


export interface HashResponse {
    hash: string;
}

@Injectable({
    providedIn: 'root'
})
export class SingleregService {
    private apiUrl = 'http://0.0.0.0:8080/121queue';

    constructor(private http: HttpClient) {
    }

    singleReg(): Observable<HashResponse> {
        return this.http.get<HashResponse>(this.apiUrl);

    }
}
