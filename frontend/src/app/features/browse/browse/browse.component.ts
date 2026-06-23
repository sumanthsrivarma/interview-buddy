import { Component, OnInit, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { QuestionService } from '../../../core/services/question.service';
import { BookmarkService } from '../../../core/services/bookmark.service';
import { DifficultyLevel, Question, TechStack, Topic } from '../../../core/models/question.model';

@Component({
  selector: 'app-browse',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatInputModule,
    MatTooltipModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './browse.component.html',
  styleUrl: './browse.component.scss'
})
export class BrowseComponent implements OnInit {
  private readonly questionService = inject(QuestionService);
  private readonly bookmarkService = inject(BookmarkService);

  questions: Question[] = [];
  expandedId: string | null = null;
  bookmarkMap = new Map<string, string>(); // questionId -> bookmarkId
  loading = false;

  readonly topics = Object.values(Topic);
  readonly techStacks = Object.values(TechStack);
  readonly difficulties = Object.values(DifficultyLevel);

  filterForm = new FormGroup({
    topic: new FormControl<Topic | null>(null),
    techStack: new FormControl<TechStack | null>(null),
    difficultyLevel: new FormControl<DifficultyLevel | null>(null)
  });

  ngOnInit(): void {
    this.loadQuestions();
    this.loadBookmarks();
  }

  loadQuestions(): void {
    this.loading = true;
    const { topic, techStack, difficultyLevel } = this.filterForm.value;
    this.questionService.list({
      topic: topic ?? undefined,
      techStack: techStack ?? undefined,
      difficultyLevel: difficultyLevel ?? undefined,
      size: 100
    }).subscribe({
      next: page => { this.questions = page.content; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  loadBookmarks(): void {
    this.bookmarkService.list().subscribe(bookmarks => {
      this.bookmarkMap.clear();
      bookmarks.forEach(b => this.bookmarkMap.set(b.question.id, b.id));
    });
  }

  applyFilters(): void { this.loadQuestions(); }

  resetFilters(): void {
    this.filterForm.reset();
    this.loadQuestions();
  }

  toggleExpand(id: string): void {
    this.expandedId = this.expandedId === id ? null : id;
  }

  isBookmarked(questionId: string): boolean {
    return this.bookmarkMap.has(questionId);
  }

  toggleBookmark(questionId: string, event: Event): void {
    event.stopPropagation();
    if (this.isBookmarked(questionId)) {
      const bookmarkId = this.bookmarkMap.get(questionId)!;
      this.bookmarkService.delete(bookmarkId).subscribe(() => {
        this.bookmarkMap.delete(questionId);
      });
    } else {
      this.bookmarkService.create({ questionId }).subscribe(b => {
        this.bookmarkMap.set(b.question.id, b.id);
      });
    }
  }
}
