/**
 * Loghy web demo — UI layer.
 *
 * Mirrors ui/MainActivity.kt: a top bar, the fluid creature, five tabs
 * (Chat / SWOT / Lateral / Hit List / Vault), a PIN gate and modal sheets.
 * State lives in localStorage, standing in for the app's Room database.
 */

import * as Brain from "./brain.js";

/* ------------------------------------------------------------------- state */

const STORE_KEY = "loghy.demo.v1";

const defaultState = () => ({
  profile: { thinkerName: "Thinker", pin: null, personality: Brain.PersonalityStyles[0] },
  ideas: [
    { id: 1, title: "Neighbourhood sourdough subscription", rawConcept: "Weekly sourdough drops for my street", domain: "Local / Food Business" },
  ],
  activeIdeaId: 1,
  messages: [
    { id: 1, ideaId: 1, sender: "LOGHY", content: "Hi. What challenge or idea are we testing today?", ts: Date.now() },
  ],
  swot: {},        // ideaId -> analysis
  lateral: [],     // sessions
  tasks: {},       // ideaId -> task[]
  notes: [
    {
      id: 1,
      title: "de Bono: Vertical vs Lateral Thinking",
      content:
        "Vertical thinking is digging the same hole deeper. Lateral thinking is trying somewhere else. You cannot dig a hole in a different place by digging the same hole deeper.",
      tone: 0,
      updatedAt: Date.now(),
      versions: [{ n: 1, title: "de Bono: Vertical vs Lateral Thinking", content: "Vertical thinking is digging the same hole deeper. Lateral thinking is trying somewhere else. You cannot dig a hole in a different place by digging the same hole deeper.", ts: Date.now(), summary: "Initial entry" }],
    },
  ],
  nextId: 100,
});

let state = load();

function load() {
  try {
    const raw = localStorage.getItem(STORE_KEY);
    if (raw) return { ...defaultState(), ...JSON.parse(raw) };
  } catch { /* fall through to defaults */ }
  return defaultState();
}
function save() {
  try { localStorage.setItem(STORE_KEY, JSON.stringify(state)); } catch { /* private mode */ }
}
const uid = () => ++state.nextId;
const activeIdea = () => state.ideas.find((i) => i.id === state.activeIdeaId) || state.ideas[0];

/* ------------------------------------------------------------------- utils */

const $ = (sel) => document.querySelector(sel);
const el = (tag, cls, text) => {
  const n = document.createElement(tag);
  if (cls) n.className = cls;
  if (text != null) n.textContent = text;
  return n;
};
const fmtDate = (ts) =>
  new Date(ts).toLocaleString(undefined, { month: "short", day: "numeric", hour: "2-digit", minute: "2-digit" });

let toastTimer;
function toast(msg) {
  const t = $("#toast");
  t.textContent = msg;
  t.classList.add("show");
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => t.classList.remove("show"), 2200);
}

/* ---------------------------------------------------------------- creature */

/**
 * The fluid companion from ui/components/FluidCreature.kt: a blob of ink with a
 * harmonic wobble, two eyes that blink and track a gaze direction, and a mouth dot.
 */
const CreatureState = { IDLE: "IDLE", THINKING: "THINKING", SPEAKING: "SPEAKING", LISTENING: "LISTENING" };
let creatureState = CreatureState.IDLE;
let gaze = "CENTER";
let blink = 1;
let t0 = performance.now();

function blobPath(cx, cy, r, phase, points = 10) {
  const pts = [];
  for (let i = 0; i < points; i++) {
    const a = (i / points) * Math.PI * 2;
    const wob =
      Math.sin(a * 2 + phase) * 0.035 +
      Math.cos(a * 3 + phase * 0.8) * 0.025 +
      Math.sin(a + phase * 0.7) * 0.015;
    const rr = r * (1 + wob);
    pts.push([cx + rr * Math.cos(a), cy + rr * Math.sin(a)]);
  }
  let d = "";
  for (let i = 0; i < points; i++) {
    const cur = pts[i];
    const next = pts[(i + 1) % points];
    const mid = [(cur[0] + next[0]) / 2, (cur[1] + next[1]) / 2];
    if (i === 0) {
      const prev = pts[points - 1];
      d += `M ${(prev[0] + cur[0]) / 2} ${(prev[1] + cur[1]) / 2} `;
    }
    d += `Q ${cur[0]} ${cur[1]} ${mid[0]} ${mid[1]} `;
  }
  return d + "Z";
}

