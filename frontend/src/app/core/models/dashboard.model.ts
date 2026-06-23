export interface TopicBreakdown {
  topic: string;
  confident: number;
  needsWork: number;
  skipped: number;
}

export interface WeakArea {
  topic: string;
  needsWorkCount: number;
}

export interface WeeklyStreak {
  week: string;
  sessionCount: number;
}
