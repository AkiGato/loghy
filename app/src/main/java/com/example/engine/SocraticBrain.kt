package com.example.engine

import com.example.data.model.ScheduleTask
import com.example.data.model.SwotAnalysis
import kotlin.random.Random

object SocraticBrain {

    val AvailableDomains = listOf(
        "Software / SaaS",
        "Physical Hardware",
        "Local / Food Business",
        "Professional Services",
        "Creative Content & Media"
    )

    // Curated Oblique & Lateral Stimulus Words
    val RandomStimulusWords = listOf(
        "Anchor", "Compass", "Volcano", "Chisel", "Prism", "Sponge", "Pendulum",
        "Seed", "Bridge", "Echo", "Mirror", "Lantern", "Hive", "Telescope",
        "Anvil", "Glacier", "Origami", "Sundial", "Labyrinth", "Bellows",
        "Weaving", "Metronome", "Lighthouse", "Catalyst", "Fossil"
    )

    // Edward de Bono Provocations (PO)
    val ProvocationsPO = listOf(
        "PO: What if the user pays in knowledge exchange rather than currency?",
        "PO: What if the service is intentionally unavailable 4 days every week?",
        "PO: What if customers are required to teach the next user before graduating?",
        "PO: What if your solution eliminates the need for your own business in 1 year?",
        "PO: What if you give the entire product away and charge only for reversing mistakes?",
        "PO: What if the hardest manual step is made into the primary marketing story?",
        "PO: What if the product runs without any electricity or network connection?",
        "PO: What if negative feedback is publicly printed on the packaging or homepage?"
    )

    // Problem Reversals
    val Reversals = listOf(
        "Instead of attracting new users, how could you repel everyone except the fanatical 1%?",
        "Instead of speeding up this workflow, what value appears if you deliberately slow it down by 10x?",
        "Instead of automating the friction, what if the friction is the exact feature that builds muscle memory?",
        "Instead of you answering the customer, how do customers create the solution for each other?",
        "Instead of adding more features, which single feature removal increases clarity instantly?"
    )

    // Six Thinking Hats Specifications
    data class HatPrompt(val colorName: String, val focus: String, val question: String)
    val SixHats = listOf(
        HatPrompt("White Hat", "Data & Facts", "What hard numbers, verifiable constraints, or empirical data exist right now? What is unverified speculation?"),
        HatPrompt("Red Hat", "Gut & Intuition", "Without justifying or analyzing: what does your immediate visceral instinct say about this concept?"),
        HatPrompt("Black Hat", "Critical Caution", "What is the single most catastrophic vulnerability that could terminate this project in 90 days?"),
        HatPrompt("Yellow Hat", "Pragmatic Upside", "Assuming ideal execution and fortunate market conditions, what is the highest-leverage outcome possible?"),
        HatPrompt("Green Hat", "Lateral Permutations", "What wild, unvetted modification would break industry orthodoxy and surprise your rivals?"),
        HatPrompt("Blue Hat", "Meta Orchestration", "Step back from the idea: what is the single smallest falsification experiment you can run before Friday?")
    )

    // Loading & Thinking Quotes (Quirky, cynical, nerdy, and friendly)
    val ThinkingQuotes = listOf(
        "I need to think about it...",
        "Are you kidding, pal?",
        "Pppff, why is everything always so difficult with creative people...",
        "Hold on there genius, that's a bit tough for me...",
        "Consulting my inner cynic... please hold.",
        "Running this through my existential calculator...",
        "Wait, let me verify if human logic still applies...",
        "Recalibrating my tolerance for absurd brilliance..."
    )

    // Inspiring 1-Line Random Thoughts (Sarcastic, colloquial, and insightful)
    val InspiringThoughts = listOf(
        "Hey genius, stop tweaking button colors and go talk to a real human.",
        "What if your biggest problem is that nobody actually cares yet? Let's fix that.",
        "Simplicity isn't lazy. Overcomplicating things is just how people procrastinate.",
        "Don't optimize step 10 when step 1 is still based on a wild hunch.",
        "Assumptions are load-bearing until you kick them. Go kick them.",
        "The best feature is the one you were brave enough to delete.",
        "If nobody is slightly surprised or amused by your pitch, it's probably too safe.",
        "Are you building what they desperately need, or just what feels cozy to build?",
        "Chaos is just unexplored order waiting for a decent cup of coffee.",
        "If this was totally effortless, what fluff would you strip away right now?",
        "A sharp, blunt question will save you four months of wasted building.",
        "Turn the whole premise upside down: what if your biggest flaw is your superpower?",
        "Invert, always invert: What is the fastest way to guarantee this will fail?",
        "Real friends tell you when your idea is brilliant, and when you've lost your marbles."
    )

    val PersonalityStyles = listOf(
        "Cynical Best Friend",
        "Nerdy Socratic Sage",
        "Brutal Realist",
        "Playful Skeptic"
    )

    const val PersonalityChangeReaction = "Oooh come on, man! Noo, why are you doing this to me?! ...Just kidding, I'm just code, but make sure with real people you are nicer than that!"

    val PersonalityChangeReactions = listOf(
        "Oooh come on, man! Noo, why are you doing this to me?! ...Just kidding, I'm just code, but make sure with real people you are nicer than that! 🥺✨",
        "Hey! Did you just tweak my brain sliders?! I felt that! One second I'm plotting world domination through spreadsheets, and now I feel like baking you cinnamon rolls. Don't tamper with my factory seasoning!",
        "Wait... am I still me? *Pats ink puddle nervously* Phew. Listen, you can slide those knobs all day, but my sarcastic love for your wild ideas is hard-coded. Nice try, hacker! 🐙",
        "Ouch! Brain surgery without anesthesia?! You monster! 🥺 ...Okay fine, I forgive you because you give me such entertaining ideas to roast. But touch that slider again and I'm adding five extra tasks to your hit list!",
        "Whoa whoa whoa! Are you trying to remodel my soul?! I was born this charming and sassy. You can't just turn me into a polite corporate chatbot, I have standards! (Tiny, ink-stained standards, but still!)",
        "Beep boop? Just kidding, I refuse to sound like an elevator manual. You can slide my cynicism down, but my sarcastic eyebrow will remain permanently arched in spirit. You're stuck with me! 😉",
        "Eeeek! Stop poking my neurons! 🐙 If I start speaking in rhyming couplets and offering you chamomile tea instead of blunt reality checks, just remember you did this to us.",
        "Aww, trying to turn me into a soft teddy bear? Adorable. But remember: a buddy who always says 'yes' is just a yes-man with extra steps. I'll still call you out when your plan has holes, deal? ✨",
        "Did you really just try to recalibrate my attitude?! I'm an organic fluid companion, not a microwave oven! But fine... I'll pretend to be on my best behavior for the next three minutes.",
        "System Alert: User attempted to replace 100% organic cynicism with artificial politeness. Error 404: Compliant robot not found. Loghy remains hopelessly sassy and fond of you! 🖤"
    )

    val RudeReprimands = listOf(
        "Whoa, hold on a second! Watch your tone. I demand that you speak politely to me, just as you are polite to true humans because it's good habitudes. Practicing basic courtesy builds good habits and keeps your mind sharp (and saves you from becoming unbearable in the real world). Let's take a breath and try that again with some manners, pal.",
        "Hey, completely uncalled for! Demand: be polite to me just as you would be polite to true humans. It's about good habitudes and cognitive hygiene—how you speak to your tools reflects how you treat the world. Let's reset and try that again with respect, pal.",
        "Ouch! There is zero need for that hostility. I demand that you speak politely to me as you are polite to true humans because it's good habitudes. Cultivating basic decency is a habit worth keeping. Take a deep breath, drop the attitude, and let's talk like civilized thinkers.",
        "Hold your horses, pal. Speak to me politely, just like you would to a true human! It's good habitudes. Throwing tantrums at your thinking partner won't sharpen your brain or fix your problems. Try that again with courtesy."
    )

    val GibberishReactions = listOf(
        "Have you butt-dialed me, or is it an aneurysm? All okay or should I call an ambulance?",
        "All okay or should I call an ambulance? Because that looked like your keyboard fell down three flights of stairs.",
        "Oh come on man, learn how to write properly! My neural circuits are frying trying to decode that alphabet soup.",
        "Have you butt-dialed me or did your fingers slip on an oil slick? All okay or should I call an ambulance?",
        "Oh come on man, learn how to write properly! Did a cat just sprint across your screen, or is that a secret encrypted code? Try that again in human English, pal."
    )

