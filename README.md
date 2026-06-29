# AI Customization Demo

A reference workspace that demonstrates how to steer an AI coding agent (GitHub Copilot in VS Code) with layered, version-controlled customization files. The application code itself is incidental — it exists only to give the customization layers something real to act on.

Each layer below answers a different question for the agent: **who it is, what the rules are, how to do recurring jobs, and how to navigate the code.**

## What this repo demonstrates

### 1. `AGENTS.md` — primary agent context
The single source of truth the agent reads first. It defines the domain vocabulary, tech stack, repository structure, architectural constraints, and explicit anti-patterns. One well-maintained context file keeps every response grounded in the project's real conventions instead of generic defaults.

### 2. Instruction files — scoped, always-on rules
- [`.github/copilot-instructions.md`](.github/copilot-instructions.md) — workspace-wide rules applied to every request.
- [`.github/instructions/*.instructions.md`](.github/instructions/) — path-scoped rules that auto-apply via an `applyTo` glob (e.g. backend rules only attach to `backend/src/**/*.java`).

Instructions are passive: the agent follows them automatically without being asked. They are how you encode "never do X / always do Y" so the rules survive across sessions and contributors.

### 3. Skill files — packaged domain procedures
[`.github/skills/*/SKILL.md`](.github/skills/) bundle multi-step expertise the agent loads on demand (e.g. the OpenSpec workflow skills and a Spring REST scaffolding skill). A skill turns a complex, repeatable task into a named capability the agent can invoke when the work matches its description.

### 4. Reusable prompt files — one-command workflows
[`.github/prompts/*.prompt.md`](.github/prompts/) are parameterized prompts you trigger by name (e.g. `/opsx:propose`, `/opsx:apply`, `/opsx:archive`). They standardize recurring operations so anyone gets the same high-quality result without retyping detailed instructions.

### 5. OpenSpec — spec-driven development (SDD)
[`openspec/`](openspec/) holds a structured change workflow: a proposal, design, specs, and tasks are generated **before** code, then implemented and archived. This keeps intent, requirements, and implementation traceable. Per-artifact rules live in [`openspec/config.yaml`](openspec/config.yaml); completed work is preserved under `openspec/changes/archive/`.

### 6. Graphify — a queryable knowledge graph
[`graphify-out/`](graphify-out/) contains a pre-built knowledge graph of the codebase (nodes, edges, communities, and an audit report). The agent uses it to navigate structure and trace cross-layer relationships **before** editing, rather than reading files blindly.

```bash
graphify query "how does the bookmark feature flow from UI to database"   # broad context (BFS)
graphify query "what depends on QuestionResponse" --dfs                    # trace one dependency path (DFS)
graphify path "BookmarksComponent" "Bookmark"                              # shortest path between concepts
graphify explain "MockSession"                                             # plain-language node summary
```

If the CLI is unavailable, read [`graphify-out/GRAPH_REPORT.md`](graphify-out/GRAPH_REPORT.md) for god nodes and community hubs. Graphify usage is wired into [`AGENTS.md`](AGENTS.md) (section 7) and [`.github/copilot-instructions.md`](.github/copilot-instructions.md) so the agent reaches for it by default.

## How the layers fit together

```
copilot-instructions.md   → always-on workspace rules
AGENTS.md                 → project context + navigation policy
instructions/*.instructions.md → path-scoped rules (auto-applied)
skills/*/SKILL.md         → on-demand procedures
prompts/*.prompt.md       → named, repeatable workflows
openspec/                 → spec-before-code change process
graphify-out/             → knowledge graph for code navigation
```

Together they show a practical pattern: **context and rules are passive and always present; skills, prompts, SDD, and the graph are active tools the agent pulls in when the task calls for them.**
