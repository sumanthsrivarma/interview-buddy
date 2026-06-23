export enum SessionStatus {
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED'
}

export enum Confidence {
  CONFIDENT = 'CONFIDENT',
  NEEDS_WORK = 'NEEDS_WORK',
  SKIPPED = 'SKIPPED'
}

export interface SessionRequest {
  name: string;
  topic?: string;
  techStack?: string;
  difficultyLevel?: string;
  experienceRange?: number | null;
  questionCount: number;
}

export interface SessionResponse {
  id: string;
  name: string;
  topic: string | null;
  techStack: string | null;
  difficultyLevel: string | null;
  experienceRange: number | null;
  questionCount: number;
  status: SessionStatus;
  startedAt: string;
  completedAt: string | null;
}

export interface AttemptResponse {
  id: string;
  sessionId: string;
  question: import('./question.model').Question;
  confidence: Confidence | null;
  personalNote: string | null;
  attemptedAt: string;
}

export interface SessionDetailResponse extends SessionResponse {
  attempts: AttemptResponse[];
  confidentCount: number;
  needsWorkCount: number;
  skippedCount: number;
  notRatedCount: number;
}

export interface AttemptRequest {
  confidence: Confidence | null;
  personalNote?: string;
}
