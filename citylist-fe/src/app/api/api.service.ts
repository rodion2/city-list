import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {catchError, Observable, of} from 'rxjs';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  apiBasePath: string;

  constructor(private http: HttpClient) {
    this.apiBasePath = environment.config.apiUrl;
  }

  get<T>(url: string, queryParams?: any, httpHeaders?: any): Observable<T> {
    return this.http.get<T>(this.apiBasePath + url, {params: queryParams, headers: httpHeaders});
  }

  post<T>(url: string, data: any, httpHeaders: any): Observable<T> {
    return this.http
      .post<T>(this.apiBasePath + url, data, {headers: httpHeaders})
      .pipe(catchError((err) => of(err)));
  }

  put<T>(url: string, data: any, httpHeaders: any): Observable<T> {
    console.log(this.apiBasePath + url, data, httpHeaders);
    return this.http
      .put<T>(this.apiBasePath + url, data, {headers: httpHeaders})
      .pipe(catchError((err) => of(err)));
  }

  delete<T>(url: string): Observable<T> {
    return this.http
      .delete<T>(this.apiBasePath + url)
      .pipe(catchError((err) => of(err)));
  }
}
