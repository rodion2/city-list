import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../service/authentication.service";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.sass']
})
export class LoginDialogComponent implements OnInit {
  username: string = "";
  password: string = "";

  constructor(private authenticationService: AuthenticationService,
              public dialogRef: MatDialogRef<LoginDialogComponent>) {
  }

  ngOnInit(): void {
  }

  login(username: string, password: string) {
    this.authenticationService.login({username, password}).subscribe(result => {
      this.dialogRef.close(this.authenticationService.isUserLoggedIn());
    });
  }

}