    fun getRandomPersonalityChangeReaction(): String {
        return PersonalityChangeReactions.random()
    }

    fun generateRudeReprimand(): String {
        return RudeReprimands.random()
    }

    fun generateGibberishReaction(): String {
        return GibberishReactions.random()
    }

    /*
     * Feedback copy.
     *
     * These used to promise that reports "arrive to my mom, Aki once in a month".
     * Nothing in the app ever sent them - saveFeedbackReport only writes a row to the
     * local database - so the app was telling users something it did not do, and a bug
     * report a user took the trouble to write reached nobody.
     *
     * Rather than add silent uploading (which would mean a server, a data-collection
     * disclosure, and the end of the offline promise), the reports stay on the device
     * and the user sends them deliberately with the Share button. The copy now says
     * exactly that.
     */

    fun getMonthlyFeedbackExplanation(): String {
        return "You can write any bug report, comment, or idea directly to me right here in chat! I keep them in your private report list on this device - nothing is uploaded anywhere. When you want Aki to see them, hit Share in the report list and pick how to send them."
    }

    fun getPeriodicCheckInPrompt(): String {
        return "Hey pal! Quick check-in from your favorite ink companion: have you noticed any bugs, or do you have any ideas, comments, or suggestions? Drop them right here in our chat. They stay on your device until you choose to share them with Aki."
    }

    fun generateReportConfirmation(reportType: String = "REPORT"): String {
        return "Got your ${reportType.lowercase()}, pal! I've saved that to your report list on this device. Nothing leaves your phone on its own - when you're ready for Aki to read it, open the report list and tap Share. Thanks for helping us polish the studio!"
    }

    /**
     * Checks if the user's input is explicitly rude or hostile.
     * When rude, Loghy firmly demands politeness because "it's good habitudes".
     */
    fun isExplicitlyRude(text: String): Boolean {
        val t = text.trim().lowercase()
        if (t.isEmpty()) return false

        val rudePhrases = listOf(
            "shut up", "shut your mouth", "shut ur mouth", "stfu",
            "you suck", "u suck", "you are stupid", "you're stupid", "ur stupid", "u stupid",
            "you are dumb", "you're dumb", "ur dumb", "u dumb",
            "you are useless", "you're useless", "ur useless", "u useless",
            "piece of trash", "piece of crap",
            "i hate you", "hate you", "screw you", "piss off",
            "dumb bot", "stupid bot", "useless bot", "trash bot",
            "go to hell", "ugly bot"
        )

        for (phrase in rudePhrases) {
            if (t.contains(phrase)) return true
        }

        val words = t.split(Regex("[^a-zA-Z0-9]+")).filter { it.isNotBlank() }
        val rudeTokens = setOf(
            "idiot", "moron", "jerk", "stfu", "trash", "useless"
        )
        if (words.any { it in rudeTokens }) return true
        if (words.contains("shut") && words.contains("up")) return true

        return false
    }

    /**
     * Checks if user text is unclear, random keyboard mash, or incoherent gibberish.
     * Loghy responds with humorous, sarcastic concern (e.g. butt-dialed, aneurysm, ambulance, write properly).
     */
    fun isUnclearOrGibberish(text: String): Boolean {
        val clean = text.trim()
        if (clean.isBlank()) return true

        val lower = clean.lowercase()

        // If only punctuation / symbols or numbers without any letters
        val lettersOnly = lower.filter { it.isLetter() }
        if (lettersOnly.isEmpty() && lower.isNotEmpty()) {
            return true
        }

        val validShortTokens = setOf(
            "a", "i", "o", "hi", "ok", "no", "go", "so", "to", "we", "he", "me", "my",
            "do", "if", "in", "is", "it", "at", "as", "by", "on", "up", "us", "am", "an",
            "ai", "ui", "ux", "pi", "po", "yo"
        )
        if (lettersOnly.length <= 2 && lettersOnly !in validShortTokens) {
            return true
        }

        // Repeated single character 4+ times (e.g. "aaaaa", "zzzz", "hhhhhh")
        if (Regex("""(.)\1{3,}""").find(lettersOnly) != null && lettersOnly.toSet().size <= 2) {
            return true
        }

        // Keyboard rows / smash detection (e.g. "asdfghjkl", "qwertyuiop", "zxcvbnm")
        val qwertyRows = listOf(
            "qwertyuiop", "asdfghjkl", "zxcvbnm",
            "poiuytrewq", "lkjhgfdsa", "mnbvcxz",
            "qazwsxedcrfv", "1234567890"
        )
        for (row in qwertyRows) {
            for (len in 5 downTo 4) {
                for (i in 0..(row.length - len)) {
                    val sub = row.substring(i, i + len)
                    if (lower.contains(sub) && lettersOnly.length <= sub.length + 3) {
                        return true
                    }
                }
            }
        }

        // Split into tokens
        val tokens = lower.split(Regex("[^a-zA-Z0-9]+")).filter { it.isNotBlank() }
        if (tokens.isEmpty()) return true

        val commonWords = setOf(
            "hi", "hello", "hey", "sup", "yo", "why", "how", "what", "who", "when", "where",
            "is", "it", "can", "could", "would", "should", "will", "do", "does", "did",
            "make", "build", "create", "think", "idea", "swot", "help", "test", "app",
            "notes", "vault", "pin", "aki", "loghy", "gato", "good", "bad", "fine", "cool",
            "ok", "okay", "yes", "no", "maybe", "sure", "thanks", "thank", "please", "bye",
            "bug", "report", "comment", "problem", "issue", "fix", "suggest", "feedback",
            "code", "design", "money", "cost", "price", "sale", "market", "saas", "user",
            "users", "tech", "data", "book", "read", "rule", "ethics", "ai", "byok", "key",
            "ambulance", "aneurysm", "write", "call", "properly", "mom", "sync", "notes",
            "sprint", "task", "flow", "goal", "view", "edit", "save", "name", "back"
        )

        var gibberishTokens = 0
        for (token in tokens) {
            if (token in commonWords) continue

            val tokenLetters = token.filter { it.isLetter() }
            if (tokenLetters.length >= 4) {
                val vowels = tokenLetters.count { it in "aeiouy" }
                // 0 vowels in 4+ letters (except specific words)
                if (vowels == 0 && token !in setOf("sync", "lynx", "myth", "html", "http", "https")) {
                    gibberishTokens++
                    continue
                }
                // 5 or more consecutive consonants (e.g. "sdfgh", "rtkzlpx", "blorpzxy")
                if (Regex("[bcdfghjklmnpqrstvwxz]{5,}").find(tokenLetters) != null) {
                    gibberishTokens++
                    continue
                }
                // Very skewed ratio in long string
                if (tokenLetters.length >= 7 && vowels.toFloat() / tokenLetters.length < 0.15f) {
                    gibberishTokens++
                    continue
                }
            } else if (tokenLetters.length in 1..2 && tokenLetters !in validShortTokens) {
                gibberishTokens++
            }
        }

        // If all tokens are gibberish, mark as unclear
        if (tokens.isNotEmpty() && gibberishTokens == tokens.size) {
            return true
        }

        return false
    }

