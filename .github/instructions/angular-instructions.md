---
applyTo: "frontend/src/**/*.ts"
---

# Angular Frontend Instructions — InterviewBuddy

## 1. Scope

This file applies to all TypeScript files under `frontend/src/`, including components, services, models, route definitions, and application configuration. It does not govern `.html` templates or `.scss` files beyond what is referenced here.

---

## 2. Component Rules

### Standalone only

Every component, directive, and pipe must declare `standalone: true`. The project has no `NgModule` files and must never gain any. There is no `AppModule`, `SharedModule`, or `FeatureModule`. Import everything a component needs directly in its `imports` array.

```typescript
@Component({
  selector: 'app-bookmark-card',
  standalone: true,
  imports: [MatCardModule, MatIconModule, MatButtonModule],
  templateUrl: './bookmark-card.component.html',
  styleUrl: './bookmark-card.component.scss'
})
export class BookmarkCardComponent { }
```

### Change detection

Use `ChangeDetectionStrategy.OnPush` on every new component. This requires state to be immutable or signal-based — update component state by reassigning references, not by mutating arrays or objects in place.

```typescript
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-bookmark-card',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  // ...
})
export class BookmarkCardComponent { }
```

### Dependency injection

Use Angular's `inject()` function for all dependencies inside a component. Do not declare constructor parameters. Declare injected dependencies as `private readonly` fields at the top of the class body.

```typescript
import { inject } from '@angular/core';
import { BookmarkService } from '../../../core/services/bookmark.service';

export class BookmarksComponent {
  private readonly bookmarkService = inject(BookmarkService);
  private readonly router = inject(Router);
}
```

### Typed `@Input()` and `@Output()`

All `@Input()` properties must have an explicit TypeScript type. All `@Output()` properties must be `EventEmitter<T>` with a concrete type argument — never `EventEmitter<any>`.

```typescript
@Input({ required: true }) bookmark!: Bookmark;
@Input() editable: boolean = false;
@Output() noteUpdated = new EventEmitter<string>();
```

### No business logic in components

Components orchestrate UI and delegate to services. They must not:
- Compute derived data beyond simple template expressions
- Contain filtering, sorting, or transformation of API data
- Call `HttpClient` directly
- Make routing decisions based on domain rules (use the service result to determine the route, then navigate)

---

## 3. Angular Material

Always use Angular Material components. Never write raw `<input>`, `<button>`, `<select>`, `<table>`, or `<form>` elements where a Material equivalent exists.

Import each Material module individually in the component's `imports` array. Do not import `MaterialModule` (it does not exist in this project) or any barrel that re-exports all Material modules.

### Material components used in this project

| Component | Module import | Usage |
|---|---|---|
| `mat-toolbar` | `MatToolbarModule` | Top navigation bar in `app.component` |
| `mat-sidenav` / `mat-sidenav-container` | `MatSidenavModule` | App shell navigation drawer |
| `mat-nav-list` | `MatListModule` | Navigation links inside the sidenav |
| `mat-card` | `MatCardModule` | Question cards, session cards, bookmark cards |
| `mat-form-field` | `MatFormFieldModule` | Every form input wrapper |
| `mat-select` | `MatSelectModule` | Dropdown filters (topic, stack, difficulty) |
| `mat-chip-listbox` / `mat-chip-option` | `MatChipsModule` | Chip-based filters and tags |
| `mat-table` | `MatTableModule` | Admin question bank list |
| `mat-paginator` | `MatPaginatorModule` | Pagination on the admin list |
| `mat-progress-bar` | `MatProgressBarModule` | Session progress indicator |
| `mat-progress-spinner` | `MatProgressSpinnerModule` | Loading state on browse page |
| `mat-icon` | `MatIconModule` | All icon buttons |
| `mat-button` / `mat-raised-button` / `mat-icon-button` | `MatButtonModule` | All buttons |
| `mat-input` | `MatInputModule` | Text inputs inside `mat-form-field` |

