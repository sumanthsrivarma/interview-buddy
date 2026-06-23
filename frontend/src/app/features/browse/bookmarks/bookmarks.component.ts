import { Component, OnInit, inject } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatExpansionModule } from '@angular/material/expansion';

import { BookmarkService } from '../../../core/services/bookmark.service';
import { Bookmark } from '../../../core/models/bookmark.model';

@Component({
  selector: 'app-bookmarks',
  standalone: true,
  imports: [ReactiveFormsModule, MatCardModule, MatButtonModule, MatIconModule,
            MatInputModule, MatFormFieldModule, MatExpansionModule],
  templateUrl: './bookmarks.component.html',
  styleUrl: './bookmarks.component.scss'
})
export class BookmarksComponent implements OnInit {
  private readonly bookmarkService = inject(BookmarkService);

  bookmarks: Bookmark[] = [];
  editingId: string | null = null;
  noteControl = new FormControl('');

  ngOnInit(): void { this.load(); }

  load(): void {
    this.bookmarkService.list().subscribe(b => this.bookmarks = b);
  }

  remove(id: string): void {
    this.bookmarkService.delete(id).subscribe(() => {
      this.bookmarks = this.bookmarks.filter(b => b.id !== id);
    });
  }

  startEdit(b: Bookmark): void {
    this.editingId = b.id;
    this.noteControl.setValue(b.note ?? '');
  }

  saveNote(id: string): void {
    this.bookmarkService.updateNote(id, { note: this.noteControl.value ?? '' }).subscribe(updated => {
      const idx = this.bookmarks.findIndex(b => b.id === id);
      if (idx > -1) this.bookmarks[idx] = updated;
      this.editingId = null;
    });
  }

  cancelEdit(): void { this.editingId = null; }
}
