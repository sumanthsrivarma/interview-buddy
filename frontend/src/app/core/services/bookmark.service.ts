import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bookmark, BookmarkNoteRequest, BookmarkRequest } from '../models/bookmark.model';

@Injectable({ providedIn: 'root' })
export class BookmarkService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api/bookmarks';

  list(): Observable<Bookmark[]> {
    return this.http.get<Bookmark[]>(this.base);
  }

  create(request: BookmarkRequest): Observable<Bookmark> {
    return this.http.post<Bookmark>(this.base, request);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  updateNote(id: string, request: BookmarkNoteRequest): Observable<Bookmark> {
    return this.http.put<Bookmark>(`${this.base}/${id}/note`, request);
  }
}
