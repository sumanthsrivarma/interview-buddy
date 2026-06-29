---
mode: agent
tools: [codebase]
description: Summarise what changed in the last Copilot session
---

# Session Summary

## Instructions

Read all files modified in the last git commit or uncommitted changes.

Then produce exactly this:

**What was built**
One line — feature name and the files created.

**Decisions the agent made**
Bullet list of any architectural or naming decisions made without
being explicitly asked — e.g. "chose to reuse existing BookmarkService
rather than create a new one".

**What was skipped**
Anything from the spec or prompt that was not implemented.

**What to verify**
Three things a human should manually check before raising a PR.

No code. No diffs. No line numbers. Plain English only.