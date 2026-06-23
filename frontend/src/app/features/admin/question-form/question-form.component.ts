import { Component, OnInit, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatChipsModule, MatChipInputEvent } from '@angular/material/chips';
import { COMMA, ENTER } from '@angular/cdk/keycodes';

import { QuestionService } from '../../../core/services/question.service';
import {
  DifficultyLevel,
  Question,
  QuestionRequest,
  TechStack,
  Topic
} from '../../../core/models/question.model';

@Component({
  selector: 'app-question-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule
  ],
  templateUrl: './question-form.component.html',
  styleUrl: './question-form.component.scss'
})
export class QuestionFormComponent implements OnInit {
  private readonly questionService = inject(QuestionService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  isEditMode = false;
  questionId: string | null = null;
  saving = false;

  readonly topics = Object.values(Topic);
  readonly techStacks = Object.values(TechStack);
  readonly difficulties = Object.values(DifficultyLevel);
  readonly separatorKeysCodes = [ENTER, COMMA] as const;

  tags: string[] = [];

  form = new FormGroup({
    title: new FormControl('', [Validators.required, Validators.maxLength(500)]),
    body: new FormControl('', [Validators.required]),
    answer: new FormControl(''),
    followUpProbes: new FormControl(''),
    topic: new FormControl<Topic | null>(null, Validators.required),
    techStack: new FormControl<TechStack | null>(null, Validators.required),
    difficultyLevel: new FormControl<DifficultyLevel | null>(null, Validators.required),
    experienceRangeMin: new FormControl<number | null>(null),
    experienceRangeMax: new FormControl<number | null>(null),
    active: new FormControl(true)
  });

  ngOnInit(): void {
    this.questionId = this.route.snapshot.paramMap.get('id');
    this.isEditMode = !!this.questionId;

    if (this.isEditMode && this.questionId) {
      this.questionService.getById(this.questionId).subscribe((q: Question) => {
        this.form.patchValue({
          title: q.title,
          body: q.body,
          answer: q.answer ?? '',
          followUpProbes: q.followUpProbes ?? '',
          topic: q.topic,
          techStack: q.techStack,
          difficultyLevel: q.difficultyLevel,
          experienceRangeMin: q.experienceRangeMin,
          experienceRangeMax: q.experienceRangeMax,
          active: q.active
        });
        this.tags = [...q.tags];
      });
    }
  }

  addTag(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    if (value) {
      this.tags = [...this.tags, value];
    }
    event.chipInput.clear();
  }

  removeTag(tag: string): void {
    this.tags = this.tags.filter(t => t !== tag);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving = true;
    const request: QuestionRequest = {
      title: this.form.value.title!,
      body: this.form.value.body!,
      answer: this.form.value.answer || undefined,
      followUpProbes: this.form.value.followUpProbes || undefined,
      topic: this.form.value.topic!,
      techStack: this.form.value.techStack!,
      difficultyLevel: this.form.value.difficultyLevel!,
      experienceRangeMin: this.form.value.experienceRangeMin,
      experienceRangeMax: this.form.value.experienceRangeMax,
      tags: this.tags
    };

    const call = this.isEditMode && this.questionId
      ? this.questionService.update(this.questionId, request)
      : this.questionService.create(request);

    call.subscribe({
      next: () => this.router.navigate(['/admin/questions']),
      error: () => { this.saving = false; }
    });
  }

  cancel(): void {
    this.router.navigate(['/admin/questions']);
  }
}