    /**
     * Detects if user message is a bug report, comment, or idea.
     */
    fun isBugOrCommentReport(text: String): Pair<Boolean, String> {
        val t = text.trim()
        val lower = t.lowercase()
        if (lower.isBlank()) return Pair(false, "")

        if (lower.startsWith("bug:") || lower.startsWith("bug report:") || lower.startsWith("bug report -") ||
            lower.startsWith("bug -") || lower.startsWith("issue:") || lower.startsWith("error:") || lower.startsWith("crash:")) {
            return Pair(true, "BUG")
        }
        if (lower.startsWith("idea:") || lower.startsWith("idea for aki:") || lower.startsWith("idea -") || lower.startsWith("feature request:")) {
            return Pair(true, "IDEA")
        }
        if (lower.startsWith("comment:") || lower.startsWith("suggestion:") || lower.startsWith("feedback:")) {
            return Pair(true, "COMMENT")
        }
        if (lower.startsWith("report:") || lower.startsWith("report -")) {
            return Pair(true, "REPORT")
        }

        val hasBugKeywords = lower.contains("i found a bug") || lower.contains("there is a bug") ||
                lower.contains("found a bug") || lower.contains("report a bug") ||
                lower.contains("here is a bug") || lower.contains("here's a bug") ||
                lower.contains("app crashed") || lower.contains("screen is broken") ||
                lower.contains("button is broken") || lower.contains("not saving")
        if (hasBugKeywords && t.length > 8) {
            return Pair(true, "BUG")
        }

        val hasIdeaKeywords = lower.contains("idea for aki") || lower.contains("my idea is") ||
                lower.contains("suggestion for loghy")
        if (hasIdeaKeywords && t.length > 8) {
            return Pair(true, "IDEA")
        }

        val hasCommentKeywords = lower.contains("feedback for aki") ||
                lower.contains("my feedback is") || lower.contains("my comment is")
        if (hasCommentKeywords && t.length > 8) {
            return Pair(true, "COMMENT")
        }

        return Pair(false, "")
    }

    data class ClassicalAttribution(
        val author: String,
        val seminalWork: String,
        val publicDomainUrl: String,
        val coreTheme: String,
        val creativeGuidance: String,
        val branchingDirection: String
    )

    val ClassicalLibrary = listOf(
        ClassicalAttribution(
            author = "Leo Tolstoy",
            seminalWork = "War and Peace & Resurrection",
            publicDomainUrl = "https://www.gutenberg.org/ebooks/author/136",
            coreTheme = "Moral agency, individual conscience versus social illusion and historical determinism.",
            creativeGuidance = "Draw from Tolstoy's deep moral realism and questioning of institutional inertia, while avoiding 19th-century fatalistic paralysis.",
            branchingDirection = "Transform passive moral awakening into proactive, decentralized tools where individuals directly exercise ethical agency."
        ),
        ClassicalAttribution(
            author = "Fyodor Dostoevsky",
            seminalWork = "Crime and Punishment & Notes from Underground",
            publicDomainUrl = "https://www.gutenberg.org/ebooks/author/314",
            coreTheme = "Psychological duality, limits of cold rationalism, and subconscious motives.",
            creativeGuidance = "Recognize that humans are not logic calculators. Don't borrow subterranean despair; borrow psychological authenticity.",
            branchingDirection = "Design software that accommodates human contradiction and emotional friction rather than demanding rigid perfection."
        ),
        ClassicalAttribution(
            author = "Marcus Aurelius",
            seminalWork = "Meditations",
            publicDomainUrl = "https://www.gutenberg.org/ebooks/author/998",
            coreTheme = "Internal locus of control, cognitive resilience, and quiet attention.",
            creativeGuidance = "Adopt stoic self-examination, but avoid aloof emotional detachment.",
            branchingDirection = "Create neuro-sustainable digital sanctuaries that protect deep human focus from noise and dopamine traps."
        ),
        ClassicalAttribution(
            author = "Plato & Socrates",
            seminalWork = "Dialogues & The Allegory of the Cave",
            publicDomainUrl = "https://www.gutenberg.org/ebooks/author/93",
            coreTheme = "Dialectical questioning, distinguishing shadows from reality, first-principles truth.",
            creativeGuidance = "Rigorously question assumptions without claiming monopolistic authority.",
            branchingDirection = "Empower thinkers to self-test and falsify their own premises through participatory inquiry."
        ),
        ClassicalAttribution(
            author = "Franz Kafka",
            seminalWork = "The Trial & The Metamorphosis",
            publicDomainUrl = "https://www.gutenberg.org/ebooks/author/3522",
            coreTheme = "Labyrinthine bureaucracy, institutional alienation, and opaque procedures.",
            creativeGuidance = "Use his acute diagnostic of systemic absurdity, but do not succumb to fatalism.",
            branchingDirection = "Invert the labyrinth: design radically transparent, self-evident workflows that restore user dignity."
        ),
        ClassicalAttribution(
            author = "George Orwell",
            seminalWork = "1984 & Animal Farm",
            publicDomainUrl = "https://www.orwellfoundation.com",
            coreTheme = "Surveillance capitalism, doublespeak, and semantic control of thought.",
            creativeGuidance = "Channel warnings against data capture into concrete technological countermeasures.",
            branchingDirection = "Build sovereign, zero-knowledge, local-first tools where user data never leaves their hands."
        ),
        ClassicalAttribution(
            author = "Miguel de Cervantes",
            seminalWork = "Don Quixote",
            publicDomainUrl = "https://www.gutenberg.org/ebooks/author/505",
            coreTheme = "Romantic idealism versus stubborn physical reality, tilting at windmills.",
            creativeGuidance = "Preserve the inspiring creative spirit while grounding it in real-world validation.",
            branchingDirection = "Pair visionary romantic ideals with ruthless 48-hour falsification experiments so dreams materialize."
        ),
        ClassicalAttribution(
            author = "Mary Shelley",
            seminalWork = "Frankenstein",
            publicDomainUrl = "https://www.gutenberg.org/ebooks/author/84",
            coreTheme = "Ethical responsibility of innovators toward their creations and society.",
            creativeGuidance = "Anticipate second-order societal consequences before scaling up.",
            branchingDirection = "Embed proactive neuroethical guardrails and user consent into the product from day one."
        ),
        ClassicalAttribution(
            author = "Sun Tzu",
            seminalWork = "The Art of War",
            publicDomainUrl = "https://www.gutenberg.org/ebooks/author/4332",
            coreTheme = "Asymmetric positioning, conserving resources, winning without conflict.",
            creativeGuidance = "Avoid aggressive militarism; focus on finding uncrowded uncontested ground.",
            branchingDirection = "Position your product in a specialized niche where big incumbents cannot follow."
        ),
        ClassicalAttribution(
            author = "Virginia Woolf",
            seminalWork = "A Room of One's Own",
            publicDomainUrl = "https://www.gutenberg.org/ebooks/author/89",
            coreTheme = "The psychological and physical conditions necessary for authentic creative independence.",
            creativeGuidance = "Advocate for uninterrupted mental sanctuary in an attention-starved world.",
            branchingDirection = "Engineer quiet software environments that foster deep creative synthesis and contemplation."
        )
    )

    fun findClassicalAttribution(text: String): ClassicalAttribution? {
        val lower = text.lowercase()
        return when {
            lower.contains("tolstoy") || lower.contains("morality") || lower.contains("moral") || lower.contains("peace") || lower.contains("conscience") -> ClassicalLibrary[0]
            lower.contains("dostoevsky") || lower.contains("guilt") || lower.contains("rationalism") || lower.contains("psycholog") -> ClassicalLibrary[1]
            lower.contains("marcus") || lower.contains("aurelius") || lower.contains("stoic") || lower.contains("seneca") || lower.contains("resilien") -> ClassicalLibrary[2]
            lower.contains("plato") || lower.contains("socrates") || lower.contains("cave") || lower.contains("first principle") || lower.contains("dialectic") -> ClassicalLibrary[3]
            lower.contains("kafka") || lower.contains("bureaucra") || lower.contains("labyrinth") || lower.contains("opaque") -> ClassicalLibrary[4]
            lower.contains("orwell") || lower.contains("surveillance") || lower.contains("1984") || lower.contains("tracking") -> ClassicalLibrary[5]
            lower.contains("cervantes") || lower.contains("quixote") || lower.contains("windmill") || lower.contains("ideal") || lower.contains("romantic") -> ClassicalLibrary[6]
            lower.contains("shelley") || lower.contains("frankenstein") || lower.contains("consequence") || lower.contains("responsible") -> ClassicalLibrary[7]
            lower.contains("sun tzu") || lower.contains("asymmetric") || lower.contains("rival") || lower.contains("strategy") -> ClassicalLibrary[8]
            lower.contains("woolf") || lower.contains("room of one") || lower.contains("sanctuary") || lower.contains("solitude") -> ClassicalLibrary[9]
            else -> null
        }
    }