function drawCreature(svg, size) {
  const now = performance.now();
  const el_ = (now - t0) / 1000;
  const phase = el_ * (creatureState === CreatureState.THINKING ? 2.4 : 1.2);
  const floatY = Math.sin(el_ * 1.1) * 3;
  const cx = 60, cy = 58 + floatY;
  const r = creatureState === CreatureState.LISTENING ? 34 : 32;

  const gazeX = gaze === "LOOK_DOWN" ? 0 : gaze === "LOOK_UP_RIGHT" ? 3 : 0;
  const gazeY = gaze === "LOOK_DOWN" ? 5 : gaze === "LOOK_UP_RIGHT" ? -3 : 0;

  const eyeY = cy - 3 + gazeY;
  const eyeDx = 11;
  const eyeRy = Math.max(0.6, 5 * blink);

  svg.innerHTML = `
    <ellipse cx="${cx}" cy="${cy + r + 8}" rx="${r * 0.72}" ry="${r * 0.13}" fill="rgba(140,131,117,.30)"/>
    <path d="${blobPath(cx, cy, r, phase)}" fill="#141316"/>
    <ellipse cx="${cx - eyeDx + gazeX}" cy="${eyeY}" rx="3.6" ry="${eyeRy}" fill="#FBFBFB"/>
    <ellipse cx="${cx + eyeDx + gazeX}" cy="${eyeY}" rx="3.6" ry="${eyeRy}" fill="#FBFBFB"/>
    <circle cx="${cx + gazeX}" cy="${eyeY + 12}" r="1.9" fill="#FBFBFB" opacity=".85"/>
  `;
  if (size) { svg.setAttribute("width", size); svg.setAttribute("height", size); }
}

// Blink roughly every four seconds, as the Compose version does.
setInterval(() => {
  blink = 0.06;
  setTimeout(() => (blink = 1), 120);
}, 4000);

function animate() {
  drawCreature($("#creature"));
  const lock = $("#lockCreature");
  if ($("#lockScreen").classList.contains("open")) drawCreature(lock);
  requestAnimationFrame(animate);
}

/* ------------------------------------------------------------------- tabs */

const TABS = [
  { id: "stage", label: "Chat", icon: `<circle cx="9" cy="10" r="4.4"/><circle cx="16" cy="15" r="3"/>` },
  { id: "swot", label: "SWOT", icon: `<path d="M12 3l7 3v6c0 4.2-2.9 7.6-7 9-4.1-1.4-7-4.8-7-9V6z"/>` },
  { id: "lateral", label: "Lateral", icon: `<path d="M9 18h6M10 21h4M12 3a6 6 0 0 1 3.6 10.8c-.5.4-.6.9-.6 1.2H9c0-.3-.1-.8-.6-1.2A6 6 0 0 1 12 3z"/>` },
  { id: "schedule", label: "Hit List", icon: `<circle cx="12" cy="12" r="8.4"/><path d="M12 7.5V12l3 2"/>` },
  { id: "vault", label: "Vault", icon: `<path d="M3.6 7.5h6l1.8 2.2h8.9v9.3H3.6z"/><path d="M3.6 7.5V5.2h5.2"/>` },
];
let currentTab = "stage";

function renderTabs() {
  const bar = $("#tabbar");
  bar.innerHTML = "";
  TABS.forEach((t) => {
    const b = el("button", `tab${t.id === currentTab ? " on" : ""}`);
    b.innerHTML = `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round">${t.icon}</svg><span>${t.label}</span>`;
    b.onclick = () => selectTab(t.id);
    bar.appendChild(b);
  });
}

function selectTab(id) {
  currentTab = id;
  document.querySelectorAll(".screen").forEach((s) => s.classList.remove("active"));
  $(`#screen-${id}`).classList.add("active");
  // The companion shrinks away from the Stage, matching animateDpAsState in MainActivity.
  const size = id === "stage" ? 130 : 68;
  $("#creature").setAttribute("width", size);
  $("#creature").setAttribute("height", size);
  renderTabs();
  render();
}

/* ------------------------------------------------------------------- chat */

