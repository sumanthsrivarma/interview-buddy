import { Component, OnInit, inject } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { SessionService } from '../../../core/services/session.service';
import { AttemptResponse, Confidence, SessionDetailResponse } from '../../../core/models/session.model';

@Component({
  selector: 'app-active-session',
  standalone: true,
  imports: [ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule,
            MatFormFieldModule, MatInputModule, MatProgressBarModule],
  templateUrl: './active-session.component.html',
  styleUrl: './active-session.component.scss'
})
export class ActiveSessionComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly sessionService = inject(SessionService);

  session: SessionDetailResponse | null = null;
  currentIndex = 0;
  showAnswer = false;
  noteControl = new FormControl('');
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
    this.sessionService.getById(id).subscribe(s => {
      this.session = s;
      this.syncNote();
    });
  }

  syncNote(): void {
    this.noteControl.setValue(this.current?.personalNote ?? '');
    this.showAnswer = false;
  }

  rate(confidence: Confidence): void {
    if (!this.session || !this.current) return;
    this.sessionService.updateAttempt(this.session.id, this.current.id, {
      confidence,
      personalNote: this.noteControl.value ?? ''
    }).subscribe(updated => {
      this.session!.attempts[this.currentIndex] = updated;
      if (this.currentIndex < this.session!.attempts.length - 1) {
        this.next();
      }
    });
  }

  saveNote(): void {
    if (!this.session || !this.current) return;
    this.sessionService.updateAttempt(this.session.id, this.current.id, {
      confidence: this.current.confidence,
      personalNote: this.noteControl.value ?? ''
    }).subscribe(updated => {
      this.session!.attempts[this.currentIndex] = updated;
    });
  }

  next(): void {
    if (!this.session || this.currentIndex >= this.session.attempts.length - 1) return;
    this.currentIndex++;
    this.syncNote();
  }

  prev(): void {
    if (this.currentIndex === 0) return;
    this.currentIndex--;
    this.syncNote();
  }

  finish(): void {
    if (!this.session) return;
    this.sessionService.complete(this.session.id).subscribe(s => {
      this.router.navigate(['/sessions', s.id, 'summary']);
    });
  }
}
