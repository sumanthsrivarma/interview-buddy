import { Component, OnInit, inject } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';

import { SessionService } from '../../../core/services/session.service';
import { SessionRequest, SessionResponse } from '../../../core/models/session.model';

@Component({
  selector: 'app-session-history',
  standalone: true,
  imports: [DatePipe, RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatTableModule],
  templateUrl: './session-history.component.html',
  styleUrl: './session-history.component.scss'
})
export class SessionHistoryComponent implements OnInit {
  private readonly sessionService = inject(SessionService);
  private readonly router = inject(Router);

  sessions: SessionResponse[] = [];
  displayedColumns = ['name', 'topic', 'difficulty', 'questionCount', 'date', 'actions'];

  ngOnInit(): void {
    this.sessionService.history().subscribe(s => this.sessions = s);
  }

  review(id: string): void { this.router.navigate(['/sessions', id, 'review']); }

  reattempt(session: SessionResponse): void {
    const request: SessionRequest = {
      name: session.name + ' (retry)',
      topic: session.topic ?? undefined,
      techStack: session.techStack ?? undefined,
      difficultyLevel: session.difficultyLevel ?? undefined,
      experienceRange: session.experienceRange,
      questionCount: session.questionCount
    };
    this.sessionService.create(request).subscribe(s => {
      this.router.navigate(['/sessions', s.id]);
    });
  }
}
