import { map } from 'rxjs/operators';
import { Comment } from './coments.model';
import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';


@Injectable()
export class CommentsService {

  constructor(private http: Http) { }

  getComments(): Observable<Comment[]>{
    return this.http.get('https://jsonplaceholder.typicode.com/comments')
                                          .pipe(map(response => response.json()));

  }
}