const CHIPS = [
  "SWOT my idea", "Socratic test", "Reading sources",
  "Random word stimulus", "Provocation PO", "Reversal method",
];

function renderChips() {
  const c = $("#chips");
  c.innerHTML = "";
  CHIPS.forEach((label) => {
    const b = el("button", "chip btn", label);
    b.onclick = () => sendMessage(label);
    c.appendChild(b);
  });
}

/** Renders Loghy's structured reply with ink droplet bullets and header underlines. */
function renderRichText(container, text) {
  text.split("\n").forEach((line) => {
    const trimmed = line.trim();
    if (!trimmed) { container.appendChild(el("div", null, " ")).style.height = "6px"; return; }

    if (trimmed.startsWith("•")) {
      const row = el("div", "bullet");
      row.appendChild(el("div", "droplet sm dot"));
      row.appendChild(el("div", null, trimmed.replace(/^•\s*/, "")));
      container.appendChild(row);
      return;
    }
    if (trimmed.endsWith(":")) {
      const h = el("span", "hdr", trimmed);
      container.appendChild(h);
      const u = el("div", "underline-ink");
      u.innerHTML = `<svg width="100%" height="5" viewBox="0 0 200 5" preserveAspectRatio="none"><path d="M0 3 Q 25 1 50 3 T 100 3 T 150 3 T 200 3" fill="none" stroke="#0b0a09" stroke-width="1.4" stroke-linecap="round"/></svg>`;
      container.appendChild(u);
      return;
    }
    container.appendChild(el("div", null, trimmed));
  });
}

function renderChat() {
  const scroll = $("#chatScroll");
  scroll.innerHTML = "";
  const msgs = state.messages.filter((m) => m.ideaId === state.activeIdeaId);

  msgs.forEach((m) => {
    const card = el("div", `msg raised ${m.sender === "USER" ? "user" : "loghy"}`);
    const who = el("div", "who");
    who.appendChild(el("div", "droplet"));
    who.appendChild(el("span", null, m.sender === "USER" ? "You" : "Loghy"));
    card.appendChild(who);

    const body = el("div", "text");
    if (m.sender === "LOGHY") renderRichText(body, m.content);
    else body.textContent = m.content;
    card.appendChild(body);

    if (m.sender === "LOGHY") {
      const save = el("button", "btn tiny save", "Save to Vault");
      save.onclick = () => {
        const first = m.content.split("\n").find((l) => l.trim()) || "Captured note";
        addNote(first.slice(0, 48), m.content, 0);
        toast("Saved to Vault.");
      };
      card.appendChild(save);
    }
    scroll.appendChild(card);
  });
  scroll.scrollTop = scroll.scrollHeight;
}

let thinking = false;

function sendMessage(text) {
  const trimmed = (text || "").trim();
  if (!trimmed || thinking) return;

  state.messages.push({ id: uid(), ideaId: state.activeIdeaId, sender: "USER", content: trimmed, ts: Date.now() });
  save();
  renderChat();

  thinking = true;
  creatureState = CreatureState.THINKING;
  gaze = "LOOK_UP_RIGHT";
  showBubble(Brain.randomThinkingQuote(), true);

  setTimeout(() => {
    const reply = Brain.answerSocratically(trimmed, activeIdea().domain);
    state.messages.push({ id: uid(), ideaId: state.activeIdeaId, sender: "LOGHY", content: reply, ts: Date.now() });
    save();
    thinking = false;
    creatureState = CreatureState.IDLE;
    gaze = "CENTER";
    hideBubble();
    renderChat();
  }, 900);
}

function showBubble(text, isThinking) {
  const b = $("#thoughtBubble");
  b.textContent = text;
  b.hidden = false;
  b.style.fontStyle = isThinking ? "italic" : "normal";
}
function hideBubble() { $("#thoughtBubble").hidden = true; }

/* ------------------------------------------------------------------- SWOT */

