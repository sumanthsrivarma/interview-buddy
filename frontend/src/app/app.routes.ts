import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'browse',
    pathMatch: 'full'
  },
  {
    path: 'admin/questions',
    loadComponent: () =>
      import('./features/admin/question-list/question-list.component')
        .then((m) => m.QuestionListComponent)
  },
  {
    path: 'admin/questions/new',
    loadComponent: () =>
      import('./features/admin/question-form/question-form.component')
        .then((m) => m.QuestionFormComponent)
  },
  {
    path: 'admin/questions/:id/edit',
    loadComponent: () =>
      import('./features/admin/question-form/question-form.component')
        .then((m) => m.QuestionFormComponent)
  },
  {
    path: 'browse',
    loadComponent: () =>
      import('./features/browse/browse/browse.component')
        .then((m) => m.BrowseComponent)
  },
  {
    path: 'browse/:id',
    loadComponent: () =>
      import('./features/browse/question-detail/question-detail.component')
        .then((m) => m.QuestionDetailComponent)
  },
  {
    path: 'sessions/new',
    loadComponent: () =>
      import('./features/session/session-config/session-config.component')
        .then((m) => m.SessionConfigComponent)
  },
  {
    path: 'sessions/history',
    loadComponent: () =>
      import('./features/session/session-history/session-history.component')
        .then((m) => m.SessionHistoryComponent)
  },
  {
    path: 'sessions/:id',
    loadComponent: () =>
      import('./features/session/active-session/active-session.component')
        .then((m) => m.ActiveSessionComponent)
  },
  {
    path: 'sessions/:id/summary',
    loadComponent: () =>
      import('./features/session/session-summary/session-summary.component')
        .then((m) => m.SessionSummaryComponent)
  },
  {
    path: 'sessions/:id/review',
    loadComponent: () =>
      import('./features/session/session-review/session-review.component')
        .then((m) => m.SessionReviewComponent)
  },
  {
    path: 'bookmarks',
    loadComponent: () =>
      import('./features/browse/bookmarks/bookmarks.component')
        .then((m) => m.BookmarksComponent)
  },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./features/dashboard/dashboard/dashboard.component')
        .then((m) => m.DashboardComponent)
  }
];
