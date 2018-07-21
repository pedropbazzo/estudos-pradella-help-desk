import { CommentsService } from './comments.service';
import { Comment } from './coments.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-service-test',
  templateUrl: './service-test.component.html',
  styleUrls: ['./service-test.component.css']
})
export class ServiceTestComponent implements OnInit {

  comments: Comment [];
  constructor(private commentsService: CommentsService) { }

  ngOnInit() {
    this.commentsService.getComments()
                              .subscribe(comments => this.comments = comments);
  }

}