function renderSwot() {
  const scroll = $("#swotScroll");
  scroll.innerHTML = "";
  const idea = activeIdea();

  const intro = el("div", "demo-note debossed");
  intro.textContent = `Reality check for “${idea.title}” — domain: ${idea.domain}. Four plain-language angles, then fixes, sources and the ethics line.`;
  scroll.appendChild(intro);

  const runRow = el("div", null);
  runRow.style.margin = "10px 0";
  const run = el("button", "btn", state.swot[idea.id] ? "Re-run reality check" : "Run reality check");
  run.onclick = () => {
    state.swot[idea.id] = Brain.evaluateSwot(idea.rawConcept || idea.title, idea.domain);
    save();
    toast("Reality check complete.");
    renderSwot();
  };
  runRow.appendChild(run);
  scroll.appendChild(runRow);

  const s = state.swot[idea.id];
  if (!s) {
    scroll.appendChild(Object.assign(el("div", "empty"), {
      textContent: "No reality check yet. Run one to see what holds up, where it leaks, the outside hurdles and the hidden upside.",
    }));
    return;
  }

  const grid = el("div", "swot-grid");
  const section = (cls, heading, items) => {
    const card = el("div", `swot-card raised ${cls}`);
    card.appendChild(el("h4", null, heading));
    const ul = el("ul");
    items.forEach((i) => {
      const li = el("li");
      li.appendChild(el("div", "droplet sm dot"));
      li.appendChild(el("div", null, i));
      ul.appendChild(li);
    });
    card.appendChild(ul);
    return card;
  };
  grid.appendChild(section("s", "What holds up (Strengths)", s.whatHoldsUp));
  grid.appendChild(section("w", "Vulnerable spots (Weaknesses)", s.vulnerableSpots));
  grid.appendChild(section("t", "Outside hurdles (Threats)", s.outsideHurdles));
  grid.appendChild(section("o", "Hidden upside (Opportunities)", s.hiddenUpside));
  grid.appendChild(section("s", "Actionable fixes", s.actionableFixes));
  grid.appendChild(section("o", "Probing questions", s.probingQuestions));
  scroll.appendChild(grid);

  const src = el("div", "swot-card raised sources");
  src.style.marginTop = "10px";
  src.appendChild(el("h4", null, "Reading sources"));
  const ul = el("ul");
  s.readingSources.forEach((r) => {
    const li = el("li");
    li.appendChild(el("div", "droplet sm dot"));
    const m = r.match(/^(.*?)\s+—\s+(https?:\/\/\S+)$/);
    if (m) {
      const wrap = el("div");
      wrap.appendChild(document.createTextNode(m[1] + " "));
      const a = el("a", null, "link");
      a.href = m[2]; a.target = "_blank"; a.rel = "noopener noreferrer";
      wrap.appendChild(a);
      li.appendChild(wrap);
    } else li.appendChild(el("div", null, r));
    ul.appendChild(li);
  });
  src.appendChild(ul);
  scroll.appendChild(src);

  const eth = el("div", "ethics debossed");
  eth.style.marginTop = "10px";
  eth.textContent = s.ethics;
  scroll.appendChild(eth);
}

/* ---------------------------------------------------------------- lateral */

let activeStimulus = null;
let activeHat = null;