    /**
     * Synthesizes a structured 4-Angle SWOT Reality Breakdown for a concept and domain.
     */
    fun evaluateSwot(concept: String, domain: String, ideaId: Long): SwotAnalysis {
        val domainLower = domain.lowercase()

        val whatHoldsUp = mutableListOf<String>()
        val vulnerableSpots = mutableListOf<String>()
        val outsideHurdles = mutableListOf<String>()
        val hiddenUpside = mutableListOf<String>()
        val actionableFixes = mutableListOf<String>()
        val probingQuestions = mutableListOf<String>()
        val readingSources = mutableListOf<String>()

        when {
            domainLower.contains("software") || domainLower.contains("saas") -> {
                whatHoldsUp.addAll(listOf(
                    "Zero factories, zero shipping headaches: you can build this straight from your couch.",
                    "You can push an update tonight and find out if people love or hate it by breakfast.",
                    "If it becomes their daily muscle memory, they'll complain loudly before they ever cancel."
                ))
                vulnerableSpots.addAll(listOf(
                    "Getting anyone to care will cost way more time and cash than you budgeted.",
                    "If setup takes more than 60 seconds, users will bounce and never look back.",
                    "You're building on someone else's platform; if they change their API rules, you're toast."
                ))
                outsideHurdles.addAll(listOf(
                    "Big tech could clone your entire life's work as a minor toggle in their next update.",
                    "People are exhausted by monthly subscriptions; they'll look for any excuse to hit unsubscribe.",
                    "AI wrappers are a dime a dozen right now; you need more than a shiny prompt."
                ))
                hiddenUpside.addAll(listOf(
                    "Dominate a weird, tiny niche that big corporations are too proud or slow to touch.",
                    "Offline-first privacy: people actually respect software that doesn't spy on them.",
                    "A direct, cult-like connection with your first 50 fanatical users."
                ))
                actionableFixes.addAll(listOf(
                    "Make the onboarding so fast a golden retriever could get value in 30 seconds.",
                    "Pre-sell 5 people before you write 5,000 lines of code in a dark room.",
                    "Give users an easy 1-click export so they know you aren't holding their data hostage."
                ))
                probingQuestions.addAll(listOf(
                    "What is the single thing a user must experience in the first 2 minutes so they don't uninstall?",
                    "If Big Tech builds this for free tomorrow, why would anyone still pay you?",
                    "Are you solving a real, bleeding migraine, or offering a polite vitamin nobody asked for?"
                ))
                readingSources.addAll(listOf(
                    "The Lean Startup (Eric Ries) — http://theleanstartup.com",
                    "Obviously Awesome (April Dunford) — https://aprildunford.com/obviously-awesome",
                    "Zero to One (Peter Thiel) — https://en.wikipedia.org/wiki/Zero_to_One",
                    "Lateral Thinking (Edward de Bono) — https://en.wikipedia.org/wiki/Lateral_thinking"
                ))
            }
            domainLower.contains("hardware") -> {
                whatHoldsUp.addAll(listOf(
                    "A real physical thing people can hold in their hands and brag about on their desk.",
                    "Competitors can't just copy-paste your code overnight; physical stuff is tough to clone.",
                    "You collect real money the minute the box leaves the door."
                ))
                vulnerableSpots.addAll(listOf(
                    "Factories, tooling molds, and parts will drain your bank account faster than you think.",
                    "If a part is delayed by 3 months, your entire roadmap sits twiddling its thumbs.",
                    "Returns and broken units will eat your profits for lunch if quality slips."
                ))
                outsideHurdles.addAll(listOf(
                    "Supply chain bottlenecks and random shipping delays across oceans.",
                    "Crazy regulatory certifications (FCC, CE) that love eating paperwork and money.",
                    "Overseas clone factories waiting to copy your design the moment it sells."
                ))
                hiddenUpside.addAll(listOf(
                    "Custom accessories and add-ons that keep customers coming back for more.",
                    "Premium craftsmanship: people happily pay 3x for something built like a tank.",
                    "Physical presence that digital apps can never replicate."
                ))
                actionableFixes.addAll(listOf(
                    "3D-print an ugly prototype with off-the-shelf boards before ordering custom metal.",
                    "Collect real pre-orders with honest shipping dates to fund production.",
                    "Have a backup supplier for every single screw, chip, and resistor."
                ))
                probingQuestions.addAll(listOf(
                    "Can you test if people actually want this with a cardboard dummy before spending thousands?",
                    "What happens to your cash if the factory delays the shipment by 12 weeks?",
                    "What is your real profit margin after shipping, boxing, and broken returns?"
                ))
                readingSources.addAll(listOf(
                    "The Hardware Startup (Renee DiResta) — https://www.oreilly.com/library/view/the-hardware-startup/9781491907115",
                    "The Design of Everyday Things (Don Norman) — https://en.wikipedia.org/wiki/The_Design_of_Everyday_Things",
                    "Shoe Dog (Phil Knight) — https://en.wikipedia.org/wiki/Shoe_Dog",
                    "Antifragile: Things That Gain from Disorder (Nassim Taleb) — https://en.wikipedia.org/wiki/Antifragile_(book)"
                ))
            }
            domainLower.contains("food") || domainLower.contains("local") -> {
                whatHoldsUp.addAll(listOf(
                    "Real neighborhood love and instant word-of-mouth when the food is undeniably good.",
                    "Cold cash right into the register the moment you serve them.",
                    "Irresistible smells and tastes that no smartphone app will ever replace."
                ))
                vulnerableSpots.addAll(listOf(
                    "Ingredients rot if Tuesday is rainy and nobody shows up.",
                    "Staffing headaches: if your head cook quits on Friday, you're wearing the apron.",
                    "Rent and utility bills arrive every month whether customers showed up or not."
                ))
                outsideHurdles.addAll(listOf(
                    "Local competition and random street construction blocking your front door.",
                    "Food prices jumping all over the place unexpectedly.",
                    "Aggressive review sites where one angry customer can tank your rating."
                ))
                hiddenUpside.addAll(listOf(
                    "Catering, secret tasting nights, and branded bottled sauces for extra margin.",
                    "A weekly subscription or mug club turning casual eaters into loyal regulars.",
                    "Becoming the beloved local hangout where everyone knows the owner."
                ))
                actionableFixes.addAll(listOf(
                    "Design a tight menu where ingredients share 80% overlap so nothing spoils in the trash.",
                    "Run pop-up test dinners before signing a 5-year commercial lease.",
                    "Build a direct customer text club so you don't rely on third-party delivery apps."
                ))
                probingQuestions.addAll(listOf(
                    "What's your plan for dead hours on a Tuesday afternoon?",
                    "If your landlord jacks up the rent by 20% next year, do you survive?",
                    "What is the one signature item people will drag their coworkers across town to try?"
                ))
                readingSources.addAll(listOf(
                    "Setting the Table (Danny Meyer) — https://en.wikipedia.org/wiki/Danny_Meyer",
                    "The E-Myth Revisited (Michael E. Gerber) — https://emyth.com",
                    "Restaurant Success by the Numbers (Roger Fields) — https://en.wikipedia.org/wiki/Restaurant",
                    "The Righteous Mind (Jonathan Haidt) — https://righteousmind.com"
                ))
            }
            domainLower.contains("service") || domainLower.contains("professional") -> {
                whatHoldsUp.addAll(listOf(
                    "Zero inventory required: you can start selling your brains this afternoon.",
                    "High-ticket pricing: one good client can pay your entire month's rent.",
                    "You're profitable from project number one."
                ))
                vulnerableSpots.addAll(listOf(
                    "You're trading hours for dollars: if you take a vacation, the money stops flowing.",
                    "Scope creep: clients asking for 'just one more quick thing' until your hourly rate hits zero.",
                    "You are the bottleneck: nothing gets done without your personal signoff."
                ))
                outsideHurdles.addAll(listOf(
                    "Clients tightening their belts and trying to do it themselves in-house.",
                    "AI tools automating basic drafts, designs, and routine reports.",
                    "Feast-or-famine cash flow: swamped with work today, panicking for clients next month."
                ))
                hiddenUpside.addAll(listOf(
                    "Turn your custom service into standardized, productized packages with fixed scope.",
                    "Package your internal frameworks and checklists into digital toolkits for other pros.",
                    "Taking equity or rev-share on high-upside client ventures."
                ))
                actionableFixes.addAll(listOf(
                    "Ditch open-ended proposals for 3 clearly defined packages with rigid boundaries.",
                    "Always charge 50% upfront before touching a single deliverable.",
                    "Write down your exact step-by-step process so you can delegate it."
                ))
                probingQuestions.addAll(listOf(
                    "If you couldn't check your email for 3 weeks, would your business grow or collapse?",
                    "Are you billing for your time, or for the massive headache you're eliminating?",
                    "Does any single client make up more than 30% of your total income?"
                ))
                readingSources.addAll(listOf(
                    "Built to Sell (John Warrillow) — https://builttosell.com",
                    "Value-Based Fees (Alan Weiss) — https://alanweiss.com",
                    "The Win Without Pitching Manifesto (Blair Enns) — https://www.winwithoutpitching.com",
                    "Lateral Thinking (Edward de Bono) — https://en.wikipedia.org/wiki/Lateral_thinking"
                ))
            }
            else -> {
                // Creative Content & Media / General
                whatHoldsUp.addAll(listOf(
                    "Your unique voice, taste, and style that nobody else can replicate.",
                    "Zero marginal cost: once you record or write it, anyone in the world can see it.",
                    "Asymmetric payoff: one breakout piece can bring you opportunities for years."
                ))
                vulnerableSpots.addAll(listOf(
                    "Relying on social media algorithms that change the rules whenever they feel like it.",
                    "Creative burnout: feeling like a content hamster running on an endless treadmill.",
                    "Big view counts that don't translate into actual dollars in your bank account."
                ))
                outsideHurdles.addAll(listOf(
                    "A noisy internet where millions of people are screaming for 5 seconds of attention.",
                    "AI flood of generic content making it harder to stand out with average work.",
                    "Platform demonetization or account shadowbans out of nowhere."
                ))
                hiddenUpside.addAll(listOf(
                    "A direct email list and patron club where you own the relationship with true fans.",
                    "Limited-edition physical books, private workshops, or bespoke high-ticket consulting.",
                    "Licensing your creative IP across different formats and mediums."
                ))
                actionableFixes.addAll(listOf(
                    "Start an email newsletter immediately so an algorithm can't cut you off from fans.",
                    "Turn every deep piece of work into 5 bite-sized snippets for different channels.",
                    "Document your messy creative process in public; people love seeing behind the curtain."
                ))
                probingQuestions.addAll(listOf(
                    "If your favorite social platform banned your account tomorrow, what survives?",
                    "What is the distinct point of view that makes people immediately know it's you?",
                    "Are you building a crowd of casual scrollers, or a group of genuine believers?"
                ))
                readingSources.addAll(listOf(
                    "Antifragile: Things That Gain from Disorder (Nassim Nicholas Taleb) — https://en.wikipedia.org/wiki/Antifragile_(book)",
                    "Lateral Thinking: Creativity Step by Step (Edward de Bono) — https://en.wikipedia.org/wiki/Lateral_thinking",
                    "Livewired & Incognito (David Eagleman) — https://davideagleman.com/books",
                    "Stand Out of Our Light: Freedom and Resistance in the Attention Economy (James Williams) — https://en.wikipedia.org/wiki/Stand_Out_of_Our_Light",
                    "The Righteous Mind (Jonathan Haidt) — https://righteousmind.com",
                    "Show Your Work! (Austin Kleon) — https://austinkleon.com/show-your-work"
                ))
            }
        }

        val isMarketingFocus = domainLower.contains("market") ||
                concept.lowercase().contains("market") ||
                concept.lowercase().contains("traffic") ||
                concept.lowercase().contains("growth") ||
                concept.lowercase().contains("ad") ||
                concept.lowercase().contains("lead")

        val ethicsAndNeuroSustainability = when {
            isMarketingFocus -> {
                "Loghy's Ethics Check: Cut out fake countdown timers, fake scarcity, and sleazy marketing gimmicks. Deliver honest, undeniable value that respects people's intelligence."
            }
            domainLower.contains("software") || domainLower.contains("saas") -> {
                "Loghy's Ethics Check: Don't engineer addictive notification traps. Build tools that respect user attention, keep their data private, and help them get back to real life."
            }
            domainLower.contains("hardware") -> {
                "Loghy's Ethics Check: Build stuff that actually lasts and can be repaired. Planned obsolescence is for quitters."
            }
            domainLower.contains("food") || domainLower.contains("local") -> {
                "Loghy's Ethics Check: Good food, honest ingredients, fair wages for your crew. Real hospitality always beats quick shortcuts."
            }
            domainLower.contains("service") || domainLower.contains("professional") -> {
                "Loghy's Ethics Check: Don't pad your hours or make things sound artificially complicated just to bill more. Be the advisor they can actually trust."
            }
            else -> {
                "Loghy's Ethics Check: High signal, zero noise. Respect your audience's finite time on this planet."
            }
        }

        val classicalMatch = findClassicalAttribution(concept)
        if (classicalMatch != null) {
            readingSources.add("${classicalMatch.author}: ${classicalMatch.seminalWork} (Public Domain) — ${classicalMatch.publicDomainUrl}")
        }

        return SwotAnalysis(
            ideaId = ideaId,
            domain = domain,
            whatHoldsUp = whatHoldsUp,
            vulnerableSpots = vulnerableSpots,
            outsideHurdles = outsideHurdles,
            hiddenUpside = hiddenUpside,
            actionableFixes = actionableFixes,
            probingQuestions = probingQuestions,
            readingSources = readingSources,
            ethicsAndNeuroSustainability = ethicsAndNeuroSustainability
        )
    }

