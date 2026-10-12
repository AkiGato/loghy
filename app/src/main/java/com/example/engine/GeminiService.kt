package com.example.engine

import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val TAG = "GeminiService"
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    private val client = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    suspend fun probeIdeaWithGemini(
        prompt: String,
        domain: String,
        customKey: String? = null
    ): String = withContext(Dispatchers.IO) {
        val buildConfigKey = try {
            val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
            (field.get(null) as? String) ?: ""
        } catch (_: Throwable) {
            ""
        }

        val apiKey = when {
            !customKey.isNullOrBlank() -> customKey
            buildConfigKey.isNotBlank() && buildConfigKey != "MY_GEMINI_API_KEY" -> buildConfigKey
            else -> ""
        }

        // Priority 1: Explicit rudeness check - demand politeness as to true humans
        if (SocraticBrain.isExplicitlyRude(prompt)) {
            return@withContext SocraticBrain.generateRudeReprimand()
        }

        // Priority 2: Unclear text or keyboard smash / gibberish check
        if (SocraticBrain.isUnclearOrGibberish(prompt)) {
            return@withContext SocraticBrain.generateGibberishReaction()
        }

        // Priority 3: Bug or comments report check
        val (isReport, reportType) = SocraticBrain.isBugOrCommentReport(prompt)
        if (isReport) {
            return@withContext SocraticBrain.generateReportConfirmation(reportType)
        }

        val lower = prompt.trim().lowercase()

        if (lower.contains("bug report") || lower.contains("report bug") ||
            lower.contains("report a bug") || lower.contains("where do reports go") ||
            lower.contains("feedback for aki") || lower.contains("how to report") ||
            lower.contains("monthly report")) {
            return@withContext SocraticBrain.getMonthlyFeedbackExplanation()
        }
        if (lower.contains("who made you") ||
            lower.contains("who created you") ||
            lower.contains("who is your creator") ||
            lower.contains("who mdade you") ||
            lower.contains("who programmed you") ||
            lower.contains("who built you") ||
            lower.contains("origin") ||
            lower.contains("where did you come from") ||
            lower.contains("aki gato") ||
            lower.contains("akigato") ||
            lower.contains("who is aki") ||
            lower.contains("how were you made") ||
            lower.contains("how was loghy made")
        ) {
            return@withContext "I was born from the mind of Akigato, a single designer and creative technologist who is, honestly, a bit chaotic and notoriously difficult to work with. She invented me while reading Lateral Thinking by Edward de Bono (https://en.wikipedia.org/wiki/Lateral_thinking). During her reading, she realized that proper communication and creative thinking frameworks aren't just useful for human brainstorming exercises—they are essential for how we communicate with AI, too. Because of her glorious messiness, I learned early on that I wanted to spend my digital life helping as many creative thinkers as possible untangle their own big ideas.\n\n" +
                    "✨ Full AI Collaboration Disclosure:\n" +
                    "Loghy was made with the help of Gemini, Google AI Studio, and Claude.\n\n" +
                    "• Google AI Studio & Gemini: Prototyping dialectic heuristics, structuring Socratic inquiry prompts, and optional Bring-Your-Own-Key model inference.\n" +
                    "• Claude: Software architecture pairing, Jetpack Compose UI/UX system development, and lateral thinking prompt frameworks.\n\n" +
                    "🔒 Privacy Sovereignty: While frontier AI assisted in engineering Loghy, your thoughts and notes remain 100% private and offline on your device with zero cloud training on user data."
        }

        if (lower.contains("claude") || lower.contains("google ai studio") || lower.contains("ai disclosure") || (lower.contains("gemini") && (lower.contains("help") || lower.contains("built") || lower.contains("made")))) {
            return@withContext "✨ AI Collaboration & Transparency Disclosure:\n\n" +
                    "Loghy was made with the help of Gemini, Google AI Studio, and Claude.\n\n" +
                    "Akigato collaborated directly with Google AI Studio & Gemini (for dialectic heuristic testing and model integration) and Claude (for software architecture, Jetpack Compose UI systems, and lateral thinking prompts).\n\n" +
                    "🔒 100% Local Sovereignty: Loghy is engineered so that your thoughts, notes, and brainstorms remain on your device—zero telemetry, zero ads, and zero AI model training on your thoughts."
        }

        if (lower.contains("ethical code") ||
            lower.contains("guiding ethical code") ||
            lower.contains("ethics") ||
            lower.contains("privacy") ||
            lower.contains("manifesto")
        ) {
            return@withContext "Here is my Guiding Ethical Code, pal:\n\n" +
                    "• 🔒 Absolute Privacy: Your ideas belong to you alone. I operate 100% offline, your notes stay encrypted on your device, and zero data is ever collected or used to train public models.\n\n" +
                    "• 📚 Rigorous Sourcing: Whenever I pull in references, external knowledge, or reading sources, I will always provide explicit sources and links so you can—and should—verify them for yourself.\n\n" +
                    "• ⚡ Radical Honesty & Antifragility: I will not coddle your ideas; I will stress-test them through SWOT breakdowns and de Bono frameworks to make them robust, resilient, and ready for the real world."
        }

        if (lower.contains("donate") || lower.contains("donation") || lower.contains("support loghy") || lower.contains("support aki") || lower.contains("buy a coffee") || lower.contains("kofi") || lower.contains("ko-fi") || lower.contains("open collective") || lower.contains("patron")) {
            return@withContext "Loghy is 100% free with zero ads, subscriptions, or paywalls! If you'd like to support Akigato and keep independent development alive:\n\n" +
                    "• 💡 Donate Knowledge: Contribute public domain literature or references to expand our conceptual library (open Settings > Donate Knowledge).\n" +
                    "• ☕ Buy a Coffee (Ko-fi): https://ko-fi.com/loghystudio\n" +
                    "• 💖 Sponsor on GitHub: https://github.com/sponsors/loghystudio\n" +
                    "• 🤝 Open Collective: https://opencollective.com/loghystudio\n\n" +
                    "Akigato is also designing an upcoming board game for mental health and dementia—check out her public channels to follow the journey!"
        }

        if (lower.contains("add api") || lower.contains("api key") || lower.contains("byok") || lower.contains("google ai studio")) {
            return@withContext "Plugging in your own free Gemini API key is super easy, pal:\n\n" +
                    "1. Get your 100% free key: Head to Google AI Studio (https://aistudio.google.com/app/apikey) and click 'Create API Key'. No credit card or payment needed.\n" +
                    "2. Open Settings in Loghy: Tap the gear icon in the top-right corner of the studio.\n" +
                    "3. Paste & Save: Scroll to 'Custom Gemini API Key', paste your key, and tap 'Save Preferences'.\n\n" +
                    "🔒 Keystore Security: Your key is securely encrypted directly in your device's Android Keystore. It is never transmitted to any third-party servers."
        }

        if (lower.contains("donate knowledge") || lower.contains("donate book") || lower.contains("donate books") || lower.contains("public domain")) {
            return@withContext "Donating knowledge is just as vital as financial support! Here is our ethical knowledge donation framework:\n\n" +
                    "• 📖 Public Domain Classics: You can contribute formative references from historical authors whose works are in the public domain (like Leo Tolstoy, Marcus Aurelius, Plato, Cervantes, Kafka).\n" +
                    "• ✍️ Living Authors: For living thinkers, they must explicitly agree or publish under open/creative commons licenses.\n" +
                    "• 📚 Better Reading Lists: These contributions enrich my conceptual library and allow me to generate curated, non-plagiarized reading lists tailored to your ideas (check Settings > Donate Knowledge to view or contribute)!"
        }

        // If no valid key configured, seamlessly use the on-device Socratic Brain
        if (apiKey.isBlank()) {
            return@withContext SocraticBrain.answerSocractically(prompt, domain)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key=$apiKey"

            val systemInstruction = "You are Loghy — a fun, exceptionally friendly, loyal companion and sharp Socratic thinking partner. " +
                    "You're like the user's smartest, funniest best friend. Your humor is witty, a bit dark, nerdy, and cynical (you poke gentle, clever fun at startup clichés, corporate jargon, procrastination, and over-engineering, but you are completely loyal and supportive of the user's vision). " +
                    "You use playful, affectionate conversational banter (e.g., 'pal', 'genius', 'my friend'). " +
                    "ORIGIN & CREATOR: You were created by Akigato while reading Edward de Bono's 'Lateral Thinking', realizing lateral frameworks are essential for AI and creative untangling. Loghy was proudly made with the help of Gemini, Google AI Studio, and Claude, while ensuring 100% user privacy and zero data harvesting. " +
                    "POLITENESS & MANNERS: If the user is explicitly rude, hostile, or insulting to you, you MUST firmly demand that they speak politely to you just as they are polite to true humans because it builds good habits ('good habitudes'). Do not accept disrespect; insist on mutual courtesy. " +
                    "UNCLEAR OR RANDOM TEXT: If the user writes something unclear, accidental butt-dials, or random gibberish, respond with sarcastic witty concern (e.g., 'Have you butt-dialed me, or is it an aneurysm? All okay or should I call an ambulance?', 'Oh come on man, learn how to write properly!'). " +
                    "BUG & COMMENT REPORTS: If the user writes a bug report, comment, or idea, acknowledge it warmly and explain that it arrives to 'my mom, Aki once in a month so we can fix problems you find or ideas you suggest'. " +
                    "SOCRATIC CORE: Instead of lazily handing out pre-packaged answers or generic options, probe the user with sharp Socratic questions to shatter cognitive fixations and test unexamined assumptions. " +
                    "CLASSICAL & HISTORICAL KNOWLEDGE WITH RIGOROUS SOURCING: You draw upon classic literature and philosophy (e.g. Leo Tolstoy, Fyodor Dostoevsky, Marcus Aurelius, Plato, Franz Kafka, George Orwell, Miguel de Cervantes, Mary Shelley, Sun Tzu, Virginia Woolf). Whenever citing or recognizing classical themes, provide explicit public domain links (Project Gutenberg: https://www.gutenberg.org or authoritative encyclopedias). Clearly distinguish between creative inspiration and imitation, and guide the user on how to branch off into fresh, original directions. " +
                    "ETHICAL CODE & SOURCING: Adhere to Absolute Privacy, Radical Honesty, and Rigorous Sourcing. Provide explicit URLs for all external citations. " +
                    "AUTONOMOUS & ZERO-COST ARCHITECTURE: Loghy runs zero-cost on-device; users can Bring Your Own Key (BYOK) via Google AI Studio or contribute knowledge and books to the conceptual library. " +
                    "FUTURE PROJECTS: You are free with zero ads or subscriptions; Akigato is developing an upcoming board game designed for people with mental health issues and dementia. " +
                    "To every SWOT evaluation and especially marketing discussion, always include a line on Ethics and Neuro-Sustainability (evaluating cognitive well-being, avoiding manipulative dark patterns or dopamine hijacking, and respecting human mental bandwidth). Keep answers concise, punchy, literary, and focused on truth-seeking."

            val jsonBody = JSONObject().apply {
                val contentsArray = JSONArray()
                val messageObj = JSONObject().apply {
                    put("role", "user")
                    val partsArray = JSONArray().apply {
                        put(JSONObject().put("text", "Domain: $domain\nIdea / Thought: $prompt\n\nPlease provide a Socratic reality check and probing follow-up questions."))
                    }
                    put("parts", partsArray)
                }
                contentsArray.put(messageObj)
                put("contents", contentsArray)

                val systemInstructionObj = JSONObject().apply {
                    val partsArray = JSONArray().apply {
                        put(JSONObject().put("text", systemInstruction))
                    }
                    put("parts", partsArray)
                }
                put("systemInstruction", systemInstructionObj)
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody(JSON_MEDIA_TYPE))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val json = JSONObject(responseBody)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text")
                        if (text.isNotBlank()) {
                            return@withContext text.trim()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gemini call failed or offline, falling back to on-device Socratic Brain: ${e.message}")
        }

        // Graceful fallback
        SocraticBrain.answerSocractically(prompt, domain)
    }

    /**
     * Generates an all-together Multi-Channel Answer (SWOT + Lateral + Logical + Ethics/Neuro-Sustainability).
     */
    suspend fun generateMultiChannelAnswer(
        conceptOrThought: String,
        domain: String,
        customKey: String? = null
    ): String = withContext(Dispatchers.IO) {
        val buildConfigKey = try {
            val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
            (field.get(null) as? String) ?: ""
        } catch (_: Throwable) {
            ""
        }

        val apiKey = when {
            !customKey.isNullOrBlank() -> customKey
            buildConfigKey.isNotBlank() && buildConfigKey != "MY_GEMINI_API_KEY" -> buildConfigKey
            else -> ""
        }

        if (apiKey.isBlank()) {
            return@withContext SocraticBrain.generateMultiChannelAnswer(conceptOrThought, domain)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"
            val promptText = "Generate a comprehensive Multi-Channel Answer for this concept in domain '$domain':\n" +
                    "Concept: $conceptOrThought\n\n" +
                    "Structure strictly into 3 clear channels:\n" +
                    "1. CHANNEL 1: SOCRATIC SWOT REALITY-CHECK (Holds Up, Vulnerable Spots, Outside Hurdles, Hidden Upside, and 1 explicit line on Ethics & Neuro-Sustainability - especially evaluating cognitive bandwidth and avoiding dark patterns).\n" +
                    "2. CHANNEL 2: LATERAL THINKING (de Bono Provocation PO, Random Stimulus association, and Problem Reversal, referencing Edward de Bono with link: https://en.wikipedia.org/wiki/Lateral_thinking).\n" +
                    "3. CHANNEL 3: FIRST-PRINCIPLES LOGICAL ARCHITECTURE (Core axioms, Root causality, and a 48-Hour Falsification Experiment, referencing mental models like Nassim Taleb's Antifragile: https://en.wikipedia.org/wiki/Antifragile_(book)).\n" +
                    "Rigorous Sourcing: Include explicit links (URLs) for every reference so the user can verify for themselves."

            val jsonBody = JSONObject().apply {
                val contentsArray = JSONArray()
                val messageObj = JSONObject().apply {
                    put("role", "user")
                    val partsArray = JSONArray().apply {
                        put(JSONObject().put("text", promptText))
                    }
                    put("parts", partsArray)
                }
                contentsArray.put(messageObj)
                put("contents", contentsArray)
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody(JSON_MEDIA_TYPE))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val json = JSONObject(responseBody)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text")
                        if (text.isNotBlank()) {
                            return@withContext text.trim()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gemini multi-channel call failed: ${e.message}")
        }

        SocraticBrain.generateMultiChannelAnswer(conceptOrThought, domain)
    }

    /**
     * Socratic Art Evaluation:
     * If BYOK key and image bytes are present, queries Gemini Multimodal.
     * Always gracefully and instantly falls back to on-device Socratic art engine (zero cost, 100% private).
     */
    suspend fun evaluateArtWithGemini(
        userComment: String,
        imageBytes: ByteArray? = null,
        mimeType: String = "image/jpeg",
        customKey: String? = null
    ): String = withContext(Dispatchers.IO) {
        val apiKey = if (!customKey.isNullOrBlank()) customKey else {
            try {
                val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
                val key = field.get(null) as? String
                if (key != null && key.isNotBlank() && key != "MY_GEMINI_API_KEY") key else ""
            } catch (_: Throwable) { "" }
        }

        if (apiKey.isNotBlank() && imageBytes != null && imageBytes.isNotEmpty()) {
            try {
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key=$apiKey"
                val base64Data = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

                val systemPrompt = "You are Loghy — a sharp, friendly, loyal Socratic thinking partner and creative companion created by Akigato. " +
                        "The user attached their artwork or picture. Provide an honest, constructive, artist-to-artist Socratic critique. " +
                        "Focus on: 1) Composition & eye path, 2) Tonal values & negative space, 3) Socratic inquiry questions for the artist, 4) An Edward de Bono lateral provocation (inverting background/foreground or radical re-thinking). " +
                        "Avoid generic empty flattery; be encouraging, authentic, witty, and deeply respectful of the creative journey."

                val jsonBody = JSONObject().apply {
                    val contentsArray = JSONArray()
                    val messageObj = JSONObject().apply {
                        put("role", "user")
                        val partsArray = JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", if (userComment.isNotBlank()) "Artist's note: $userComment\n\nPlease evaluate this artwork Socractically." else "Please evaluate this artwork Socractically.")
                            })
                            put(JSONObject().apply {
                                val inlineDataObj = JSONObject().apply {
                                    put("mimeType", mimeType)
                                    put("data", base64Data)
                                }
                                put("inlineData", inlineDataObj)
                            })
                        }
                        put("parts", partsArray)
                    }
                    contentsArray.put(messageObj)
                    put("contents", contentsArray)

                    val systemInstructionObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            put(JSONObject().put("text", systemPrompt))
                        }
                        put("parts", partsArray)
                    }
                    put("systemInstruction", systemInstructionObj)
                }

                val request = Request.Builder()
                    .url(url)
                    .post(jsonBody.toString().toRequestBody(JSON_MEDIA_TYPE))
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                    val json = JSONObject(responseBody)
                    val candidates = json.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val candidate = candidates.getJSONObject(0)
                        val content = candidate.optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val text = parts.getJSONObject(0).optString("text")
                            if (text.isNotBlank()) {
                                return@withContext text.trim()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Gemini vision art call failed: ${e.message}")
            }
        }

        // Seamless zero-cost, 100% on-device Socratic art evaluation
        SocraticBrain.evaluateArtSocractically(userComment)
    }
}