function renderLateral() {
  const scroll = $("#lateralScroll");
  scroll.innerHTML = "";
  const idea = activeIdea();

  const techs = [
    { key: "RANDOM_WORD", name: "Random Word", sub: "Cross-fertilisation" },
    { key: "PROVOCATION_PO", name: "Provocation PO", sub: "Stepping stone" },
    { key: "REVERSAL", name: "Reversal", sub: "Invert the premise" },
    { key: "SIX_HATS", name: "Six Hats", sub: "Perspective shift" },
  ];

  const row = el("div", "tech-row");
  techs.forEach((t) => {
    const b = el("button", "tech btn");
    b.appendChild(el("div", "n", t.name));
    b.appendChild(el("div", "s", t.sub));
    b.onclick = () => {
      if (t.key === "SIX_HATS") { activeHat = 0; activeStimulus = null; }
      else {
        activeHat = null;
        activeStimulus =
          t.key === "RANDOM_WORD" ? Brain.generateRandomWordExercise(idea.title)
          : t.key === "PROVOCATION_PO" ? Brain.generateProvocationExercise()
          : Brain.generateReversalExercise(idea.title);
        state.lateral.unshift({ id: uid(), ideaId: idea.id, technique: t.key, stimulus: activeStimulus.stimulus, ts: Date.now() });
        save();
      }
      renderLateral();
    };
    row.appendChild(b);
  });
  scroll.appendChild(row);

  if (activeHat !== null) {
    const hats = el("div", "hat-row");
    Brain.SixHats.forEach((h, i) => {
      const b = el("button", `hat btn${i === activeHat ? " on" : ""}`, h.colorName);
      b.onclick = () => { activeHat = i; renderLateral(); };
      hats.appendChild(b);
    });
    scroll.appendChild(hats);

    const h = Brain.getHatExercise(activeHat);
    const card = el("div", "stimulus raised");
    card.appendChild(el("div", "word", h.colorName));
    card.appendChild(el("div", "label", h.focus));
    card.appendChild(el("div", "p", h.question));
    scroll.appendChild(card);
  } else if (activeStimulus) {
    const card = el("div", "stimulus raised");
    card.appendChild(el("div", "word", activeStimulus.stimulus));
    card.appendChild(el("div", "p", activeStimulus.prompt));
    scroll.appendChild(card);
  } else {
    scroll.appendChild(Object.assign(el("div", "empty"), {
      textContent: "Pick a de Bono technique above. Each one is a deliberate pattern break, not a search for the right answer.",
    }));
  }

  const sessions = state.lateral.filter((l) => l.ideaId === idea.id);
  if (sessions.length) {
    scroll.appendChild(Object.assign(el("div", "label"), { textContent: "Past stimuli", style: "margin:14px 0 6px" }));
    sessions.slice(0, 8).forEach((l) => {
      const s = el("div", "snap raised");
      s.appendChild(el("div", "n", l.technique.replace(/_/g, " ")));
      s.appendChild(el("div", "when", fmtDate(l.ts)));
      s.appendChild(el("div", "txt", l.stimulus));
      scroll.appendChild(s);
    });
  }
}

/* --------------------------------------------------------------- hit list */

function renderSchedule() {
  const scroll = $("#scheduleScroll");
  scroll.innerHTML = "";
  const idea = activeIdea();
  let tasks = state.tasks[idea.id];

  const intro = el("div", "demo-note debossed");
  intro.textContent = "A five-step hit list with honest hour estimates and the kind of focus each step needs.";
  scroll.appendChild(intro);

  const gen = el("button", "btn", tasks ? "Regenerate hit list" : "Generate hit list");
  gen.style.margin = "10px 0";
  gen.onclick = () => {
    state.tasks[idea.id] = Brain.calculateDevelopmentSchedule().map((t, i) => ({ ...t, id: uid(), done: false, order: i }));
    save();
    toast("Hit list ready.");
    renderSchedule();
  };
  scroll.appendChild(gen);

  tasks = state.tasks[idea.id];
  if (!tasks) {
    scroll.appendChild(Object.assign(el("div", "empty"), { textContent: "No hit list yet for this idea." }));
    return;
  }

  const total = tasks.reduce((a, t) => a + t.estimatedHours, 0);
  const doneCount = tasks.filter((t) => t.done).length;
  const sum = el("div", "demo-note");
  sum.textContent = `${doneCount} of ${tasks.length} done · ${total.toFixed(1)} h estimated`;
  scroll.appendChild(sum);

  tasks.forEach((t) => {
    const card = el("div", `task raised${t.done ? " done" : ""}`);
    const check = el("div", "check");
    check.onclick = () => { t.done = !t.done; save(); renderSchedule(); };
    card.appendChild(check);

    const main = el("div");
    main.style.flex = "1";
    main.appendChild(el("div", "phase", t.phase));
    main.appendChild(el("div", "ttl", t.title));
    main.appendChild(el("div", "desc", t.description));
    const meta = el("div", "meta");
    [`${t.estimatedHours} h`, t.effortLevel, t.cognitiveLoad, t.recommendedSprint].forEach((p) =>
      meta.appendChild(el("span", "pill", p))
    );
    main.appendChild(meta);
    card.appendChild(main);
    scroll.appendChild(card);
  });
}

/* ------------------------------------------------------------------ vault */

function addNote(title, content, tone) {
  const now = Date.now();
  state.notes.unshift({
    id: uid(), title: title || "Untitled Note", content, tone: tone ?? 0, updatedAt: now,
    versions: [{ n: 1, title: title || "Untitled Note", content, ts: now, summary: "Initial entry" }],
  });
  save();
}

