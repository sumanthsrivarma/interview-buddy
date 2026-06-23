import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TopicBreakdown, WeakArea, WeeklyStreak } from '../models/dashboard.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api/dashboard';

  getSummary(): Observable<TopicBreakdown[]> {
    return this.http.get<TopicBreakdown[]>(`${this.base}/summary`);
  }

  getWeakAreas(): Observable<WeakArea[]> {
    return this.http.get<WeakArea[]>(`${this.base}/weak-areas`);
  }

  getStreak(): Observable<WeeklyStreak[]> {
    return this.http.get<WeeklyStreak[]>(`${this.base}/streak`);
  }
}
