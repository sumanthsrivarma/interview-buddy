import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  AttemptRequest,
  AttemptResponse,
  SessionDetailResponse,
  SessionRequest,
  SessionResponse
} from '../models/session.model';

@Injectable({ providedIn: 'root' })
export class SessionService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api/sessions';

  list(): Observable<SessionResponse[]> {
    return this.http.get<SessionResponse[]>(this.base);
  }

  history(): Observable<SessionResponse[]> {
    return this.http.get<SessionResponse[]>(`${this.base}/history`);
  }

  create(request: SessionRequest): Observable<SessionDetailResponse> {
    return this.http.post<SessionDetailResponse>(this.base, request);
  }

  getById(id: string): Observable<SessionDetailResponse> {
    return this.http.get<SessionDetailResponse>(`${this.base}/${id}`);
  }

  complete(id: string): Observable<SessionDetailResponse> {
    return this.http.put<SessionDetailResponse>(`${this.base}/${id}/complete`, {});
  }

  updateAttempt(sessionId: string, attemptId: string, request: AttemptRequest): Observable<AttemptResponse> {
    return this.http.put<AttemptResponse>(`${this.base}/${sessionId}/attempts/${attemptId}`, request);
  }
}
