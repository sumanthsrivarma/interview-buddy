# Graph Report - .  (2026-06-27)

## Corpus Check
- Corpus is ~46,841 words - fits in a single context window. You may not need a graph.

## Summary
- 563 nodes · 772 edges · 61 communities (26 shown, 35 thin omitted)
- Extraction: 89% EXTRACTED · 11% INFERRED · 0% AMBIGUOUS · INFERRED: 88 edges (avg confidence: 0.84)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Backend Core Services|Backend Core Services]]
- [[_COMMUNITY_Session UI Components|Session UI Components]]
- [[_COMMUNITY_Frontend Session Feature|Frontend Session Feature]]
- [[_COMMUNITY_API Endpoints & Features|API Endpoints & Features]]
- [[_COMMUNITY_Question Domain Entity|Question Domain Entity]]
- [[_COMMUNITY_Frontend Question BrowseAdmin|Frontend Question Browse/Admin]]
- [[_COMMUNITY_Question Response DTOs|Question Response DTOs]]
- [[_COMMUNITY_Session Attempt Domain|Session Attempt Domain]]
- [[_COMMUNITY_Mock Session Domain|Mock Session Domain]]
- [[_COMMUNITY_OpenSpec Change Workflow|OpenSpec Change Workflow]]
- [[_COMMUNITY_Session Response DTOs|Session Response DTOs]]
- [[_COMMUNITY_Dashboard & Progress Charts|Dashboard & Progress Charts]]
- [[_COMMUNITY_Session Request DTOs|Session Request DTOs]]
- [[_COMMUNITY_Session Detail DTOs|Session Detail DTOs]]
- [[_COMMUNITY_Mock Session Service Layer|Mock Session Service Layer]]
- [[_COMMUNITY_Topic Breakdown Analytics|Topic Breakdown Analytics]]
- [[_COMMUNITY_Active Session UI|Active Session UI]]
- [[_COMMUNITY_Bookmark Feature|Bookmark Feature]]
- [[_COMMUNITY_Session REST Controller|Session REST Controller]]
- [[_COMMUNITY_Attempt Repository Layer|Attempt Repository Layer]]
- [[_COMMUNITY_App Bootstrap & Routing|App Bootstrap & Routing]]
- [[_COMMUNITY_Question Admin List|Question Admin List]]
- [[_COMMUNITY_Weekly Streak Metrics|Weekly Streak Metrics]]
- [[_COMMUNITY_OpenSpec Artifact Types|OpenSpec Artifact Types]]
- [[_COMMUNITY_Backend Arch Constraints|Backend Arch Constraints]]
- [[_COMMUNITY_Weak Area Analytics|Weak Area Analytics]]
- [[_COMMUNITY_Question Browse UI|Question Browse UI]]
- [[_COMMUNITY_Angular Frontend Rules|Angular Frontend Rules]]
- [[_COMMUNITY_Session History & Streaks|Session History & Streaks]]
- [[_COMMUNITY_Spring Security Config|Spring Security Config]]
- [[_COMMUNITY_Database & Migrations|Database & Migrations]]
- [[_COMMUNITY_Application Tests|Application Tests]]
- [[_COMMUNITY_Spring Boot Entry Point|Spring Boot Entry Point]]
- [[_COMMUNITY_Prod Environment Config|Prod Environment Config]]
- [[_COMMUNITY_Bookmarks Component|Bookmarks Component]]
- [[_COMMUNITY_Dev Environment Config|Dev Environment Config]]
- [[_COMMUNITY_App Identity|App Identity]]
- [[_COMMUNITY_SQL Seed Data|SQL Seed Data]]
- [[_COMMUNITY_Bookmarks TS Component|Bookmarks TS Component]]
- [[_COMMUNITY_Material UI Pattern|Material UI Pattern]]
- [[_COMMUNITY_Security Config Class|Security Config Class]]
- [[_COMMUNITY_Session Request DTO|Session Request DTO]]
- [[_COMMUNITY_Attempt Request DTO|Attempt Request DTO]]
- [[_COMMUNITY_Requirements Document|Requirements Document]]
- [[_COMMUNITY_H2 Test Database|H2 Test Database]]
- [[_COMMUNITY_Bookmarks UI|Bookmarks UI]]
- [[_COMMUNITY_Summarise Changes Prompt|Summarise Changes Prompt]]
- [[_COMMUNITY_CLI Integration Concept|CLI Integration Concept]]

## God Nodes (most connected - your core abstractions)
1. `Question` - 31 edges
2. `QuestionResponse` - 30 edges
3. `SessionResponse` - 22 edges
4. `MockSession` - 22 edges
5. `AttemptResponse` - 14 edges
6. `SessionAttempt` - 14 edges
7. `Question Entity` - 14 edges
8. `SessionRequest` - 13 edges
9. `SessionDetailResponse` - 13 edges
10. `SessionService` - 12 edges

