import { Component, OnInit, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

import { SessionService } from '../../../core/services/session.service';
import { DifficultyLevel, TechStack, Topic } from '../../../core/models/question.model';

@Component({
  selector: 'app-session-config',
  standalone: true,
  imports: [ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule],
  templateUrl: './session-config.component.html',
  styleUrl: './session-config.component.scss'
})
export class SessionConfigComponent {
  private readonly sessionService = inject(SessionService);
  private readonly router = inject(Router);

  readonly topics = Object.values(Topic);
  readonly techStacks = Object.values(TechStack);
  readonly difficulties = Object.values(DifficultyLevel);
  creating = false;

  form = new FormGroup({
    name: new FormControl('', Validators.required),
    topic: new FormControl<Topic | null>(null),
    techStack: new FormControl<TechStack | null>(null),
    difficultyLevel: new FormControl<DifficultyLevel | null>(null),
    experienceRange: new FormControl<number | null>(null),
    questionCount: new FormControl(10, [Validators.required, Validators.min(1), Validators.max(50)])
  });

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.creating = true;
    const v = this.form.value;
    this.sessionService.create({
      name: v.name!,
      topic: v.topic ?? undefined,
      techStack: v.techStack ?? undefined,
      difficultyLevel: v.difficultyLevel ?? undefined,
      experienceRange: v.experienceRange,
      questionCount: v.questionCount!
    }).subscribe({
      next: session => this.router.navigate(['/sessions', session.id]),
      error: (err) => { this.creating = false; alert(err?.error?.detail ?? 'Failed to create session'); }
    });
  }
}