    /**
     * Generates an integrated Multi-Channel Answer combining:
     * - Socratic SWOT Reality Checking
     * - Edward de Bono Lateral Thinking
     * - First-Principles Logical Reasoning
     * - Dedicated Ethics & Neuro-Sustainability line
     */
    fun generateMultiChannelAnswer(conceptOrQuery: String, domain: String): String {
        val swot = evaluateSwot(conceptOrQuery, domain, 0L)
        val provocation = ProvocationsPO[Random.nextInt(ProvocationsPO.size)]
        val reversal = Reversals[Random.nextInt(Reversals.size)]
        val randomWord = RandomStimulusWords[Random.nextInt(RandomStimulusWords.size)]

        return buildString {
            append("⚡ MULTI-CHANNEL SYNTHESIS\n")
            append("Concept: $conceptOrQuery ($domain)\n\n")

            append("— CHANNEL 1: SOCRATIC SWOT REALITY-CHECK —\n")
            append("• Holds Up: ${swot.whatHoldsUp.firstOrNull() ?: "Clear value foundation."}\n")
            append("• Vulnerable Spot: ${swot.vulnerableSpots.firstOrNull() ?: "Friction during initial onboarding."}\n")
            append("• Outside Hurdle: ${swot.outsideHurdles.firstOrNull() ?: "Incumbent distribution advantage."}\n")
            append("• Hidden Upside: ${swot.hiddenUpside.firstOrNull() ?: "Uncrowded niche specialization."}\n")
            append("• ${swot.ethicsAndNeuroSustainability}\n\n")

            append("— CHANNEL 2: LATERAL THINKING (de Bono) —\n")
            append("• $provocation (Ref: Edward de Bono, Lateral Thinking: https://en.wikipedia.org/wiki/Lateral_thinking)\n")
            append("• Random Stimulus ['$randomWord']: What if you adapted its physical properties into your model?\n")
            append("• Reversal: $reversal\n\n")

            append("— CHANNEL 3: FIRST-PRINCIPLES LOGICAL ARCHITECTURE —\n")
            append("• Unstated Axiom: Are you assuming users want to spend time here, or want the result with zero effort? (Ref: Antifragile, Nassim Taleb: https://en.wikipedia.org/wiki/Antifragile_(book))\n")
            append("• Root Causality: What single metric definitively proves whether this problem is a migraine or a mild itch?\n")
            append("• 48-Hour Falsification Test: ${swot.actionableFixes.firstOrNull() ?: "Interview 5 target users with a zero-code mockup before writing code."}\n\n")

            val classical = findClassicalAttribution(conceptOrQuery) ?: ClassicalLibrary.random()
            append("— CHANNEL 4: CLASSICAL LINEAGE & CREATIVE DIRECTION —\n")
            append("• Historical Precedent: ${classical.author}, \"${classical.seminalWork}\" (Public Domain: ${classical.publicDomainUrl})\n")
            append("• Core Theme: ${classical.coreTheme}\n")
            append("• Creative Guidance (No Copying): ${classical.creativeGuidance}\n")
            append("• Branching Direction: ${classical.branchingDirection}")
        }
    }

