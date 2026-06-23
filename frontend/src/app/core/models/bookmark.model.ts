import { Question } from './question.model';

export interface Bookmark {
  id: string;
  question: Question;
  note: string | null;
  createdAt: string;
}

export interface BookmarkRequest {
  questionId: string;
}

export interface BookmarkNoteRequest {
  note: string;
}
