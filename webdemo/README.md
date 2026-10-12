# Loghy — Web Demo

A browser copy of the Loghy Android app, built to show the product without an
emulator or an APK install.

## Running it

ES modules do not load over `file://`, so it needs a static server:

```bash
npx --yes http-server webdemo -p 8777 -c-1
```

Then open <http://localhost:8777>. Any static server works; there is no build step,
no bundler and no dependencies.

## What it covers

All five tabs from `MainActivity.kt`:

| Tab | What it does |
|---|---|
| **Chat** | The fluid companion and the Socratic stream. Ask anything and get the four-angle reality check. |
| **SWOT** | The full breakdown — what holds up, vulnerable spots, outside hurdles, hidden upside, plus fixes, probing questions, reading sources and the ethics line. |
| **Lateral** | Edward de Bono techniques: Random Word, Provocation PO, Reversal and Six Thinking Hats. |
| **Hit List** | The five-step development schedule with hour estimates, effort level and cognitive load. |
| **Vault** | Notes with 7 paper tones, up to 20 version snapshots each, and non-destructive restore. |

Also included: the PIN lock screen, the idea selector with all five domains, the
personality picker, and the inspiring-thought bubble when you tap the companion.

## Fidelity

The content is **ported, not rewritten**. `brain.js` carries the same copy as
`engine/SocraticBrain.kt` — every domain's SWOT lists, the provocations, reversals,
six hats, thinking quotes and reprimands — and the same routing rules, so the demo
answers the way the app does.

The design system in `styles.css` uses the tokens from `ui/theme/Color.kt` verbatim.
The raised and debossed surfaces reproduce the twin directional lighting from
`ui/components/NeumorphicSurfaces.kt` (top-left specular bloom, bottom-right warm
diffuse shadow) with `box-shadow` in place of Compose's `drawBehind`. Type follows
`ui/theme/Type.kt`: serif for display, headline, title and body-large; sans for the
rest. The companion is an SVG blob with the same harmonic wobble, blink cycle and
gaze directions as `ui/components/FluidCreature.kt`.

## Where it differs

Honest list, so nothing here is mistaken for the app:

- **Storage is `localStorage`, not Room.** Clearing site data resets the demo.
  "Reset demo data" in Settings does the same deliberately.
- **No PBKDF2.** The app hashes the PIN with a salt; here it is compared directly.
  This is a UI demo, not a security boundary — do not treat the lock as protection.
- **No voice.** The app has speech-to-text and TTS; this has neither.
- **No reminders.** The app schedules real notifications; the Hit List here is
  checkboxes only.
- **Text answers only.** The app's image attachment and feedback-report flows are
  not carried over.
- **One seeded idea** rather than a full multi-idea database, though you can add more.

## Files

```
webdemo/
├── index.html    shell: top bar, creature, five screens, sheets, lock
├── styles.css    the Neumorphic Ink design system
├── brain.js      Socratic engine ported from SocraticBrain.kt
└── app.js        state, rendering, interaction
```

No network calls, no API keys, no telemetry — same as the app.
