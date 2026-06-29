import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormControl } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Bookmark } from '../../../core/models/bookmark.model';
import { BookmarkService } from '../../../core/services/bookmark.service';

@Component({
  selector: 'app-bookmarks',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatChipsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
  ],
  templateUrl: './bookmarks.component.html',
  styleUrl: './bookmarks.component.scss'
})
export class BookmarksComponent implements OnInit {
  private readonly bookmarkService = inject(BookmarkService);
  private readonly snackBar = inject(MatSnackBar);

  bookmarks: Bookmark[] = [];
  editingId: string | null = null;
  editNoteControl = new FormControl<string>('');

  ngOnInit(): void {
    this.loadBookmarks();
  }

  private loadBookmarks(): void {
    this.bookmarkService.list().subscribe({
      next: (data) => { this.bookmarks = data; },
    });
  }

  startEdit(bookmark: Bookmark): void {
    this.editingId = bookmark.id;
    this.editNoteControl.setValue(bookmark.note ?? '');
  }

  cancelEdit(): void {
    this.editingId = null;
    this.editNoteControl.setValue('');
  }

  saveNote(bookmark: Bookmark): void {
    const note = this.editNoteControl.value ?? '';
    this.bookmarkService.updateNote(bookmark.id, { note }).subscribe({
      next: (updated) => {
        const idx = this.bookmarks.findIndex(b => b.id === updated.id);
        if (idx !== -1) {
          this.bookmarks[idx] = updated;
        }
        this.editingId = null;
      },
    });
  }

  deleteBookmark(id: string): void {
    this.bookmarkService.delete(id).subscribe({
      next: () => {
        this.bookmarks = this.bookmarks.filter(b => b.id !== id);
      },
      error: () => {
        this.snackBar.open('Failed to delete bookmark. Please try again.', 'Dismiss', { duration: 4000 });
      },
    });
  }
}