```typescript
// Correct — import only what this component uses
imports: [MatCardModule, MatButtonModule, MatIconModule]

// Wrong — importing everything
imports: [MaterialModule]
```

---

## 4. Service Rules

### HTTP calls belong in services

All HTTP calls go through a service class in `frontend/src/app/core/services/`. Components must never inject `HttpClient` directly.

Services must:
- Be decorated with `@Injectable({ providedIn: 'root' })`
- Inject `HttpClient` via `inject(HttpClient)` and store it as `private readonly http`
- Define a `private readonly base` field for the API root path (e.g. `'/api/bookmarks'`)
- Return `Observable<T>` from every method — never subscribe internally
- Use concrete TypeScript interfaces from `core/models/` as the generic type argument — never `Observable<any>`

```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bookmark, BookmarkNoteRequest } from '../models/bookmark.model';

@Injectable({ providedIn: 'root' })
export class BookmarkService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api/bookmarks';

  list(): Observable<Bookmark[]> {
    return this.http.get<Bookmark[]>(this.base);
  }

  updateNote(id: string, request: BookmarkNoteRequest): Observable<Bookmark> {
    return this.http.put<Bookmark>(`${this.base}/${id}/note`, request);
  }
}
```

### Models mirror backend DTOs

TypeScript interfaces in `core/models/` must match backend DTO field names exactly (camelCase). Do not invent frontend-only field aliases for data that comes from the API.

Each model file exports:
- A response interface (e.g. `Bookmark`, `Question`, `SessionResponse`)
- A request interface when the feature requires submitting data (e.g. `BookmarkRequest`, `BookmarkNoteRequest`)
- Enums that mirror Java enums, with string literal values matching the enum name (e.g. `CONFIDENT = 'CONFIDENT'`)

```typescript
// core/models/bookmark.model.ts
export interface Bookmark {
  id: string;
  question: Question;
  note: string | null;
  createdAt: string;
}

export interface BookmarkNoteRequest {
  note: string;
}
```

---

## 5. Routing

### Lazy loading with `loadComponent`

All feature routes use `loadComponent` with a dynamic `import()`. There are no eagerly loaded feature components in the route tree. Every route definition in `app.routes.ts` follows this pattern:

```typescript
{
  path: 'bookmarks',
  loadComponent: () =>
    import('./features/browse/bookmarks/bookmarks.component')
      .then((m) => m.BookmarksComponent)
}
```

Do not use `loadChildren` with a `Routes` array unless a feature genuinely needs a nested router outlet with its own child routes. For single-page features (one route, one component), always use `loadComponent`.

### Navigation in components

Use Angular's `Router` service via `inject(Router)` for programmatic navigation. Pass route paths as string arrays to `router.navigate()`:

```typescript
private readonly router = inject(Router);

goToSession(id: string): void {
  this.router.navigate(['/sessions', id]);
}
```

Do not hardcode full URL strings in `navigate()` calls. Use array segments.

### Route parameter access

Access route parameters via `inject(ActivatedRoute)`. Use `snapshot.paramMap.get('id')` for parameters that do not change while the component is alive. Use the observable `paramMap` only when the parameter can change without destroying the component.

```typescript
private readonly route = inject(ActivatedRoute);

ngOnInit(): void {
  const id = this.route.snapshot.paramMap.get('id')!;
}
```

---

## 6. State and Reactivity

### Signals for local component state

Use Angular signals (`signal()`, `computed()`) for local mutable state in new components. Signals work correctly with `ChangeDetectionStrategy.OnPush` without additional wiring.

```typescript
import { signal, computed } from '@angular/core';

export class BookmarksComponent {
  private readonly bookmarks = signal<Bookmark[]>([]);
  readonly loading = signal(false);
  readonly isEmpty = computed(() => this.bookmarks().length === 0);
}
```

### `toSignal()` for observable-backed state

Convert service observables to signals using `toSignal()` when the data should load once and remain available in the template. `toSignal()` subscribes automatically and cleans up when the component is destroyed — no manual unsubscription required.

