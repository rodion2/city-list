import {Injectable} from '@angular/core';
import {HttpHeaders} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {Observable} from "rxjs";
import {ApiService} from "../api/api.service";

export class User {
  constructor(
    public status: string,
    public roles: string[]
  ) { }

}

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(
    private api: ApiService,
  ) {
  }

  login(formData: { username: string; password: string }): Observable<User> {
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa(formData.username + ':' + formData.password) });
    return this.api.get<User>('authentication/validateLogin', {},{ headers }).pipe(
      map(
        userData => {
          sessionStorage.setItem('username', formData.username);
          let authString = 'Basic ' + btoa(formData.username + ':' + formData.password);
          sessionStorage.setItem('basicauth', authString);
          sessionStorage.setItem('roles', JSON.stringify(userData.roles));
          return userData;
        }
      )
    );
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem('username')
    return !(user === null)
  }

  hasRole(role: string) {
    let roles: string[] = JSON.parse(sessionStorage.getItem('roles')?.toString() ?? "");
    return roles.includes(role);
  }

  logout() {
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('basicauth');
    sessionStorage.removeItem('roles');
  }
}
