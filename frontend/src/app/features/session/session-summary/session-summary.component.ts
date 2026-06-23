import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';

import { SessionService } from '../../../core/services/session.service';
import { Confidence, SessionDetailResponse } from '../../../core/models/session.model';

@Component({
  selector: 'app-session-summary',
  standalone: true,
  imports: [RouterLink, MatCardModule, MatButtonModule, MatListModule, MatIconModule],
  templateUrl: './session-summary.component.html',
  styleUrl: './session-summary.component.scss'
})
export class SessionSummaryComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly sessionService = inject(SessionService);

  session: SessionDetailResponse | null = null;
  readonly Confidence = Confidence;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.sessionService.getById(id).subscribe(s => this.session = s);
  }

  confidenceIcon(c: Confidence | null): string {
    if (c === Confidence.CONFIDENT) return 'check_circle';
    if (c === Confidence.NEEDS_WORK) return 'warning';
    if (c === Confidence.SKIPPED) return 'skip_next';
    return 'radio_button_unchecked';
  }

  confidenceColor(c: Confidence | null): string {
    if (c === Confidence.CONFIDENT) return 'confident';
    if (c === Confidence.NEEDS_WORK) return 'needs-work';
    return 'neutral';
  }

  newSession(): void { this.router.navigate(['/sessions/new']); }
}