## Surprising Connections (you probably didn't know these)
- `QuestionDetailComponent` --shows_details_of--> `Question Entity`  [INFERRED]
  frontend/src/app/features/browse/question-detail/question-detail.component.html → backend/src/main/resources/db/migration/V1__init_schema.sql
- `DashboardComponent` --displays_breakdown--> `Confidence`  [INFERRED]
  frontend/src/app/features/dashboard/dashboard/dashboard.component.html → backend/src/main/java/com/interviewbuddy/dto/AttemptRequest.java
- `ActiveSessionComponent` --records--> `Confidence`  [INFERRED]
  frontend/src/app/features/session/active-session/active-session.component.html → backend/src/main/java/com/interviewbuddy/dto/AttemptRequest.java
- `Question Entity` --references--> `TechStack Enum`  [EXTRACTED]
  backend/src/main/resources/db/migration/V1__init_schema.sql → agents.md
- `Question Entity` --references--> `DifficultyLevel Enum`  [EXTRACTED]
  backend/src/main/resources/db/migration/V1__init_schema.sql → agents.md

## Hyperedges (group relationships)
- **Question Management Flow** — admin_feature, ts_question_service, ts_question_model, entity_question [INFERRED 0.90]
- **Session Lifecycle Flow** — session_feature, ts_session_service, ts_session_model, enum_confidence [INFERRED 0.90]
- **Dashboard Analytics Flow** — dashboard_feature, ts_dashboard_service, ts_dashboard_model, ts_session_model [INFERRED 0.85]
- **DTO Data Transfer Layer** — question_response, attempt_response, session_response, session_detail_response, attempt_request, session_request, weak_area_dto, topic_breakdown_dto, weekly_streak_dto [EXTRACTED 1.00]
- **Repository Access Layer** — question_repository, session_attempt_repository, mock_session_repository [EXTRACTED 1.00]
- **Session Feature Flow** — active_session_component, session_review_component, session_service, session_detail_response, attempt_response [INFERRED 0.80]
- **Persistence Domain Model** — question_entity, mock_session_entity, session_attempt_entity, bookmark_entity [EXTRACTED 1.00]
- **REST Controller Layer** — QuestionController, DashboardController, MockSessionController [INFERRED 0.95]
- **Business Service Layer** — QuestionService, DashboardService, MockSessionService [INFERRED 0.95]
- **Domain Entity Layer** — Question, MockSession, SessionAttempt [INFERRED 0.95]
- **Enumeration Constants** — Topic, TechStack, DifficultyLevel, SessionStatus, Confidence [INFERRED 0.90]
- **Data Transfer Object Layer** — QuestionResponse, TopicBreakdownDto, WeakAreaDto, WeeklyStreakDto, SessionResponse, SessionDetailResponse, AttemptResponse, SessionRequest, AttemptRequest [INFERRED 0.90]
- **3-Layer Clean Architecture Pattern** — rest_controller_layer, business_service_layer, domain_entity_layer [EXTRACTED 0.95]
- **Frontend Features** — admin_feature, browse_feature, session_feature, dashboard_feature [INFERRED 0.90]
- **Core Domain Entities** — question_entity, mocksession_entity, sessionattempt_entity, bookmark_entity [EXTRACTED 1.00]
- **Core Domain Enums** — topic_enum, techstack_enum, difficultylevel_enum, confidence_enum, sessionstatus_enum [EXTRACTED 1.00]
- **Backend Technology Stack** — springboot_framework, postgresql_db, flyway_migration, spring_security [EXTRACTED 1.00]
- **Frontend Technology Stack** — angular_framework, materialsdk_ui, standalone_components, materialsdk_constraint [EXTRACTED 1.00]
- **Admin Feature UI Components** — questionlist_component, questionform_component [INFERRED 0.90]
- **Session Feature UI Components** — sessionconfig_component, activesession_component, sessionhistory_component, sessionreview_component, sessionsummary_component [INFERRED 0.90]
- **Three-Layer Architecture** — backend_layer, frontend_layer, database_layer [EXTRACTED 1.00]
- **Linear OpenSpec Workflow** — skill_openspec_explore, skill_openspec_new_change, skill_openspec_continue_change, skill_openspec_apply_change, skill_openspec_verify_change, skill_openspec_archive_change [EXTRACTED 0.95]
- **Fast-Track OpenSpec Workflows** — skill_openspec_propose, skill_openspec_ff_change, skill_openspec_apply_change [INFERRED 0.90]
- **Spec-Driven Artifact Sequence** — artifact_proposal, artifact_specs, artifact_design, artifact_tasks [EXTRACTED 1.00]
- **Archive and Finalization Operations** — skill_openspec_verify_change, skill_openspec_sync_specs, skill_openspec_archive_change, skill_openspec_bulk_archive [EXTRACTED 0.95]
- **OpenSpec Prompt Ecosystem** — prompt_opsx_new, prompt_opsx_apply, prompt_opsx_explore, prompt_opsx_propose, prompt_opsx_sync, prompt_opsx_archive, prompt_opsx_bulk_archive, prompt_opsx_onboard [EXTRACTED 1.00]

