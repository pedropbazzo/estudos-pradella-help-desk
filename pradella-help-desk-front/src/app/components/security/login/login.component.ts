import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';

import { UserService } from './../../../services/user.service';
import { SharedService } from './../../../services/shared.service';
import { User } from 'src/app/model/user.model';
import { CurrentUser } from 'src/app/model/current-user.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user = new User('', '', '', '');
  shared: SharedService;
  message: string;

  constructor(
    private userService: UserService,
    private router: Router,
  ) {
    this.shared = SharedService.getInstance();
  }

  ngOnInit() {
  }

  login() {
    this.message = '';
    this.userService.login(this.user).subscribe((userAuthentication: CurrentUser) => {
      this.shared.user = userAuthentication.user;
      this.shared.token = userAuthentication.token;
      this.shared.user.profile = userAuthentication.user.profile.substring(5);
      this.shared.showTemplate.emit(true);
      this.router.navigate(['/']);
    }, err => {
      this.shared.user = null;
      this.shared.token = null;
      this.shared.showTemplate.emit(false);
      this.message = 'Error';
    });
  }

  cancelLogin() {
    this.user = new User('','','','');
    this.message = '';
    window.location.href = '/login';
    window.location.reload();
  }

  getFormGroupClass(isInvalid: boolean, isDirty): {} {
      return {
        'form-group': true,
        'has-error':isInvalid && isDirty,
        'has-success':!isInvalid && isDirty        
      }
  }

}
