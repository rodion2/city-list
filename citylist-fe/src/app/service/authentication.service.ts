import {Injectable} from '@angular/core';
import {delay, Observable, of} from 'rxjs';
import {ApiService} from "../api/api.service";
import * as _ from "lodash";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({providedIn: 'root'})
export class AuthenticationService {

  constructor(private apiService: ApiService,
              private httpClient: HttpClient) {
  }

  get isAuthenticated(): boolean {
    if (!_.isEmpty(sessionStorage.getItem('credentials'))) {
      return true;
    }
    return false;
  }

  login(formData: { username: string; password: string }): Observable<boolean> {
    this.httpClient.get("localhost:8080/api/v1/user-details", ).subscribe((data) => {
      sessionStorage.setItem('credentials',
        JSON.stringify(new AuthenticationDetails(formData.password, formData.username)));
    }, (error) => {
      console.log(error);
    });

    return of(true).pipe(delay(3000));
  }

  logout() {
    sessionStorage.removeItem('credentials');
    window.location.href = '/';
  }
}
