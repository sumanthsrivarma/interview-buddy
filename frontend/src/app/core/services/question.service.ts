import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page, Question, QuestionFilter, QuestionRequest } from '../models/question.model';

@Injectable({ providedIn: 'root' })
export class QuestionService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api/questions';

  list(filter: QuestionFilter = {}): Observable<Page<Question>> {
    let params = new HttpParams();
    if (filter.topic) params = params.set('topic', filter.topic);
    if (filter.techStack) params = params.set('techStack', filter.techStack);
    if (filter.difficultyLevel) params = params.set('difficultyLevel', filter.difficultyLevel);
    if (filter.includeInactive) params = params.set('includeInactive', 'true');
    if (filter.page != null) params = params.set('page', filter.page);
    if (filter.size != null) params = params.set('size', filter.size);
    return this.http.get<Page<Question>>(this.base, { params });
  }

  getById(id: string): Observable<Question> {
    return this.http.get<Question>(`${this.base}/${id}`);
  }

  create(request: QuestionRequest): Observable<Question> {
    return this.http.post<Question>(this.base, request);
  }

  getTopics(): Observable<string[]> {
    return this.http.get<string[]>(`${this.base}/topics`);
  }

  getStacks(): Observable<string[]> {
    return this.http.get<string[]>(`${this.base}/stacks`);
  }
}