function saveNoteEdit(note, title, content, tone, summary) {
  const next = (note.versions[note.versions.length - 1]?.n || 0) + 1;
  note.title = title; note.content = content; note.tone = tone; note.updatedAt = Date.now();
  note.versions.push({ n: next, title, content, ts: Date.now(), summary: summary || "Edited note" });
  // 20-version cap, newest kept — same rule as PersonalNote.withNewVersion.
  if (note.versions.length > 20) note.versions = note.versions.slice(-20);
  save();
}

function renderVault() {
  const scroll = $("#vaultScroll");
  scroll.innerHTML = "";

  const head = el("div", null);
  head.style.cssText = "display:flex;align-items:center;gap:10px;margin-bottom:10px";
  const count = el("div", "caption", `${state.notes.length} note${state.notes.length === 1 ? "" : "s"} · up to 20 snapshots each`);
  count.style.flex = "1";
  head.appendChild(count);
  const add = el("button", "btn tiny", "+ New note");
  add.onclick = () => openNoteEditor(null);
  head.appendChild(add);
  scroll.appendChild(head);

  if (!state.notes.length) {
    scroll.appendChild(Object.assign(el("div", "empty"), { textContent: "The vault is empty." }));
    return;
  }

  state.notes.forEach((n) => {
    const card = el("div", "note raised");
    const h = el("div", "head");
    const tone = el("div", "tone");
    tone.style.background = `var(--tone-${n.tone})`;
    h.appendChild(tone);
    h.appendChild(el("div", "ttl", n.title));
    const ver = el("div", "ver", `v${n.versions.length} (${n.versions.length}/20)`);
    ver.onclick = () => openHistory(n);
    h.appendChild(ver);
    card.appendChild(h);
    card.appendChild(el("div", "body-txt", n.content));

    const acts = el("div", "acts");
    const hist = el("button", "btn tiny", "History");
    hist.onclick = () => openHistory(n);
    const edit = el("button", "btn tiny", "Edit");
    edit.onclick = () => openNoteEditor(n);
    const del = el("button", "btn tiny", "Delete");
    del.onclick = () => {
      state.notes = state.notes.filter((x) => x.id !== n.id);
      save(); toast("Note deleted."); renderVault();
    };
    acts.append(hist, edit, del);
    card.appendChild(acts);
    scroll.appendChild(card);
  });
}

/* ----------------------------------------------------------------- sheets */

function openSheet(title, build) {
  $("#sheetTitle").textContent = title;
  const body = $("#sheetBody");
  body.innerHTML = "";
  build(body);
  $("#overlay").classList.add("open");
}
function closeSheet() { $("#overlay").classList.remove("open"); }

function openNoteEditor(note) {
  openSheet(note ? "Edit Note" : "New Note", (body) => {
    let tone = note?.tone ?? 0;

    const t = el("div", "field-block");
    t.appendChild(el("label", "label", "Title"));
    const ti = el("input"); ti.value = note?.title || ""; ti.placeholder = "Give it a name";
    t.appendChild(ti); body.appendChild(t);

    const c = el("div", "field-block");
    c.appendChild(el("label", "label", "Content"));
    const ta = el("textarea"); ta.value = note?.content || "";
    c.appendChild(ta); body.appendChild(c);

    const tn = el("div", "field-block");
    tn.appendChild(el("label", "label", "Paper tone"));
    const tones = el("div", "tones");
    for (let i = 0; i < 7; i++) {
      const p = el("div", `tone-pick${i === tone ? " on" : ""}`);
      p.style.background = `var(--tone-${i})`;
      p.onclick = () => {
        tone = i;
        tones.querySelectorAll(".tone-pick").forEach((x, j) => x.classList.toggle("on", j === i));
      };
      tones.appendChild(p);
    }
    tn.appendChild(tones); body.appendChild(tn);

    let summaryInput = null;
    if (note) {
      const s = el("div", "field-block");
      s.appendChild(el("label", "label", "What changed? (optional)"));
      summaryInput = el("input"); summaryInput.placeholder = "e.g. Tightened the scope";
      s.appendChild(summaryInput); body.appendChild(s);
    }

    const go = el("button", "btn", note ? "Save version" : "Save note");
    go.style.width = "100%";
    go.onclick = () => {
      if (!ta.value.trim()) { toast("Nothing to save yet."); return; }
      if (note) saveNoteEdit(note, ti.value.trim() || note.title, ta.value, tone, summaryInput.value.trim());
      else addNote(ti.value.trim(), ta.value, tone);
      closeSheet(); renderVault();
      toast(note ? "Version saved." : "Note saved.");
    };
    body.appendChild(go);
  });
}

