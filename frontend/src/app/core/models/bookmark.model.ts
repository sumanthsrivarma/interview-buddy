import { DifficultyLevel, Topic } from './question.model';

export interface Bookmark {
  id: string;
  questionId: string;
  questionText: string;
  topic: Topic;
  difficultyLevel: DifficultyLevel;
  note: string | null;
  createdAt: string;
}

export interface BookmarkRequest {
  questionId: string;
}

export interface BookmarkNoteRequest {
  note: string;
}