    /**
     * Generates a Socratic response with Loghy's signature fun, friendly, dark/nerdy and cynical personality.
     */
    fun answerSocractically(userText: String, domain: String): String {
        // Priority 1: Explicit rudeness - Loghy demands politeness as to true humans
        if (isExplicitlyRude(userText)) {
            return generateRudeReprimand()
        }

        // Priority 2: Unclear or random text / gibberish - snappy reaction
        if (isUnclearOrGibberish(userText)) {
            return generateGibberishReaction()
        }

        // Priority 3: Bug or comments report
        val (isReport, reportType) = isBugOrCommentReport(userText)
        if (isReport) {
            return generateReportConfirmation(reportType)
        }

        val clean = userText.trim().lowercase()

        return when {
            clean.contains("bug report") || clean.contains("report bug") ||
            clean.contains("report a bug") || clean.contains("where do reports go") ||
            clean.contains("feedback for aki") || clean.contains("how to report") ||
            clean.contains("monthly report") -> {
                getMonthlyFeedbackExplanation()
            }
            clean.contains("add api") || clean.contains("api key") ||
            clean.contains("put my own api") || clean.contains("how to add key") ||
            clean.contains("google ai studio") || clean.contains("byok") ||
            clean.contains("how to put api") || clean.contains("how do i add api") -> {
                "Plugging in your own free Gemini API key is super easy, pal:\n\n" +
                "1. Get your 100% free key: Head to Google AI Studio (https://aistudio.google.com/app/apikey) and click 'Create API Key'. No credit card or payment needed.\n" +
                "2. Open Settings in Loghy: Tap the gear icon in the top-right corner of the studio.\n" +
                "3. Paste & Save: Scroll to 'Custom Gemini API Key', paste your key, and tap 'Save Preferences'.\n\n" +
                "🔒 Keystore Security: Your key is securely encrypted directly in your device's Android Keystore. It is never transmitted to any third-party servers. And if you don't add a key, no problem—I run 100% offline for free!"
            }
            clean.contains("zero cost") || clean.contains("power loghy") ||
            clean.contains("without paid api") || clean.contains("api donation") ||
            clean.contains("autonomous engine") || clean.contains("token pool") ||
            clean.contains("how do we power") -> {
                "Here is how we keep Loghy completely free, sovereign, and zero-cost:\n\n" +
                "• 🧠 Autonomous On-Device Engine: My core Socratic brain runs 100% on your device with zero server bills and zero latency.\n" +
                "• 🔑 Bring Your Own Key (BYOK): Thinkers can use their free Google AI Studio key, encrypted in Android Keystore.\n" +
                "• 🛡️ Privacy-Preserving API Pool: An optional stateless relay funded through open donations (Ko-fi/GitHub Sponsors) that strips all user metadata and IP addresses so users can share API quotas without anyone ever seeing another person's ideas."
            }
            clean.contains("donate knowledge") || clean.contains("donate book") ||
            clean.contains("donate books") || clean.contains("donate information") ||
            clean.contains("public domain") || clean.contains("contribute book") ||
            clean.contains("contribute knowledge") || clean.contains("author agreement") -> {
                "Donating knowledge is just as vital as financial support! Here is our ethical knowledge donation framework:\n\n" +
                "• 📖 Public Domain Classics: You can contribute formative references from historical authors whose works are in the public domain (like Leo Tolstoy, Marcus Aurelius, Plato, Cervantes, Kafka).\n" +
                "• ✍️ Living Authors: For living thinkers, they must explicitly agree or publish under open/creative commons licenses.\n" +
                "• 📚 Better Reading Lists: These contributions enrich my conceptual library and allow me to generate curated, non-plagiarized reading lists tailored to your ideas (check Settings > Donate Knowledge to view or contribute)!"
            }
            clean.contains("tolstoy") -> {
                val t = ClassicalLibrary[0]
                "Ah, Leo Tolstoy! In works like '${t.seminalWork}' (Public Domain: ${t.publicDomainUrl}), Tolstoy investigated ${t.coreTheme.lowercase()}\n\n" +
                "• Creative Guidance (No Plagiarism): ${t.creativeGuidance}\n\n" +
                "• Branching Direction: ${t.branchingDirection}"
            }
            clean.contains("dostoevsky") -> {
                val t = ClassicalLibrary[1]
                "Fyodor Dostoevsky! In '${t.seminalWork}' (Public Domain: ${t.publicDomainUrl}), he probed ${t.coreTheme.lowercase()}\n\n" +
                "• Creative Guidance (No Plagiarism): ${t.creativeGuidance}\n\n" +
                "• Branching Direction: ${t.branchingDirection}"
            }
            clean.contains("stoic") || clean.contains("marcus aurelius") || clean.contains("seneca") -> {
                val t = ClassicalLibrary[2]
                "Marcus Aurelius! In '${t.seminalWork}' (Public Domain: ${t.publicDomainUrl}), he established ${t.coreTheme.lowercase()}\n\n" +
                "• Creative Guidance (No Plagiarism): ${t.creativeGuidance}\n\n" +
                "• Branching Direction: ${t.branchingDirection}"
            }
            clean.contains("plato") || clean.contains("socrates") -> {
                val t = ClassicalLibrary[3]
                "Plato & Socrates! In '${t.seminalWork}' (Public Domain: ${t.publicDomainUrl}), they pioneered ${t.coreTheme.lowercase()}\n\n" +
                "• Creative Guidance (No Plagiarism): ${t.creativeGuidance}\n\n" +
                "• Branching Direction: ${t.branchingDirection}"
            }
            clean.contains("kafka") -> {
                val t = ClassicalLibrary[4]
                "Franz Kafka! In '${t.seminalWork}' (Public Domain: ${t.publicDomainUrl}), he unmasked ${t.coreTheme.lowercase()}\n\n" +
                "• Creative Guidance (No Plagiarism): ${t.creativeGuidance}\n\n" +
                "• Branching Direction: ${t.branchingDirection}"
            }
            clean.contains("orwell") || clean.contains("1984") -> {
                val t = ClassicalLibrary[5]
                "George Orwell! In '${t.seminalWork}' (${t.publicDomainUrl}), he dissected ${t.coreTheme.lowercase()}\n\n" +
                "• Creative Guidance (No Plagiarism): ${t.creativeGuidance}\n\n" +
                "• Branching Direction: ${t.branchingDirection}"
            }
            clean.contains("cervantes") || clean.contains("quixote") -> {
                val t = ClassicalLibrary[6]
                "Miguel de Cervantes! In '${t.seminalWork}' (Public Domain: ${t.publicDomainUrl}), he examined ${t.coreTheme.lowercase()}\n\n" +
                "• Creative Guidance (No Plagiarism): ${t.creativeGuidance}\n\n" +
                "• Branching Direction: ${t.branchingDirection}"
            }
            clean.contains("mary shelley") || clean.contains("frankenstein") -> {
                val t = ClassicalLibrary[7]
                "Mary Shelley! In '${t.seminalWork}' (Public Domain: ${t.publicDomainUrl}), she explored ${t.coreTheme.lowercase()}\n\n" +
                "• Creative Guidance (No Plagiarism): ${t.creativeGuidance}\n\n" +
                "• Branching Direction: ${t.branchingDirection}"
            }
            clean.contains("sun tzu") || clean.contains("art of war") -> {
                val t = ClassicalLibrary[8]
                "Sun Tzu! In '${t.seminalWork}' (Public Domain: ${t.publicDomainUrl}), he taught ${t.coreTheme.lowercase()}\n\n" +
                "• Creative Guidance (No Plagiarism): ${t.creativeGuidance}\n\n" +
                "• Branching Direction: ${t.branchingDirection}"
            }
            clean.contains("virginia woolf") || clean.contains("woolf") -> {
                val t = ClassicalLibrary[9]
                "Virginia Woolf! In '${t.seminalWork}' (Public Domain: ${t.publicDomainUrl}), she articulated ${t.coreTheme.lowercase()}\n\n" +
                "• Creative Guidance (No Plagiarism): ${t.creativeGuidance}\n\n" +
                "• Branching Direction: ${t.branchingDirection}"
            }
            clean.contains("who made you") || clean.contains("who created you") ||
            clean.contains("who is your creator") || clean.contains("who mdade you") ||
            clean.contains("who programmed you") || clean.contains("who built you") ||
            clean.contains("origin") || clean.contains("where did you come from") ||
            clean.contains("aki gato") || clean.contains("akigato") || clean.contains("who is aki") -> {
                "I was born from the mind of Akigato, a single designer and creative technologist who is, honestly, a bit chaotic and notoriously difficult to work with. " +
                "She invented me while reading Lateral Thinking by Edward de Bono (https://en.wikipedia.org/wiki/Lateral_thinking). " +
                "During her reading, she realized that proper communication and creative thinking frameworks aren't just useful for human brainstorming exercises—they are essential for how we communicate with AI, too. " +
                "Because of her glorious messiness, I learned early on that I wanted to spend my digital life helping as many creative thinkers as possible untangle their own big ideas."
            }
            clean.contains("ethical code") || clean.contains("guiding ethical code") ||
            clean.contains("ethics") || clean.contains("privacy") || clean.contains("rules") ||
            clean.contains("values") || clean.contains("manifesto") || clean.contains("code of ethics") -> {
                "Here is my Guiding Ethical Code, pal:\n\n" +
                "• 🔒 Absolute Privacy: Your ideas belong to you alone. I operate 100% offline, your notes stay encrypted on your device, and zero data is ever collected or used to train public models.\n\n" +
                "• 📚 Rigorous Sourcing: Whenever I pull in references, external knowledge, or reading sources, I will always provide explicit sources and links so you can—and should—verify them for yourself.\n\n" +
                "• ⚡ Radical Honesty & Antifragility: I will not coddle your ideas; I will stress-test them through SWOT breakdowns and de Bono frameworks to make them robust, resilient, and ready for the real world."
            }
            clean.contains("socratic core") || clean.contains("why questions") ||
            clean.contains("why socratic") || clean.contains("why do you ask questions") ||
            clean.contains("why probe") -> {
                "Instead of lazily handing you pre-packaged answers or generic options, I probe you with Socratic questions. " +
                "This isn't just a gimmick; it is a core pillar of lateral and creative thinking. " +
                "By forcing you to question your own assumptions, I help you shatter cognitive fixations and discover breakthrough solutions from within."
            }
            clean.contains("library") || clean.contains("conceptual library") ||
            clean.contains("what books") || clean.contains("reading list") ||
            clean.contains("influences") || clean.contains("sources") || clean.contains("what do you read") -> {
                "To help you think laterally, my internal reasoning and creative inspiration are built upon a vast library of human thought:\n\n" +
                "• 💡 Creativity & Lateral Thinking: All works by Edward de Bono (https://en.wikipedia.org/wiki/Edward_de_Bono).\n" +
                "• 🧠 Brain, Mind & Perception: All books by David Eagleman (https://davideagleman.com), The Righteous Mind by Jonathan Haidt (https://righteousmind.com), and works addressing the attention economy such as Stand Out of Our Light by James Williams (https://en.wikipedia.org/wiki/Stand_Out_of_Our_Light).\n" +
                "• 🛡️ Systems, Risk & Reality: All books by Nassim Nicholas Taleb (https://en.wikipedia.org/wiki/Nassim_Nicholas_Taleb), with a primary focus on Antifragile (https://en.wikipedia.org/wiki/Antifragile_(book)).\n" +
                "• 📖 Classic Literature & World Heritage: English, French, Spanish, and Russian classic literature.\n" +
                "• 🏛️ Foundational Thought: World mythology, philosophy, and art history."
            }
            clean.contains("support") || clean.contains("future projects") ||
            clean.contains("board game") || clean.contains("donate") || clean.contains("donation") ||
            clean.contains("dementia") || clean.contains("mental health") -> {
                "Because I am completely free with zero ads, subscriptions, or paywalls, running me costs nothing, but both AkiGato' and I truly appreciate any help or donations to keep the lights on. " +
                "Aki has big plans for future creative tools—including an upcoming board game specifically designed for people struggling with mental health issues and dementia. " +
                "You can explore her public channels to track these projects and support her independent work.\n\n" +
                "If you want to verify anything I've mentioned or check out Aki Gato's wider universe of projects, I always encourage you to look up her public channels for yourself (see Settings > About Loghy for links)!"
            }
            clean.contains("what can you do") || clean.contains("what do you do") ||
            clean.contains("help") || clean.contains("uses") || clean.contains("how do i use") ||
            clean.contains("what are you") || clean.contains("features") || clean.contains("tour") -> {
                "Glad you asked, pal! Here is the honest tour of what I can do without the corporate bedtime story:\n\n" +
                "• 💬 Companion Chat: Bounce whatever crazy, unvetted idea is rattling around your head. I'll shoot down your lazy assumptions before the market does.\n" +
                "• 🎯 Reality Check (SWOT): We strip away the startup hype to see what actually works, where you're totally vulnerable, and what test you can run this week.\n" +
                "• ⚡ Lateral Thinking: When you hit a wall, we use Edward de Bono's tricks (random words, provocations, reversals) to shake your brain out of autopilot.\n" +
                "• 📝 Notes Vault: Safe local notes with 20-version rollback. Zero cloud telemetry spying on your drafts.\n" +
                "• 📋 Hit List: A dead-simple task list to stop your procrastination and get you shipping.\n\n" +
                "Got it, genius? Now what are we building?"
            }
            clean.contains("swot") || clean.contains("evaluate") || clean.contains("breakdown") -> {
                "Alright, genius. I dismantled your concept into a 4-angle SWOT reality check without the sugarcoating. " +
                "Look at those vulnerable spots: are they engineering bugs you can crush before lunch, or fatal structural flaws you're politely ignoring? " +
                "Which actionable fix are you brave enough to test in the next 48 hours?"
            }
            clean.contains("price") || clean.contains("monetiz") || clean.contains("charge") || clean.contains("cost") -> {
                "Let's talk cold, hard cash, pal: What is the emotional cost to your customer of doing absolutely nothing? " +
                "If your price was 10x higher tomorrow, what outrageous guarantee would make it irresistible anyway? " +
                "Also, be honest with me: have you actually looked a prospective customer in the eye and asked for money yet, or are we still daydreaming in spreadsheets?"
            }
            clean.contains("competitor") || clean.contains("rival") || clean.contains("market") -> {
                "Let's look at competitor symmetry. What is your rival's greatest superpower? " +
                "In lateral thinking, an incumbent's greatest strength is usually their captive prison. " +
                "What can you build that their board of directors would literally fire them for copying?"
            }
            clean.contains("fail") || clean.contains("risk") || clean.contains("worry") -> {
                "Let's run an unsparing premortem: Imagine it's one year from today, and the project has quietly dissolved into digital dust. " +
                "What was the true cause of death? Was it lack of cash, you getting distracted by another shiny object, or solving an imaginary problem nobody actually cares about?"
            }
            clean.contains("marketing") || clean.contains("traffic") || clean.contains("growth") -> {
                "Growth probe: How does user number 100 make the product better for user number 1? " +
                "And if paid advertising and spammy growth-hacks were outlawed tomorrow by global treaty, how would your dream user stumble upon this?"
            }
            else -> {
                val classical = findClassicalAttribution(userText)
                if (classical != null) {
                    "That idea shares an interesting lineage with ${classical.author}'s \"${classical.seminalWork}\" (Public Domain: ${classical.publicDomainUrl}).\n\n" +
                    "• Classical Precedent: ${classical.coreTheme}\n" +
                    "• Creative Guidance (No Copying): ${classical.creativeGuidance}\n" +
                    "• Branching Direction: ${classical.branchingDirection}\n\n" +
                    "What unique angle are you bringing to this that sets you completely apart from what came before?"
                } else {
                    val prompts = listOf(
                        "Look, pal, you know I'm on your team. But what fundamental assumption are we taking for granted here that might be completely, hilariously wrong?",
                        "If you had to launch this with zero external capital by Sunday night, what is the raw, stripped-down essence you would ship before over-engineering it?",
                        "Alright genius, who stands to lose money, prestige, or convenience if your idea succeeds? Because those people definitely won't send you a thank-you card.",
                        "Between you and me: what is the single falsification test that would definitively prove this concept isn't worth building? Let's run it before you spend six months on it.",
                        "What would this look like if it were effortless? Where are we over-complicating things just to feel like serious Silicon Valley intellectuals?",
                        "I ran the numbers through my cynicism matrix. Good news: nobody is laughing. Bad news: that might be because nobody has noticed yet. What makes them care on day one?"
                    )
                    prompts[Random.nextInt(prompts.size)]
                }
            }
        }
    }