## Communities (61 total, 35 thin omitted)

### Community 0 - "Backend Core Services"
Cohesion: 0.06
Nodes (26): AttemptResponse (DTO), Confidence (Enum), DashboardController, DashboardService, DifficultyLevel (Enum), MockSession, MockSessionController, MockSessionService (+18 more)

### Community 1 - "Session UI Components"
Cohesion: 0.07
Nodes (42): ActiveSessionComponent, ActiveSessionComponent, AppComponent, AttemptRequest, AttemptResponse, Bookmark Entity, BrowseComponent, Confidence (+34 more)

### Community 2 - "Frontend Session Feature"
Cohesion: 0.09
Nodes (11): AttemptRequest, AttemptResponse, Confidence, SessionDetailResponse, SessionRequest, SessionResponse, SessionStatus, SessionService (+3 more)

### Community 3 - "API Endpoints & Features"
Cohesion: 0.08
Nodes (36): Admin Feature - Question Management, /api/bookmarks Endpoint, /api/dashboard Endpoint, /api/questions Endpoint, /api/sessions Endpoint, Browse Feature - Question Exploration, Question Entity (Seeded), Confidence Enum (CONFIDENT, NEEDS_WORK, SKIPPED) (+28 more)

### Community 5 - "Frontend Question Browse/Admin"
Cohesion: 0.13
Nodes (11): DifficultyLevel, Page, Question, QuestionFilter, QuestionRequest, TechStack, Topic, QuestionDetailComponent (+3 more)

### Community 9 - "OpenSpec Change Workflow"
Cohesion: 0.10
Nodes (23): Artifact Dependencies and Sequencing, OpenSpec Change Lifecycle, Delta Specifications Pattern, Apply OpenSpec Change Prompt, Archive OpenSpec Change Prompt, Bulk Archive OpenSpec Changes Prompt, Explore OpenSpec Change Prompt, Start New OpenSpec Change Prompt (+15 more)

### Community 11 - "Dashboard & Progress Charts"
Cohesion: 0.23
Nodes (5): DashboardComponent, TopicBreakdown, WeakArea, WeeklyStreak, DashboardService

### Community 17 - "Bookmark Feature"
Cohesion: 0.27
Nodes (4): Bookmark, BookmarkNoteRequest, BookmarkRequest, BookmarkService

### Community 20 - "App Bootstrap & Routing"
Cohesion: 0.43
Nodes (3): AppComponent, appConfig, routes

### Community 23 - "OpenSpec Artifact Types"
Cohesion: 0.53
Nodes (6): Design Artifact Type, Proposal Artifact Type, Specifications Artifact Type, Tasks Artifact Type, OpenSpec Configuration, Spec-Driven Schema Workflow

### Community 24 - "Backend Arch Constraints"
Cohesion: 0.33
Nodes (6): Backend Layer, Constructor Injection Pattern, CORS Configuration, Layering Architecture, Spring Security, Spring Boot 3.x

### Community 27 - "Angular Frontend Rules"
Cohesion: 0.40
Nodes (5): Angular 20, Frontend Layer, Angular Material Requirement, Angular Material, Standalone Components Constraint

### Community 30 - "Database & Migrations"
Cohesion: 0.50
Nodes (4): Database Layer, Flyway Migration Immutability, Flyway, PostgreSQL 16

## Knowledge Gaps
- **47 isolated node(s):** `SessionStatus`, `BookmarksComponent`, `environment`, `environment`, `QuestionRepository` (+42 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **35 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `SessionAttempt` connect `Backend Core Services` to `Attempt Repository Layer`?**
  _High betweenness centrality (0.035) - this node is a cross-community bridge._
- **Why does `QuestionResponse` connect `Question Response DTOs` to `Backend Core Services`?**
  _High betweenness centrality (0.035) - this node is a cross-community bridge._
- **What connects `SessionStatus`, `BookmarksComponent`, `environment` to the rest of the system?**
  _58 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Backend Core Services` be split into smaller, more focused modules?**
  _Cohesion score 0.06262626262626263 - nodes in this community are weakly interconnected._
- **Should `Session UI Components` be split into smaller, more focused modules?**
  _Cohesion score 0.0708245243128964 - nodes in this community are weakly interconnected._
- **Should `Frontend Session Feature` be split into smaller, more focused modules?**
  _Cohesion score 0.08858858858858859 - nodes in this community are weakly interconnected._
- **Should `API Endpoints & Features` be split into smaller, more focused modules?**
  _Cohesion score 0.08095238095238096 - nodes in this community are weakly interconnected._