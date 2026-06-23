export enum Topic {
  JAVA_CORE = 'JAVA_CORE',
  COLLECTIONS = 'COLLECTIONS',
  CONCURRENCY = 'CONCURRENCY',
  JVM_INTERNALS = 'JVM_INTERNALS',
  SPRING_BOOT = 'SPRING_BOOT',
  SPRING_SECURITY = 'SPRING_SECURITY',
  HIBERNATE_JPA = 'HIBERNATE_JPA',
  MICROSERVICES = 'MICROSERVICES',
  DESIGN_PATTERNS = 'DESIGN_PATTERNS',
  SOLID = 'SOLID',
  TDD = 'TDD',
  SYSTEM_DESIGN = 'SYSTEM_DESIGN',
  BEHAVIOURAL = 'BEHAVIOURAL'
}

export enum TechStack {
  JAVA = 'JAVA',
  SPRING = 'SPRING',
  ANGULAR = 'ANGULAR',
  REACT = 'REACT',
  POSTGRES = 'POSTGRES',
  KAFKA = 'KAFKA',
  DOCKER = 'DOCKER',
  KUBERNETES = 'KUBERNETES',
  AWS = 'AWS',
  GENERAL = 'GENERAL'
}

export enum DifficultyLevel {
  EASY = 'EASY',
  MEDIUM = 'MEDIUM',
  HARD = 'HARD'
}

export interface Question {
  id: string;
  title: string;
  body: string;
  answer: string | null;
  followUpProbes: string | null;
  topic: Topic;
  techStack: TechStack;
  difficultyLevel: DifficultyLevel;
  experienceRangeMin: number | null;
  experienceRangeMax: number | null;
  tags: string[];
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface QuestionRequest {
  title: string;
  body: string;
  answer?: string;
  followUpProbes?: string;
  topic: Topic;
  techStack: TechStack;
  difficultyLevel: DifficultyLevel;
  experienceRangeMin?: number | null;
  experienceRangeMax?: number | null;
  tags?: string[];
}

export interface QuestionFilter {
  topic?: Topic;
  techStack?: TechStack;
  difficultyLevel?: DifficultyLevel;
  includeInactive?: boolean;
  page?: number;
  size?: number;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
