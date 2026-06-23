import { Component, OnInit, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';

import { QuestionService } from '../../../core/services/question.service';
import { DifficultyLevel, Question, TechStack, Topic } from '../../../core/models/question.model';

@Component({
  selector: 'app-question-list',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatTooltipModule
  ],
  templateUrl: './question-list.component.html',
  styleUrl: './question-list.component.scss'
})
export class QuestionListComponent implements OnInit {
  private readonly questionService = inject(QuestionService);
  private readonly router = inject(Router);

  displayedColumns = ['title', 'topic', 'techStack', 'difficultyLevel', 'experienceRange', 'status', 'actions'];
  dataSource: Question[] = [];
  totalElements = 0;
  pageSize = 20;
  pageIndex = 0;

  readonly topics = Object.values(Topic);
  readonly techStacks = Object.values(TechStack);
  readonly difficulties = Object.values(DifficultyLevel);

  filterForm = new FormGroup({
    topic: new FormControl<Topic | null>(null),
    techStack: new FormControl<TechStack | null>(null),
    difficultyLevel: new FormControl<DifficultyLevel | null>(null),
    includeInactive: new FormControl(false)
  });

  ngOnInit(): void {
    this.loadQuestions();
  }

  loadQuestions(): void {
    const { topic, techStack, difficultyLevel, includeInactive } = this.filterForm.value;
    this.questionService.list({
      topic: topic ?? undefined,
      techStack: techStack ?? undefined,
      difficultyLevel: difficultyLevel ?? undefined,
      includeInactive: includeInactive ?? false,
      page: this.pageIndex,
      size: this.pageSize
    }).subscribe(page => {
      this.dataSource = page.content;
      this.totalElements = page.totalElements;
    });
  }

  applyFilters(): void {
    this.pageIndex = 0;
    this.loadQuestions();
  }

  resetFilters(): void {
    this.filterForm.reset({ topic: null, techStack: null, difficultyLevel: null, includeInactive: false });
    this.pageIndex = 0;
    this.loadQuestions();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadQuestions();
  }

  editQuestion(id: string): void {
    this.router.navigate(['/admin/questions', id, 'edit']);
  }

  deactivateQuestion(id: string): void {
    this.questionService.deactivate(id).subscribe(() => this.loadQuestions());
  }
}
