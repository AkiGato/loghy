# Google Play — Data Safety form answers for Loghy

Copy these answers into **Play Console → App content → Data safety**.

Google re-checks these against what your app actually does. A wrong answer here is
one of the most common reasons indie apps get suspended — usually by accident, by
claiming "we collect nothing" while the app asks for the microphone. These answers
match Loghy's real behaviour.

---

## Section 1 — Data collection and security

| Question | Answer |
|---|---|
| Does your app collect or share any of the required user data types? | **No** |
| Is all of the user data collected by your app encrypted in transit? | *(skipped — only appears if you answer Yes above)* |
| Do you provide a way for users to request that their data is deleted? | **No** *(nothing is collected; uninstalling removes everything)* |

### Why "No" is correct here

Google defines **"collected"** as data your app *transmits off the device*. It does
**not** include data that only ever sits in local storage.

Loghy stores a lot on the phone — notes, ideas, conversations — but transmits none of
it to us. There is no server, no account and no analytics. So the honest answer is
**No**.

### The microphone — why it does not change the answer

This is the part people get wrong.

Loghy uses Android's built-in speech-to-text. That service does send audio to Google,
**but it is Google's own system service doing it, not your app**, and your app never
receives or stores the audio — only the text that comes back.

Google's own guidance is that you declare data *your app* collects. Using a platform
service like `SpeechRecognizer` is not declared as audio collection by your app.

> **If a reviewer ever queries the microphone permission**, the answer is: "The
> RECORD_AUDIO permission is used solely for dictation via the Android
> `SpeechRecognizer` API while the user holds the microphone button. The app does not
> record, store or transmit audio itself. This is disclosed in the privacy policy."

Keep the microphone disclosure in your privacy policy regardless — it is there, and it
is what makes this defensible.

### The optional API key — also does not change the answer

If a user adds their own Gemini key, the request goes **from their device to Google
under their own key**. You are not the recipient and you never see it. It is disclosed
in the privacy policy, which is the right place for it.

---

## Section 2 — Other declarations in Play Console

| Item | Answer |
|---|---|
| Privacy policy URL | *(your hosted PRIVACY.md — see below)* |
| Ads | **No ads** |
| App access | **All functionality is available without special access** (no login) |
| Content rating | Complete the questionnaire — expect **Everyone / PEGI 3** |
| Target audience | **18+** or **13+**. Avoid selecting under-13, which triggers Families Policy and much stricter rules |
| Government app | No |
| Financial features | None |
| Health apps | None |
| Data safety — "Data types" | Leave everything unticked, consistent with "No" above |

### Permissions declaration

You will be asked to justify sensitive permissions. Loghy requests four:

| Permission | Justification to give |
|---|---|
| `RECORD_AUDIO` | Voice dictation for composing notes and messages, active only while the user holds the mic button. |
| `POST_NOTIFICATIONS` | Local reminders for tasks the user schedules themselves. |
| `INTERNET` | Only used if the user supplies their own AI API key. No other network use. |
| `VIBRATE` | Haptic feedback on interaction. |

**You no longer declare `SCHEDULE_EXACT_ALARM`** — it was removed, which avoids a
restricted-permission review you would most likely have lost.

---

## Section 3 — Hosting the privacy policy

Play requires a **publicly reachable URL**. Since your repo is public, GitHub Pages
gives you one free in about two minutes:

1. Rename `PRIVACY.md` to `index.md` inside a new `docs/` folder, **or** just leave it
   and use the direct file link below
2. Repo → **Settings** → **Pages**
3. Source: **Deploy from a branch** → Branch: **main** → Folder: **/docs** → Save
4. Wait ~1 minute. Your URL will be:
   `https://akigato.github.io/loghy/`

**Quickest alternative with no setup:** paste this direct link into Play Console —

```
https://github.com/AkiGato/loghy/blob/main/PRIVACY.md
```

It is publicly reachable and acceptable. GitHub Pages just looks more professional.

> **If you make the repo private**, both links die and Play will flag your listing.
> In that case host the policy elsewhere — a free Notion public page, Carrd, or
> GitHub Gist all work.

---

## Before you submit — quick check

- [ ] Privacy policy is reachable in a private browser window (not just while logged in)
- [ ] Policy mentions the microphone and the optional API key
- [ ] Data safety says **No** collection
- [ ] No `SCHEDULE_EXACT_ALARM` in the manifest
- [ ] App signing key backed up in two places
- [ ] Screenshots uploaded (2–8 phone screenshots)
- [ ] Short description (80 chars) and full description written