```typescript
import { toSignal } from '@angular/core/rxjs-interop';

export class BookmarksComponent {
  private readonly bookmarkService = inject(BookmarkService);

  readonly bookmarks = toSignal(this.bookmarkService.list(), { initialValue: [] });
}
```

### Avoid manual subscribe/unsubscribe

Do not call `.subscribe()` in `ngOnInit()` or elsewhere in the component class unless the subscription must trigger a side effect (e.g. navigate after save). For read-only data rendering, use `toSignal()` or the `async` pipe. When a manual subscription is unavoidable, use `takeUntilDestroyed()` to prevent memory leaks:

```typescript
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

export class ActiveSessionComponent {
  private readonly destroyRef = inject(DestroyRef);

  ngOnInit(): void {
    this.sessionService.getById(id).pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(session => {
      // side effect required here (setting up form state)
    });
  }
}
```

### `async` pipe in templates

For observables that are not converted to signals, use the `async` pipe in the template. Import `AsyncPipe` in the component's `imports` array.

```html
<mat-card *ngFor="let bookmark of bookmarks$ | async">...</mat-card>
```

---

## 7. Naming Conventions

### Files

| Type | Pattern | Example |
|---|---|---|
| Component | `kebab-case.component.ts` | `bookmark-card.component.ts` |
| Service | `kebab-case.service.ts` | `bookmark.service.ts` |
| Model | `kebab-case.model.ts` | `bookmark.model.ts` |
| Route file | `routes.ts` (if added per feature) | `session/routes.ts` |

### Classes and interfaces

| Type | Pattern | Example |
|---|---|---|
| Component class | `PascalCase` + `Component` | `BookmarkCardComponent` |
| Service class | `PascalCase` + `Service` | `BookmarkService` |
| Response interface | `PascalCase` noun | `Bookmark`, `SessionResponse` |
| Request interface | `PascalCase` + `Request` | `BookmarkNoteRequest` |
| Enum | `PascalCase` | `Confidence`, `DifficultyLevel` |

### Component selectors

Use the `app-` prefix for all component selectors. Use kebab-case.

```typescript
selector: 'app-bookmark-card'
selector: 'app-session-summary'
```

---

## 8. What NOT To Do

- **Do not create `NgModule` files.** There are no modules in this project. Every component, directive, and pipe uses `standalone: true`.
- **Do not import `BrowserModule` or `CommonModule` in standalone components.** In Angular 17+, standalone components do not need these. Import `NgIf`, `NgFor`, and `AsyncPipe` individually from `@angular/common` only if you are not using the control flow syntax (`@if`, `@for`).
- **Do not inject `HttpClient` in components.** `HttpClient` belongs only in service classes inside `core/services/`.
- **Do not use `any` in TypeScript.** Every variable, parameter, return type, and generic type argument must have an explicit type. If the type is unknown, define an interface.
- **Do not put business logic in components.** Filtering, sorting, grouping, and data transformation belong in a service method or a computed signal, not in template event handlers or `ngOnInit`.
- **Do not manipulate the DOM directly.** Do not use `document.querySelector`, `document.getElementById`, or `ElementRef.nativeElement` to read or write DOM state. Use Angular template bindings, signals, and Angular CDK utilities instead.
- **Do not use template-driven forms.** Use `ReactiveFormsModule` with `FormGroup` and `FormControl` for every form that has validation or programmatic state.
- **Do not hardcode the backend URL in service methods.** All API calls use relative paths starting with `/api/`. The dev proxy in `proxy.conf.json` forwards them to `http://localhost:8080`.
- **Do not subscribe inside a service method and return the unwrapped value.** Services return `Observable<T>`. Subscribing inside a service hides errors, prevents retry logic, and makes testing harder.
- **Do not leave unused `import` statements.** Every entry in a component's `imports` array must correspond to a template element, directive, or pipe actually used in that component's template.
