import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { SessionService } from '../../../core/services/session.service';
import { AttemptResponse, Confidence, SessionDetailResponse } from '../../../core/models/session.model';

@Component({
  selector: 'app-session-review',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatIconModule, MatProgressBarModule],
  templateUrl: './session-review.component.html',
  styleUrl: './session-review.component.scss'
})
export class SessionReviewComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly sessionService = inject(SessionService);

  session: SessionDetailResponse | null = null;
  currentIndex = 0;
  readonly Confidence = Confidence;

  get current(): AttemptResponse | null {
    return this.session?.attempts[this.currentIndex] ?? null;
  }

  get progress(): number {
    if (!this.session) return 0;
    return ((this.currentIndex + 1) / this.session.attempts.length) * 100;
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.sessionService.getById(id).subscribe(s => this.session = s);
  }

  next(): void { if (this.session && this.currentIndex < this.session.attempts.length - 1) this.currentIndex++; }
  prev(): void { if (this.currentIndex > 0) this.currentIndex--; }

  confidenceLabel(c: Confidence | null): string {
    if (c === Confidence.CONFIDENT) return '✓ Confident';
    if (c === Confidence.NEEDS_WORK) return '⚠ Needs Work';
    if (c === Confidence.SKIPPED) return '→ Skipped';
    return '— Not rated';
  }
  confidenceClass(c: Confidence | null): string {
    if (c === Confidence.CONFIDENT) return 'confident';
    if (c === Confidence.NEEDS_WORK) return 'needs-work';
    return 'neutral';
  }
}
