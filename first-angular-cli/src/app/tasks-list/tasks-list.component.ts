import { Component} from '@angular/core';

@Component({
  selector: 'app-tasks-list',
  templateUrl: './tasks-list.component.html',
  styleUrls: ['./tasks-list.component.css']
})
export class TasksListComponent  {
  tasks = [];
  task="";
  isAdmin=true;

  add():void{
    this.tasks.push(this.task)
  };

}