function openHistory(note) {
  openSheet("Version History", (body) => {
    body.appendChild(Object.assign(el("div", "caption"), {
      textContent: `${note.versions.length}/20 edits recorded`, style: "margin-bottom:10px",
    }));
    [...note.versions].reverse().forEach((v) => {
      const s = el("div", "snap raised");
      const top = el("div");
      top.style.cssText = "display:flex;align-items:center;gap:8px";
      top.appendChild(el("div", "n", `Version #${v.n}`));
      const restore = el("button", "btn tiny", "Restore");
      restore.style.marginLeft = "auto";
      restore.onclick = () => {
        saveNoteEdit(note, v.title, v.content, note.tone, `Restored version #${v.n}`);
        closeSheet(); renderVault();
        toast(`Restored version #${v.n}.`);
      };
      top.appendChild(restore);
      s.appendChild(top);
      s.appendChild(el("div", "when", `${fmtDate(v.ts)} · ${v.summary}`));
      s.appendChild(el("div", "txt", v.content.slice(0, 180) + (v.content.length > 180 ? "…" : "")));
      body.appendChild(s);
    });
  });
}

function openIdeaSelector() {
  openSheet("Ideas", (body) => {
    state.ideas.forEach((i) => {
      const card = el("div", "note raised");
      card.style.cursor = "pointer";
      card.appendChild(el("div", "ttl", i.title));
      card.appendChild(el("div", "caption", i.domain));
      card.onclick = () => {
        state.activeIdeaId = i.id; save(); closeSheet(); render();
        toast(`Switched to “${i.title}”.`);
      };
      body.appendChild(card);
    });

    body.appendChild(Object.assign(el("div", "label"), { textContent: "New idea", style: "margin:16px 0 6px" }));
    const t = el("div", "field-block");
    const ti = el("input"); ti.placeholder = "What are you testing?";
    t.appendChild(ti); body.appendChild(t);

    const d = el("div", "field-block");
    const sel = el("select");
    Brain.AvailableDomains.forEach((dom) => {
      const o = el("option", null, dom); o.value = dom; sel.appendChild(o);
    });
    d.appendChild(sel); body.appendChild(d);

    const go = el("button", "btn", "Create idea");
    go.style.width = "100%";
    go.onclick = () => {
      if (!ti.value.trim()) { toast("Give the idea a name first."); return; }
      const id = uid();
      state.ideas.push({ id, title: ti.value.trim(), rawConcept: ti.value.trim(), domain: sel.value });
      state.activeIdeaId = id;
      state.messages.push({ id: uid(), ideaId: id, sender: "LOGHY", content: "Hi. What challenge or idea are we testing today?", ts: Date.now() });
      save(); closeSheet(); render();
      toast("Idea created.");
    };
    body.appendChild(go);
  });
}

function openSettings() {
  openSheet("Settings", (body) => {
    body.appendChild(Object.assign(el("div", "label"), { textContent: "Personality" }));
    const row = el("div", "hat-row");
    Brain.PersonalityStyles.forEach((p) => {
      const b = el("button", `hat btn${p === state.profile.personality ? " on" : ""}`, p);
      b.onclick = () => {
        state.profile.personality = p; save();
        toast(Brain.randomPersonalityReaction());
        closeSheet();
      };
      row.appendChild(b);
    });
    body.appendChild(row);

    body.appendChild(Object.assign(el("div", "label"), { textContent: "App lock", style: "margin-top:16px" }));
    const pinRow = el("div", "hat-row");
    if (state.profile.pin) {
      const off = el("button", "btn tiny", "Remove PIN");
      off.onclick = () => { state.profile.pin = null; save(); closeSheet(); toast("PIN removed."); };
      const lock = el("button", "btn tiny", "Lock now");
      lock.onclick = () => { closeSheet(); openLock(false); };
      pinRow.append(off, lock);
    } else {
      const on = el("button", "btn tiny", "Set 4-digit PIN");
      on.onclick = () => { closeSheet(); openLock(true); };
      pinRow.appendChild(on);
    }
    body.appendChild(pinRow);

    const about = el("div", "demo-note debossed");
    about.style.marginTop = "16px";
    about.textContent =
      "Web demo of the Loghy Android app. The Socratic engine, SWOT content and de Bono exercises are ported from the app and run entirely in your browser — no network calls, no API keys, nothing leaves this page. Notes live in localStorage.";
    body.appendChild(about);

    const reset = el("button", "btn tiny", "Reset demo data");
    reset.style.marginTop = "10px";
    reset.onclick = () => {
      localStorage.removeItem(STORE_KEY);
      state = defaultState(); save(); closeSheet(); render();
      toast("Demo reset.");
    };
    body.appendChild(reset);
  });
}

