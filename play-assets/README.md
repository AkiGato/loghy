# Play Store assets for Loghy

Everything needed for the store listing, plus the one thing that must not go wrong.

---

## Screenshots

Five real screenshots, captured from the running app on a Pixel-class emulator at
**1080 × 1920 (16:9)**.

| File | Shows |
|---|---|
| `01-chat.png` | The companion answering a real idea with a Socratic question |
| `02-swot.png` | The four-angle reality check, strengths section |
| `03-lateral.png` | de Bono tools — Random Word stimulus and the Six Hats |
| `04-hitlist.png` | The 5-step roadmap with hour estimates |
| `05-vault.png` | A note with paper tone and edit history |

**Why 1080 × 1920 and not the phone's native size:** the emulator's real display is
1080 × 2400, which is a **2.22:1** ratio. Play rejects phone screenshots above **2:1**.
Captured at 16:9 so they upload without complaint.

Play requires **2–8** phone screenshots. All five are usable; `01` and `02` are the
strongest openers because they show the app actually doing something.

### Still needed (Play requires these, and they are design work, not code)

- **App icon** — 512 × 512 PNG, 32-bit, no transparency
- **Feature graphic** — 1024 × 500 PNG or JPG, shown at the top of your listing

---

## Store listing text

**App name** (30 chars max)

```
Loghy
```

**Short description** (80 chars max)

```
A private thinking partner that stress-tests your ideas. Offline. No data taken.
```

**Full description** (4000 chars max)

```
Loghy is a quiet thinking partner for people with more ideas than certainty.

Pitch it something — a business, a project, a half-formed hunch — and Loghy tests it
with you instead of flattering you. It asks the uncomfortable question, finds where
the idea leaks, and tells you what to do about it.

WHAT'S INSIDE

• Companion Chat — bounce concepts off a partner who pushes back honestly
• Reality Check — your idea from four plain-language angles: what holds up, where
  it's vulnerable, the outside hurdles, and the hidden upside
• Lateral Thinking — Edward de Bono's tools: Random Word, Provocation, Reversal and
  the Six Thinking Hats
• Hit List — a 5-step roadmap with honest hour estimates, built to stop
  procrastination rather than feed it
• Notes Vault — notes on tactile paper tones, with up to 20 versions of edit history
  you can roll back to

PRIVACY, PROPERLY

Your ideas belong to you.

• Everything is stored on your device. There is no account and no server.
• Nothing you write is used to train any AI. Loghy's engine doesn't learn from you.
• No ads, no trackers, no analytics, no data collection.
• Works fully offline.

Loghy is free. No ads, no subscription, no paywall.

If you want richer answers you can optionally add your own Google Gemini API key —
your key, your account, under your control. Without one, Loghy's built-in engine
works entirely on your phone.

Made by one independent designer for creative people who are tired of tools that
harvest their work.
```

**Category:** Productivity
**Tags:** brainstorming, notes, creativity, productivity
**Contact email:** aki.gato.art@gmail.com
**Privacy policy URL:** see `PLAY_DATA_SAFETY.md`

---

## The signing key — the part worth being careful about

I need to correct something I said earlier: I told you that losing your signing key
means you can never update your app. **That was out of date.**

With **Play App Signing** (the default for new apps since 2021), there are two keys:

| Key | Who holds it | If lost |
|---|---|---|
| **App signing key** | Google holds it | Google has it. Nothing to lose. |
| **Upload key** | You hold it | **Recoverable** — you request a reset from Google |

So losing your upload key is a nuisance involving a support request, not the end of
your app. That is much better than the old world, and I shouldn't have alarmed you.

**Still back it up.** A reset takes days and involves proving who you are. Two copies
in different places — a password manager and an encrypted drive, say — turns a week of
stress into nothing.

### Creating the upload key

Android Studio does this for you: **Build → Generate Signed App Bundle → Create new…**

Or by command line (from the project root):

```bash
keytool -genkeypair -v -keystore loghy-upload.jks -alias upload \
  -keyalg RSA -keysize 2048 -validity 10000
```

It asks for a password and some details. Keep the password with the file.

**Where it must never go:** your repository. `.gitignore` already blocks `*.jks` and
`*.keystore`, so git will refuse — but store it outside the project folder anyway.

### Building the release

```bash
KEYSTORE_PATH=/path/to/loghy-upload.jks \
STORE_PASSWORD=yourpassword \
KEY_PASSWORD=yourpassword \
./gradlew bundleRelease
```

The output is `app/build/outputs/bundle/release/app-release.aab` — Play wants the
`.aab`, not an APK.

**Also keep `app/build/outputs/mapping/release/mapping.txt`** from each release you
ship. R8 renames everything, so without the matching mapping file a crash report from
that version is unreadable.

---

## Submission checklist

- [ ] Google Play developer account ($25, one time)
- [ ] Upload key created and backed up in two places
- [ ] `bundleRelease` built and signed
- [ ] Privacy policy hosted at a public URL
- [ ] Data Safety form filled in (see `PLAY_DATA_SAFETY.md`)
- [ ] App icon 512 × 512
- [ ] Feature graphic 1024 × 500
- [ ] 2–8 phone screenshots (five are in this folder)
- [ ] Short and full description
- [ ] Content rating questionnaire
- [ ] Target audience set to 13+ or 18+, not under-13
