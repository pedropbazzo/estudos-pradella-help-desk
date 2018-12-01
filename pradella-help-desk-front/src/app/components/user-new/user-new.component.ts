import { ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';

import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from './../../services/user.service';
import { SharedService } from './../../services/shared.service';
import { User } from 'src/app/model/user.model';
import { ResponseApi } from 'src/app/model/response-api';

@Component({
  selector: 'app-user-new',
  templateUrl: './user-new.component.html',
  styleUrls: ['./user-new.component.css']
})
export class UserNewComponent implements OnInit {

  @ViewChild("form")
  form: NgForm;

  user: User = new User('', '', '', '');
  shared: SharedService;
  message: {};
  classCss: {};

  constructor(
    private userService: UserService,
    private router: ActivatedRoute
  ) {
    this.shared = SharedService.getInstance();
  }


  ngOnInit() {
    let id: string = this.router.snapshot.params['id'];
    if(id != undefined){
      this.findById(id);
    } else {
      this.user = new User('', '', '', '');
      this.form.resetForm();
    }
  }

  findById(id: string) {
    this.userService.findById(id).subscribe((responseApi: ResponseApi) => {
      this.user = responseApi.data;
      this.user.password = '';
    }, err => {
      this.showMessage({
        type: 'error',
        text: err['error']['errors'][0]
      });
    });
  }

  register() {
    this.message = {};
    this.userService.createOrUpdate(this.user).subscribe((responseApi: ResponseApi) => {
      this.user = new User('', '', '', '');
      let userRet: User = responseApi.data;
      this.form.resetForm();
      this.showMessage({
        type: 'success',
        text: `Registered ${userRet.email} sucessfully`
      });
    }, err => {
      this.showMessage({
        type: 'error',
        text: err['error']['errors'][0]
      });
    });
  }

  private showMessage(message: { type: string, text: string }): void {
    this.message = message;
    this.buildClasses(message.type);
    setTimeout(() => {
      this.message = undefined;
    }, 3000);
  }

  private buildClasses(type: string): void {
    this.classCss = {
      'alert': true
    }
    this.classCss[`alert-${type}`] = true
  }

  getFormGroupClass(isInvalid: boolean, isDirty): {} {
    return {
      'form-group': true,
      'has-error':isInvalid && isDirty,
      'has-success':!isInvalid && isDirty        
    }
}

}