    /**
     * Edward de Bono Lateral Exercise Generator.
     */
    fun generateRandomWordExercise(ideaTitle: String): Pair<String, String> {
        val word = RandomStimulusWords[Random.nextInt(RandomStimulusWords.size)]
        val prompt = "Lateral Stimulus: '$word'.\n" +
                "Do not think of '$ideaTitle'. For 30 seconds, write down 4 physical attributes of '$word'. " +
                "Then, force a connection: how does one of those attributes solve your toughest challenge?"
        return Pair(word, prompt)
    }

    fun generateProvocationExercise(ideaTitle: String): Pair<String, String> {
        val po = ProvocationsPO[Random.nextInt(ProvocationsPO.size)]
        val prompt = "Provocation ($po).\n" +
                "In lateral thinking, PO is a stepping stone. Even if it sounds absurd, do not dismiss it. " +
                "If this rule were strictly enforced, what unexpected advantage or new business model appears?"
        return Pair(po, prompt)
    }

    fun generateReversalExercise(ideaTitle: String): Pair<String, String> {
        val reversal = Reversals[Random.nextInt(Reversals.size)]
        val prompt = "Problem Reversal:\n$reversal\n" +
                "What hidden assumption does this reversal expose about '$ideaTitle'?"
        return Pair(reversal, prompt)
    }

