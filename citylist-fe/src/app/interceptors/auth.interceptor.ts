import {Injectable} from "@angular/core";
import {HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from "@angular/common/http";

@Injectable({providedIn: 'root'})
export class AuthInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    if (sessionStorage.getItem('username') && sessionStorage.getItem('basicauth')) {
      const basicAuth = sessionStorage.getItem('basicauth') ?? '';
      req = req.clone({
        headers: new HttpHeaders({
          Authorization: basicAuth.toString()
        }),
      })
    }

    return next.handle(req);
  }
}
