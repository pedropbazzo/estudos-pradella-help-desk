import { Component, OnInit } from '@angular/core';

import { SharedService } from './services/shared.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  

  showtemplate: boolean;
  shared: SharedService

  constructor(){
    this.shared = SharedService.getInstance();
  }

  ngOnInit(){
    this.shared.showTemplate.subscribe(
      show => this.showtemplate = show
    ); 
  }

  showContentWrapper(){
    return {
      'content-wrapper' : this.shared.isLogged()
    }
  }
}