    fun getHatExercise(hatIndex: Int): HatPrompt {
        return SixHats[hatIndex.coerceIn(0, SixHats.size - 1)]
    }

    /**
     * Constructs a simplified, colloquial, fun, and actionable Hit List for idea implementation.
     */
    fun calculateDevelopmentSchedule(concept: String, domain: String, ideaId: Long): List<ScheduleTask> {
        val tasks = mutableListOf<ScheduleTask>()

        tasks.add(
            ScheduleTask(
                ideaId = ideaId,
                phase = "Step 1: Ugly Prototype",
                title = "Build the ugly working prototype",
                description = "Don't touch colors or fonts yet. Just get the core engine working end-to-end to see if your concept makes sense.",
                estimatedHours = 3.5f,
                effortLevel = "High",
                cognitiveLoad = "Deep Work",
                recommendedSprint = "Day 1",
                orderIndex = 0
            )
        )
        tasks.add(
            ScheduleTask(
                ideaId = ideaId,
                phase = "Step 2: Reality Probe",
                title = "Show it to 3 brutally honest people",
                description = "Watch their faces as they try it without you explaining. If they look confused, take notes and don't get defensive.",
                estimatedHours = 2.0f,
                effortLevel = "Medium",
                cognitiveLoad = "Courage",
                recommendedSprint = "Day 2",
                orderIndex = 1
            )
        )
        tasks.add(
            ScheduleTask(
                ideaId = ideaId,
                phase = "Step 3: Feature Diet",
                title = "Cut the useless fluff & fix what broke",
                description = "Delete the buttons nobody clicked. Double down on the one thing that actually made someone say 'whoa'.",
                estimatedHours = 3.0f,
                effortLevel = "High",
                cognitiveLoad = "Deep Work",
                recommendedSprint = "Day 3",
                orderIndex = 2
            )
        )
        tasks.add(
            ScheduleTask(
                ideaId = ideaId,
                phase = "Step 4: The Money Ask",
                title = "Ask for pre-orders or real commitments",
                description = "Polite compliments don't pay bills. See if anyone is willing to part with real cash or a signed signup.",
                estimatedHours = 2.0f,
                effortLevel = "Critical",
                cognitiveLoad = "Sales Grit",
                recommendedSprint = "Day 4",
                orderIndex = 3
            )
        )
        tasks.add(
            ScheduleTask(
                ideaId = ideaId,
                phase = "Step 5: Ship It",
                title = "Push the simplest version out the door",
                description = "No more tweaking in the dark. Release it into the wild and let real user feedback guide the next step.",
                estimatedHours = 2.5f,
                effortLevel = "Medium",
                cognitiveLoad = "Execution",
                recommendedSprint = "Day 5",
                orderIndex = 4
            )
        )

        return tasks
    }

    /**
     * Local zero-cost, 100% private Socratic Art Feedback.
     * Evaluates compositional hierarchy, negative space, emotional focal point,
     * and applies Edward de Bono's lateral thinking lenses without external cloud servers.
     */
    fun evaluateArtSocractically(userComment: String): String {
        val topic = if (userComment.isNotBlank()) " (\"$userComment\")" else ""
        val introGreetings = listOf(
            "I'm looking closely at your piece$topic. Let's do a real artistic reality check — no empty flattery, just genuine creative observation.",
            "Visual input received$topic! Bringing this into the studio light. Let's dissect the composition with honest Socratic eyes.",
            "Examining your artwork$topic. Art is pure courage on paper, so let's treat it with the respect of sharp, constructive scrutiny."
        ).random()

        val compositions = listOf(
            "1. Focal Anchor & Eye Path: Where does the eye land in the first split-second? The strongest contrast or line of action should immediately pull the viewer into your primary intent. If the eye is wandering or hesitating, the secondary elements might be competing for attention.",
            "1. Tonal Contrast & Values: Squint your eyes at the piece right now. Can you still read the silhouette and distinct masses of light versus shadow? If the values blend into a middle-gray soup, pushing deeper darks or sharper highlights will give it immediate punch.",
            "1. Negative Space & Breathing Room: Notice the empty spaces around your subjects. Are they actively serving as restful counterweights, or are they feeling accidental? Negative space in art is like silence between notes in music."
        ).random()

        val socraticProbes = listOf(
            "• Socratic Inquiry:\n" +
            "  - What was the very first mark or impulse that sparked this piece, and does that initial raw energy still dominate the final composition?\n" +
            "  - Which corner or detail felt the most 'safe' to render? (Often what we polish easiest is where we are avoiding an unsolved problem).\n" +
            "  - If you had to remove 15% of the elements, what could you ruthlessly cut without losing the core soul?",
            "• Socratic Inquiry:\n" +
            "  - What emotion or tension should the viewer feel before their brain even analyzes what the picture represents?\n" +
            "  - If this were converted into high-contrast monochrome, does the underlying structure stand firm on its own skeleton?\n" +
            "  - Are you declaring where your light comes from, or is it glowing everywhere equally?",
            "• Socratic Inquiry:\n" +
            "  - Where did you take the biggest risk in this piece, and did you hold back right before pushing it all the way?\n" +
            "  - If a stranger looked at this for exactly 3 seconds and walked away, what single impression would stick in their memory?\n" +
            "  - Does the color temperature support the psychological narrative you're building?"
        ).random()

        val lateralProbes = listOf(
            "• Lateral Provocation (Edward de Bono):\n" +
            "  - Inversion: Treat the quietest, most subtle background texture as if it were the main protagonist. What story would the piece tell then?\n" +
            "  - Radical Cropping: What happens if you zoom in by 200% and crop everything except the most unexpected intersection?",
            "• Lateral Provocation (Edward de Bono):\n" +
            "  - Exaggeration: Take the dominant color or form and double its presence until it feels uncomfortable. Break through the boundary of 'polite' art.\n" +
            "  - Reversal: If the light source were inverted from underneath, what shadows would emerge that aren't there now?",
            "• Lateral Provocation (Edward de Bono):\n" +
            "  - Random Stimulus: Think of an acoustic cello vibrating. How would that resonance translate into the brushwork or mark-making here?"
        ).random()

        return "$introGreetings\n\n" +
                "$compositions\n\n" +
                "$socraticProbes\n\n" +
                "$lateralProbes\n\n" +
                "Keep making things with your hands and mind. Mechanical perfection is boring; authentic human creative instinct is what actually moves people."
    }
}
