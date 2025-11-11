SUCCESSMETER V2 — MASTER BLUEPRINT
0) Product North Star & Operating Truths
North Star
Help a person spend more minutes meaningfully—and feel that alignment—by turning 10/15/30-minute blocks into a compounding journey of aims → goals → actions → reflection; now supercharged with a purpose-aligned social layer that motivates and educates rather than distracts.
Three pillars (unchanged in spirit):
1.	Awareness through structure — deliberate logging in micro-intervals (10/15/30) enhances self-observation.
2.	Alignment over activity — success is measured by how time maps to what matters (Eisenhower matrix; A/B/C/D/E).
3.	Purpose through reflection — mission, quotes, and daily/weekly reviews ensure meaning stays central.
V2 Expansion: A goal-aware Feed, Profiles, and Followers/Following, all governed by the same alignment ethos: the algorithm optimizes for who you’re becoming, not for raw attention.
________________________________________
1) System Map (V2, End-to-End)
1.1 Primary Screens (now 8)
1.	Daily Intervals — grid of 10/15/30-min cells; “Log Now” snaps to active cell; stored in minutes for analytics fidelity. ABCDE tagging at log time.
2.	Tasks — duration classes: under 2m, 2–10m, >10m; link tasks → Goals; supports realistic planning & flow.
3.	Goals — A–E domains mapped to the Eisenhower matrix; goals/sub-tasks placed into quadrants for strategic awareness.
4.	Graphs & Analytics — daily/weekly/monthly; Important vs Not, Urgent vs Not, streaks; “time leaks” insights.
5.	Mission & Quotes — personal “why,” daily quote, end-day reflection; thematic quotes possible by Aim.
6.	Chief Aims Tree — visual hierarchy: 10-year Aim → 5y/2y/1y → 6m/1m → weekly/daily → intervals, with roll-ups and health color; ABCDE acts as the “pruning tool.”
7.	Feed (NEW) — Motivation/Education/Both; Reels/Pics/Both; ranking by Chief Aims, active goals, recent intervals, and quality signals (saves/finishes).
8.	Profiles (NEW) — public pages: posts grid, bio, Aim chips, current goal spotlight (opt-in), followers/following; follow graph shapes content discovery.
1.2 Support Engines (carried forward)
•	Flexible Interval Engine: user chooses 10 / 15 / 30; UI re-renders 6/4/2 slots per hour; data truth = minutes; historical data remains consistent when switching.
•	ABCDE Decision Layer: A(Do First) / B(Do Next) / C(Do Later) / D(Delegate) / E(Eliminate) — stays attached to intervals & goals; used in analytics, reflection, and optional profile badges.
•	Notifications: weekly review, pace alerts, neglect warnings, streaks, milestone celebrations, quiet hours; V2 extends with feed-aware nudges (“Daily 3 ready”).
•	Per-Goal Analytics & Health Score: totals, pace vs plan, consistency, recency, focus quality; labels: On Track / At Risk / Off Track.
________________________________________
2) Taxonomy & Ontology (Authoritative Dictionary)
2.1 Chief Aim Archetypes (examples; configurable)
•	Entrepreneurship / Business Building
•	Writing / Publishing / Creative Production
•	Fitness / Health / Well-being
•	Learning / Scholarship / Skill Mastery
•	Career Acceleration / Leadership
•	Design / Product / Engineering
•	Finance / Wealth / Operations
•	Relationships / Self-Care / Balance
(Used for alignment chips & personalization; never forces disclosure of private text.)
2.2 Goal Time Horizons (consistent with Tree)
Chief Aim (10y) → 5y → 2y → 1y → 6m → 1m → Weekly → Daily Goals → 10–30-minute Intervals.
2.3 Eisenhower / ABCDE Mapping
•	A → Important & (often) Urgent; B → Important, Not Urgent; C → lower leverage; D → delegate; E → eliminate. Visual & analytic integration across screens.
2.4 Feed Tags
•	Mode: Motivation / Education / Both
•	Aim Tags: one or more Chief Aim archetypes
•	Topic Tags (Education): UX, Copywriting, Strength, HIIT, Habit Formation, Focus, Kotlin/Android, Marketing, Finance Basics, Public Speaking, etc.
•	Emotion Tags (Motivation): Courage, Consistency, Discipline, Calm, Resilience, Momentum
(Tag dictionary maintained as a first-class artifact; creators must choose at least Mode + ≥1 thematic tag.)
________________________________________
3) Data Model (Conceptual Schema & Constraints)
User
•	id, displayName, avatar, bio, location?
•	chiefAimIds[] (declared themes), interestTags[]
•	privacy: showAimChips?, showGoalSpotlight?, showRhythmBadge?
•	settings: feedPurpose (Motivation|Education|Both), mediaPref (Reels|Pics|Both)
•	followersCount, followingCount, timestamps
FollowEdge
•	followerId, followedId, createdAt, uniqueness constraint
Post
•	id, authorId, type (reel|image|carousel), modeTag (Motivation|Education|Both)
•	aimTags[], topicTags[]
•	goalMapping? (author-side optional: {aimId, goalNodeId, visibility})
•	caption, mediaRefs, createdAt
•	moderation: status (ok|review|removed), flags[], isSpamLikely?
Engagement
•	like, save, finish (≥80% watched), share, comment? (V2.1), each with userId, postId, timestamps
IntervalSnapshot (V1 contract; do not break)
•	minutes, decisionAtLog (A/B/C/D/E), goalNodeId, chiefAimId, timestamp → analytics truth layer across features.
GoalNode
•	hierarchical reference, plannedHours?, dueDate?, healthScore, labels
Quote
•	curated text, themeTags[] (mapped to Aims), daily selection logic
________________________________________
4) Personalization Brain (Ranking & Surfaces)
4.1 The Objective Function
Maximize aligned progress, not watch time. Concretely, rank to increase:
•	(a) Educational saves that lead to a logged session within 24h
•	(b) Motivational finishes that precede a logged session within 2h
•	(c) Diversity across the user’s Aims/Topics to prevent monotony
4.2 Signals & Weights (Deterministic v1)
1.	Aim Match: declared (chiefAimIds) + inferred (from intervals last 7d).
2.	Active Goal Proximity: match post topicTags to the current 1m/6m goals; heavier boost if due date is near.
3.	Recent Intervals: if user logged e.g., “Marketing,” elevate education posts on marketing within 72h window.
4.	Engagement Quality: saves and finishes weighted higher than likes; author reputation (save/finish ratio).
5.	Recency/Diversity: freshness + author rotation + topic coverage within session.
6.	Social Graph: “followed” creators boosted only when aligned; otherwise neutral (prevents noise).
4.3 Surfaces
•	Main Feed: infinite scroll but with soft session guardrails (see §10).
•	Daily 3: three spotlight items computed from last 48–72h behavior + active goals; at least one Education card when feedPurpose ∈ {Education, Both}.
•	Explore Paths: discover by Aim (“Writers”, “Fitness Builders”) and by Goal templates (“Finish Draft”, “Lose 5kg”, “Ship MVP”).
4.4 Cold-Start Strategy
•	Onboarding collects Aims + top interests; seed feed from curated library mapped to common Aims/topics.
•	“Starter Cohorts”: opt-in lists like “30-Day Writing Sprint,” prepopulating a follow graph to overcome emptiness.
*(All ranking respects V1’s ethos that alignment > activity.)
________________________________________
5) Feed Creation & Moderation
5.1 Creator Flow (V2.0)
•	Upload media → choose Mode (Motivation/Education/Both) → Aim tags (≤3) → Topic tags (≤5) → optional Goal mapping (author’s own) → caption.
•	Preview shows chips that end users will see (transparent taxonomy helps quality).
5.2 Quality & Anti-Spam
•	Required tagging (Mode + ≥1 Aim/Topic); missing → downrank/hard stop.
•	Low-value detection: bait phrases, excessive hashtags, rapid multi-post spamming → throttle.
•	Community reports: triage queue; repeated policy hits → shadow downrank → suspension.
5.3 Civility Standard
Motivation must uplift; education must be constructive. No shame, no toxic comparison; remove content that promotes harm or misinformation (ed guidelines). (Maps to “uplift not nag” philosophy from V1.)
________________________________________
6) Profiles (“Pages” in SuccessMeter)
6.1 Purpose
Show who I’m becoming, not just what I post. A profile is an identity around Chief Aims + current focus, with a public portfolio of motivational/educational contributions.
6.2 Components
•	Header: avatar, name, handle, bio; location (optional).
•	Chips: Aim chips (theme-level; no private text).
•	Current Goal Spotlight (optional): a single 1-month goal title + due date (privacy toggle).
•	Rhythm Badge (optional): last-7-day A/B ratio or streak (privacy toggle). Ties to Intervals + Graphs.
•	Follow/following counts and button.
•	Tabs: All / Motivation / Education — filter the grid.
•	Posts grid: reels & images; tap to detail → see chips, saves, finishes.
•	Progress Shares: auto-generated cards from Intervals/Goals (“7-day A-streak,” “+6h on ‘Publish Book 1’ this month”).
6.3 Follow Graph Rules
•	Following a user does not automatically flood your feed; it boosts rank only when alignment signals are present (Aim/Goal/Topic match).
•	“Suggested to follow” uses Aim proximity and recent topic overlap.
________________________________________
7) Intervals, Tasks, Goals — Deep Continuity
7.1 Flexible Interval Engine (Non-negotiable Fidelity)
•	Storage is minutes, not “number of cells.” Historical totals never change if interval size changes; grids re-render only. Examples: 15-min mode → duration=15; switch to 10 → new logs=10, past stays intact.
•	“Log Now” snaps to the nearest slot (2:08 with 15-min → 2:00–2:15; 2:23 with 10-min → 2:20–2:30).
7.2 ABCDE Everywhere
•	Log screens enforce ABCDE tagging.
•	Graphs summarize A/B/C time shares; Alignment Index remains a flagship metric.
7.3 Goals & Eisenhower
•	Users place goals/sub-tasks into quadrants to visualize busy vs meaningful. Feed surfacing integrates this by favoring posts that boost Important/Not Urgent skills.
________________________________________
8) Chief Aims Tree — V2 Roles
•	Structural Truth: Daily work is explicitly mapped up to long-term aims; the tree “glows” where activity happens; idle branches grey out.
•	Feed Personalization Backbone: Aims/Goals become ranking signals; private text remains private by default—only thematic chips are displayed.
•	Profile Identity: Optional public chips show thematic identity (“Writer”, “Builder”, “Athlete”).
•	Analytics Views: per-Aim roll-ups: total hours, top active/neglected branches, A/B share, plain-language insights (“80% of your writing time came from Outlining & Drafting”).
________________________________________
9) Analytics & Insights (User-Facing)
9.1 Core Dashboards (V1 + V2 enhancements)
•	Daily/Weekly/Monthly: time distribution by quadrant & goal; streaks; time leaks with comparisons (“+3h in NU vs last week”).
•	Per-Goal: hours, sessions, consistency, recency, focus quality (A+B%), Health Score (pace, consistency, recency, focus quality). Labels: On Track ≥70, At Risk 40–69, Off Track <40.
•	Per-Aim: roll-ups, neglected branches, contribution of goals to Aim total.
9.2 Social-Aware Insights (NEW)
•	Use-Then-Log: “Saved 2 UX posts; you practiced UX for 40m within 24h.”
•	Feed Balance: “Education:Motivation ratio 60:40; when Motivation >60% you log earlier in the day.”
•	Creator Feedback: save/finish ratios, topic resonance heatmaps.
________________________________________
10) Notifications (Respectful, Escalation Ethic)
•	Weekly Review (Sun 6pm): aim-aware summaries; suggest 3 actions.
•	Pace Drift / Neglect: “You’re under pace on ‘Finish Draft’; a 15-min A-block closes today’s gap.”
•	Milestones & Streaks: celebratory; encourage share to profile.
•	Feed-Aware: “Daily 3 is ready for ‘Finish Draft’.”
•	Quiet Hours & Caps with per-category opt-outs; cohort-specific throttling.
________________________________________
11) Session Design, Focus & Guardrails
•	Intent-First: on Feed entry choose Motivation/Education/Both (chips to switch anytime).
•	Soft Timer (opt-in): suggest 5–10 mins; dismissible; shows a “turn learning into action” prompt at end.
•	Action Bridge: at session exit, show 1-tap “Log 10 minutes on [topic] now?” to kickstart an interval.
This honors the SuccessMeter ethic of purposeful time, not infinite scroll.
________________________________________
12) Content Seeding & Cohorts
•	Seed Library: ≥500 high-quality posts hand-tagged across top Aims/Topics to avoid barren feeds at launch.
•	Creator Program: 50 pledges across Writing, Fitness, Entrepreneurship; templates for educational carousels & motivational reels.
•	Cohorts: opt-in 30-day sprints (e.g., “Draft Novel Month”); follow edges auto-suggested intra-cohort.
________________________________________
13) Monetization & Year-1 Model
•	Pro ($5.99/mo or $39.99/yr): ad-free; advanced analytics; enhanced Daily 3; creator deep dives; export.
•	Ads (Free): low-frequency, brand-safe (edtech, wellness, creator tools).
•	Micro-Purchases: templates, reflection packs, dashboards.
Month-12 Base Case (transparent):
105k MAU; 4% paid (4.2k × $5.99 ≈ $25.2k); ads (100.8k × $0.022 ARPDAU × 30 ≈ $66.5k); micro 1% × $3 ≈ $3.1k → ~$94.8k/mo (ARR ≈ $1.14M).
Conservative ~$50k/mo; Upside $190–220k/mo with higher MAU/ARPDAU and 5% paid.
________________________________________
14) Privacy, Safety, and Trust
•	Default Private Text: we never surface goal text unless user opts in; public identity uses thematic chips (Aims).
•	Analytics Anonymization: cohort insights aggregate; creators get ratios, not private user data.
•	Reporting & Appeals: lightweight UI, human backstop for creators.
•	Children/ Sensitive: enforce age gating; block harmful categories; medical/financial claims require disclaimers.
________________________________________
15) Accessibility, Internationalization, Performance
•	WCAG-aware color contrast; captions on reels; transcripts for educational content.
•	Localize Aim/Topic dictionaries; right-to-left support; quote sources localized.
•	Media pipeline: adaptive bitrates; prefetch “Daily 3”; strict cache budgets; graceful degrade on low bandwidth.
________________________________________
16) Acceptance Criteria (Audit List)
1.	Intervals: 10/15/30 rendering; “Log Now” snapping; storage in minutes; historical totals invariant.
2.	ABCDE presence at log time; graphs reflect A/B/C shares; Alignment Index stable.
3.	Feed intention (Motivation/Education/Both) & media preference (Reels/Pics/Both) switchable in 1 tap; persisted.
4.	Ranking shows measurable lift for Aim/Goal/Recent-interval matches vs. baseline.
5.	Daily 3 includes ≥1 Education item when user intention is Education or Both.
6.	Profiles: public grid, follow counts, Aim chips, optional goal spotlight & rhythm badge; privacy respected.
7.	Follow boost only when aligned; otherwise neutral.
8.	Seed content covers top 10 Aim×Topic combos; cold-start feed non-empty.
9.	Notifications: weekly review; neglect & pace; Daily 3; quiet hours honored.
10.	Moderation: tagged content only; spam throttles; report→review flow works.
________________________________________
17) Risks & Mitigations
•	Content starvation: solve with seed library + creator pledges + cohorts.
•	Drift into entertainment: enforce intent-first, Daily 3, session guardrails, alignment-weighted ranking.
•	Privacy fear: thematic chips, default-private goal text, transparent settings.
•	Gaming tags: reputation weighting; downrank creators with poor save/finish ratios.
________________________________________
18) Differentiation & Uniqueness
•	Unique Objective Function: optimize for aligned life progress, not attention.
•	Tree-Backed Personalization: the only feed that truly reads from long-term aims → short-term goals → real intervals.
•	Rituals That Reduce Doomscrolling: intent-first, Daily 3, action bridge to log a block.
Verdict: Substantively unique in positioning and mechanics; overlap only in media containers, not in purpose.
________________________________________
19) Probability of Success (12-Month Read)
•	Early PMF with purpose-driven segment: ~65%
•	Base revenue case (~$95k/mo by M12): ~40%
•	Upside case (>$190k/mo): ~20%
Rationale: differentiated backbone (Intervals/Goals/Tree/Analytics/Notifications) already proven conceptually; primary risks = seeding + keeping alignment sacrosanct.
________________________________________
20) Ops Artifacts to Produce Next (No Code Yet)
1.	Tag Dictionary v1.0 (Aims, Topics, Emotion tags; synonyms; localization hints).
2.	Seed Content Plan (500 posts) by Aim×Topic matrix with creative briefs and QC checklist.
3.	Ranking Rules v1 (weights, thresholds, backfills, diversity rules).
4.	Profile & Follow UX maps (privacy toggles, spotlight flows, share-from-Intervals).
5.	Moderation Policy (examples, auto-actions, appeal paths).
6.	Notification Spec (copy, triggers, limits, quiet hours).
________________________________________
Appendix A — Canonical V1 Concepts (Ground Truth)
•	Flexible intervals (10/15/30), stored as minutes; analytics independent of interval size; “Log Now” snapping.
•	Goals (A–E), Eisenhower quadrants, ABCDE decision layer across logging and analysis.
•	Graphs & Analytics: daily/weekly/monthly; Important vs Not; Urgent vs Not; streaks; time-leak insights.
•	Mission & Quotes: motivational anchor; daily quote & reflection.
•	Chief Aims Tree: hierarchical aims; visual growth; optional gamification; per-aim rollups & health.
•	Per-Goal Analytics & Health Score (pace, consistency, recency, focus quality).
•	Respectful Notifications with escalation ethics and quiet hours.

_2.png)