/* ------------------------------------------------------------------- lock */

let pinBuffer = "";
let settingPin = false;

function openLock(isSetting) {
  settingPin = isSetting;
  pinBuffer = "";
  $("#lockSub").textContent = isSetting ? "Choose a 4-digit PIN" : "Brainstorming Vault Locked";
  $("#pinError").textContent = "";
  $("#lockScreen").classList.add("open");
  renderPin();
}
function closeLock() { $("#lockScreen").classList.remove("open"); }

function renderPin() {
  const dots = $("#pinDots");
  dots.innerHTML = "";
  for (let i = 0; i < 4; i++) dots.appendChild(el("div", `pd${i < pinBuffer.length ? " on" : ""}`));
}

function renderKeypad() {
  const kp = $("#keypad");
  kp.innerHTML = "";
  ["1","2","3","4","5","6","7","8","9","C","0","⌫"].forEach((k) => {
    const b = el("button", "btn", k);
    b.onclick = () => {
      $("#pinError").textContent = "";
      if (k === "C") pinBuffer = "";
      else if (k === "⌫") pinBuffer = pinBuffer.slice(0, -1);
      else if (pinBuffer.length < 4) pinBuffer += k;
      renderPin();

      if (pinBuffer.length === 4) {
        setTimeout(() => {
          if (settingPin) {
            state.profile.pin = pinBuffer; save();
            closeLock(); toast("App lock activated.");
          } else if (pinBuffer === state.profile.pin) {
            closeLock();
          } else {
            $("#pinError").textContent = "Incorrect PIN. Try again.";
            pinBuffer = ""; renderPin();
          }
        }, 160);
      }
    };
    kp.appendChild(b);
  });
}

/* ------------------------------------------------------------------ render */

function render() {
  const idea = activeIdea();
  $("#ideaTitle").textContent = idea ? idea.title : "Select Idea";
  $("#ideaDomain").textContent = idea ? idea.domain : "Socratic";

  if (currentTab === "stage") renderChat();
  if (currentTab === "swot") renderSwot();
  if (currentTab === "lateral") renderLateral();
  if (currentTab === "schedule") renderSchedule();
  if (currentTab === "vault") renderVault();
}

/* -------------------------------------------------------------------- init */

function init() {
  renderTabs();
  renderChips();
  renderKeypad();
  selectTab("stage");

  $("#sendBtn").onclick = () => { sendMessage($("#chatInput").value); $("#chatInput").value = ""; };
  $("#chatInput").addEventListener("keydown", (e) => {
    if (e.key === "Enter") { sendMessage($("#chatInput").value); $("#chatInput").value = ""; }
  });
  $("#chatInput").addEventListener("focus", () => { gaze = "LOOK_DOWN"; });
  $("#chatInput").addEventListener("blur", () => { gaze = "CENTER"; });

  $("#ideaBtn").onclick = openIdeaSelector;
  $("#settingsBtn").onclick = openSettings;
  $("#lockBtn").onclick = () => {
    if (state.profile.pin) openLock(false);
    else toast("Set a PIN in Settings first.");
  };
  $("#sheetClose").onclick = closeSheet;
  $("#overlay").addEventListener("click", (e) => { if (e.target.id === "overlay") closeSheet(); });

  $("#creature").onclick = () => {
    if (thinking) return;
    showBubble(Brain.randomInspiringThought(), false);
    setTimeout(hideBubble, 5200);
  };

  if (state.profile.pin) openLock(false);
  animate();
}

init();
