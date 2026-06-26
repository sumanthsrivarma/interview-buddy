---
applyTo: "frontend/src/**/*.ts"
---

# Angular Frontend Instructions — InterviewBuddy

## 1. Components

- **Standalone only** — every component, directive, and pipe uses `standalone: true`; no `NgModule` files ever
- **`ChangeDetectionStrategy.OnPush`** on every new component — update state by reassigning references, not mutating in place
- **`inject()` for DI** — no constructor parameters; declare injected dependencies as `private readonly` fields at the top of the class
- **Typed `@Input()`/`@Output()`** — explicit types on all inputs; `EventEmitter<T>` with a concrete type, never `EventEmitter<any>`
- **No business logic** — no filtering, sorting, or data transformation in components; delegate everything to services

---

## 2. Angular Material

Always use Material components. Never use raw `<input>`, `<button>`, `<select>`, `<table>`, or `<form>` where a Material equivalent exists. Import each module individually in the component's `imports` array — no barrel `MaterialModule`.

| Component | Module |
|---|---|
| `mat-toolbar` | `MatToolbarModule` |
| `mat-sidenav` / `mat-sidenav-container` | `MatSidenavModule` |
| `mat-nav-list` | `MatListModule` |
| `mat-card` | `MatCardModule` |
| `mat-form-field` | `MatFormFieldModule` |
| `mat-input` | `MatInputModule` |
| `mat-select` | `MatSelectModule` |
| `mat-chip-listbox` / `mat-chip-option` | `MatChipsModule` |
| `mat-table` | `MatTableModule` |
| `mat-paginator` | `MatPaginatorModule` |
| `mat-progress-bar` | `MatProgressBarModule` |
| `mat-progress-spinner` | `MatProgressSpinnerModule` |
| `mat-icon` | `MatIconModule` |
| `mat-button` / `mat-raised-button` / `mat-icon-button` | `MatButtonModule` |

---

## 3. Services

- `@Injectable({ providedIn: 'root' })` — inject `HttpClient` via `inject(HttpClient)` as `private readonly http`
- Define `private readonly base = '/api/...'` for the root path; never hardcode backend URLs in method bodies
- Return `Observable<T>` from every method — never subscribe internally; use concrete model types, never `Observable<any>`
- Models in `core/models/` mirror backend DTO field names exactly (camelCase); each file exports a response interface, a request interface (if needed), and enums with string literal values matching the Java enum name

---

## 4. Routing

- All routes use `loadComponent` with `import()` — no eagerly loaded feature components
- Programmatic navigation: `inject(Router)` with `router.navigate(['/path', id])` — array segments, no hardcoded URL strings
- Route params: `inject(ActivatedRoute).snapshot.paramMap.get('id')` for stable params; observable `paramMap` only when the param changes without destroying the component

---

## 5. State and Reactivity

- **Signals** (`signal()`, `computed()`) for local mutable state — works with `OnPush` without extra wiring
- **`toSignal()`** to convert service observables to signals for read-only template data — auto-subscribes and cleans up on destroy
- **Avoid manual subscribe** — use `toSignal()` or `async` pipe for data rendering; if `.subscribe()` is unavoidable for a side effect, pipe through `takeUntilDestroyed(this.destroyRef)`

---

## 6. Naming Conventions

| Type | Pattern | Example |
|---|---|---|
| Component file | `kebab-case.component.ts` | `bookmark-card.component.ts` |
| Service file | `kebab-case.service.ts` | `bookmark.service.ts` |
| Model file | `kebab-case.model.ts` | `bookmark.model.ts` |
| Component class | `PascalCase` + `Component` | `BookmarkCardComponent` |
| Service class | `PascalCase` + `Service` | `BookmarkService` |
| Response interface | `PascalCase` noun | `Bookmark`, `SessionResponse` |
| Request interface | `PascalCase` + `Request` | `BookmarkNoteRequest` |
| Selector | `app-` prefix, kebab-case | `app-bookmark-card` |

---

## 7. What NOT To Do

- No `NgModule` files — standalone only
- No `BrowserModule` or `CommonModule` in standalone components — use `@if`/`@for` control flow syntax; import `NgIf`/`NgFor` individually only if needed
- No `HttpClient` in components — services only
- No `any` in TypeScript — explicit types everywhere
- No business logic in components — use computed signals or service methods
- No direct DOM manipulation (`document.querySelector`, `ElementRef.nativeElement`) — use template bindings and Angular CDK
- No template-driven forms — `ReactiveFormsModule` with `FormGroup`/`FormControl` for all validated forms
- No hardcoded backend URLs — `/api/...` relative paths only
- No subscribing inside a service and returning unwrapped values — return `Observable<T>`
- No inline styles — component styles go in `.scss`; global styles in `src/styles.scss`
- No unused entries in a component's `imports` array
