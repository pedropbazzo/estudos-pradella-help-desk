import { User } from './user';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'tasks';
  upperText: String = 'Display UpperCase exemple';
  lowerText: String = 'Display lowerCase exemple';
  percentValue: number = 0.5;
  date: Date = new Date();
  money: number = 1000;
  isAdmin0: boolean = true;
  profile: number = 2;
  user : User = {
    name: 'Jhon Snow',
    age: 20
  }
 
  
}
