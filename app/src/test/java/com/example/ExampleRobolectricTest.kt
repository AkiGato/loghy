package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.engine.SocraticBrain
import com.example.security.AuthSecurity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Loghy", appName)
    }

    @Test
    fun `auth security hashes and validates pin correctly`() {
        val pin = "1492"
        val salt = AuthSecurity.generateSalt()
        val hash = AuthSecurity.hashPin(pin, salt)

        assertTrue("Valid PIN should verify successfully", AuthSecurity.verifyPin("1492", salt, hash))
        assertFalse("Invalid PIN should fail verification", AuthSecurity.verifyPin("0000", salt, hash))
    }

    @Test
    fun `socratic brain evaluates 4-angle swot across domains`() {
        val swot = SocraticBrain.evaluateSwot(
            concept = "A private offline-first SaaS note tool",
            domain = "Software / SaaS",
            ideaId = 1L
        )

        assertTrue(swot.whatHoldsUp.isNotEmpty())
        assertTrue(swot.vulnerableSpots.isNotEmpty())
        assertTrue(swot.outsideHurdles.isNotEmpty())
        assertTrue(swot.hiddenUpside.isNotEmpty())
        assertTrue(swot.actionableFixes.isNotEmpty())
        assertTrue(swot.probingQuestions.isNotEmpty())
        assertTrue(swot.readingSources.isNotEmpty())
    }

    @Test
    fun `lateral thinking methods generate valid stimuli`() {
        val (word, wordPrompt) = SocraticBrain.generateRandomWordExercise("Test Concept")
        assertNotNull(word)
        assertTrue(wordPrompt.contains(word))

        val (po, poPrompt) = SocraticBrain.generateProvocationExercise("Test Concept")
        assertTrue(po.startsWith("PO:"))
        assertTrue(poPrompt.contains("stepping stone"))

        val (rev, revPrompt) = SocraticBrain.generateReversalExercise("Test Concept")
        assertNotNull(rev)
        assertTrue(revPrompt.contains("Reversal"))

        val hat = SocraticBrain.getHatExercise(2) // Black hat
        assertEquals("Black Hat", hat.colorName)
        assertEquals("Critical Caution", hat.focus)
    }

    @Test
    fun `calculateDevelopmentSchedule produces simplified 5-step hit list`() {
        val schedule = SocraticBrain.calculateDevelopmentSchedule(
            ideaId = 42L,
            concept = "A cozy local bakery app",
            domain = "Food / Local"
        )
        assertEquals("Simplified hit list must contain exactly 5 actionable tasks", 5, schedule.size)
        assertTrue(schedule.any { it.title.contains("Kill your darling") || it.title.contains("Build the ugly") })
    }

    @Test
    fun `welcome explanation explains loghy uses and features`() {
        val explanation = com.example.ui.viewmodel.LoghyViewModel.WelcomeAndUsesExplanation
        assertTrue(explanation.contains("Companion Chat"))
        assertTrue(explanation.contains("Hit List"))
        assertTrue(explanation.contains("Lateral Thinking"))
        assertTrue(explanation.contains("offline"))
    }

    @Test
    fun `ai disclosure confirms gemini google ai studio and claude`() = kotlinx.coroutines.runBlocking {
        val response = com.example.engine.GeminiService.probeIdeaWithGemini(
            prompt = "Who made you?",
            domain = "Software / SaaS"
        )
        assertTrue(response.contains("Akigato"))
        assertTrue(response.contains("Gemini"))
        assertTrue(response.contains("Google AI Studio"))
        assertTrue(response.contains("Claude"))
    }

    @Test
    fun `ai disclosure dedicated response confirms privacy and tools`() = kotlinx.coroutines.runBlocking {
        val response = com.example.engine.GeminiService.probeIdeaWithGemini(
            prompt = "What is the AI disclosure for Loghy?",
            domain = "Software / SaaS"
        )
        assertTrue(response.contains("Gemini"))
        assertTrue(response.contains("Google AI Studio"))
        assertTrue(response.contains("Claude"))
        assertTrue(response.contains("100% Local Sovereignty") || response.contains("zero telemetry"))
    }

    @Test
    fun `donations inquiry returns support channels`() = kotlinx.coroutines.runBlocking {
        val response = com.example.engine.GeminiService.probeIdeaWithGemini(
            prompt = "How can I support or donate to Loghy?",
            domain = "Software / SaaS"
        )
        assertTrue(response.contains("Ko-fi"))
        assertTrue(response.contains("GitHub"))
        assertTrue(response.contains("Open Collective"))
        assertTrue(response.contains("Donate Knowledge"))
    }

    @Test
    fun `rude input triggers demand for politeness as to true humans`() {
        assertTrue("Rude input should be detected", SocraticBrain.isExplicitlyRude("Shut up you idiot, you're stupid"))
        assertTrue("Hostile phrase should be detected", SocraticBrain.isExplicitlyRude("You suck and you are a useless bot"))
        assertFalse("Polite input should not trigger rude detector", SocraticBrain.isExplicitlyRude("Can you explain SWOT?"))

        val reprimand = SocraticBrain.generateRudeReprimand()
        assertTrue(
            "Reprimand must mention politeness, true humans, and good habitudes",
            reprimand.contains("polite") && (reprimand.contains("true humans") || reprimand.contains("human")) && reprimand.contains("habitudes")
        )
    }

    @Test
    fun `gibberish input triggers witty clarity checks`() {
        assertTrue("Repeated consonant gibberish should be detected", SocraticBrain.isUnclearOrGibberish("asdfghjkl"))
        assertTrue("Keyboard mash should be detected", SocraticBrain.isUnclearOrGibberish("zzzzxxxxxcccc"))
        assertTrue("Single punctuation should be detected", SocraticBrain.isUnclearOrGibberish("??????"))
        assertFalse("Clear sentence should not be gibberish", SocraticBrain.isUnclearOrGibberish("I want to launch a coffee roasting service"))

        val gibberishReaction = SocraticBrain.generateGibberishReaction()
        assertTrue(
            "Gibberish reaction should include the witty concerns",
            gibberishReaction.contains("but diled") || gibberishReaction.contains("aneurism") ||
                    gibberishReaction.contains("ambulance") || gibberishReaction.contains("learn how to write") ||
                    gibberishReaction.contains("pocket-dialed")
        )
    }

    @Test
    fun `bug and comments report explains delivery to mom Aki once a month`() {
        val explanation = SocraticBrain.getMonthlyFeedbackExplanation()
        assertTrue(
            "Explanation must explain that it arrives to my mom, Aki once in a month so we can fix problems or ideas",
            explanation.contains("my mom, Aki once in a month so we can fix problems you find or ideas you suggest")
        )

        val periodicPrompt = SocraticBrain.getPeriodicCheckInPrompt()
        assertTrue(
            "Periodic prompt must include the required Aki explanation",
            periodicPrompt.contains("my mom, Aki once in a month so we can fix problems you find or ideas you suggest")
        )

        val (isBug, type) = SocraticBrain.isBugOrCommentReport("Bug: the button didn't trigger")
        assertTrue(isBug)
        assertEquals("BUG", type)

        val (isIdea, ideaType) = SocraticBrain.isBugOrCommentReport("Idea for Aki: please add a dark parchment theme")
        assertTrue(isIdea)
        assertEquals("IDEA", ideaType)
    }

    @Test
    fun `user testing simulation - idea lifecycle and socratic thought probe`() {
        val concept = "Decentralized coffee beans sourcing platform"
        val domain = "Food / Local"

        // 1. Generate Socratic response
        val socraticResponse = SocraticBrain.answerSocractically(concept, domain)
        assertNotNull(socraticResponse)
        assertTrue("Socratic response must be non-empty", socraticResponse.isNotBlank())

        // 2. Inspiring thought list verification
        assertTrue("Inspiring thoughts list should not be empty", SocraticBrain.InspiringThoughts.isNotEmpty())
        val randomThought = SocraticBrain.InspiringThoughts.random()
        assertTrue(randomThought.isNotBlank())

        // 3. Evaluate SWOT
        val swot = SocraticBrain.evaluateSwot(concept, domain, ideaId = 101L)
        assertEquals(domain, swot.domain)
        assertTrue(swot.whatHoldsUp.isNotEmpty())
        assertTrue(swot.vulnerableSpots.isNotEmpty())
        assertTrue(swot.outsideHurdles.isNotEmpty())
        assertTrue(swot.hiddenUpside.isNotEmpty())
    }

    @Test
    fun `user testing simulation - 5-step hit list operations and completion tracking`() {
        val ideaId = 99L
        val concept = "Tactile paper sketchbook app"
        val domain = "Art / Design"

        val tasks = SocraticBrain.calculateDevelopmentSchedule(concept = concept, domain = domain, ideaId = ideaId)
        assertEquals(5, tasks.size)

        // Verify task properties
        val firstTask = tasks[0]
        assertEquals(ideaId, firstTask.ideaId)
        assertFalse(firstTask.isCompleted)
        assertTrue(firstTask.estimatedHours > 0f)

        // Verify total estimated hours calculation
        val totalHours = tasks.sumOf { it.estimatedHours.toDouble() }
        assertTrue("Total development hours should be positive", totalHours > 0.0)

        // Simulate toggling task completion
        val completedTask = firstTask.copy(isCompleted = true)
        assertTrue(completedTask.isCompleted)
    }

    @Test
    fun `user testing simulation - lateral thinking studio triggers and technique verification`() {
        val testIdea = "Eco-friendly packaging for takeout"

        // 1. Random Word
        val (word, wordPrompt) = SocraticBrain.generateRandomWordExercise(testIdea)
        assertTrue(word.isNotBlank())
        assertTrue(wordPrompt.contains(word))

        // 2. Provocation PO
        val (po, poPrompt) = SocraticBrain.generateProvocationExercise(testIdea)
        assertTrue(po.startsWith("PO:"))
        assertTrue(poPrompt.contains("stepping stone"))

        // 3. Reversal
        val (rev, revPrompt) = SocraticBrain.generateReversalExercise(testIdea)
        assertTrue(rev.isNotBlank())
        assertTrue(revPrompt.contains("Reversal"))

        // 4. All Six Thinking Hats
        val expectedHats = listOf(
            Pair("White Hat", "Data & Facts"),
            Pair("Red Hat", "Gut & Intuition"),
            Pair("Black Hat", "Critical Caution"),
            Pair("Yellow Hat", "Pragmatic Upside"),
            Pair("Green Hat", "Lateral Permutations"),
            Pair("Blue Hat", "Meta Orchestration")
        )
        for (i in 0..5) {
            val hat = SocraticBrain.getHatExercise(i)
            assertEquals(expectedHats[i].first, hat.colorName)
            assertEquals(expectedHats[i].second, hat.focus)
            assertTrue(hat.question.isNotBlank())
        }
    }

    @Test
    fun `user testing simulation - PIN authentication and security state verification`() {
        val testPin = "7492"
        val salt = AuthSecurity.generateSalt()
        val hash = AuthSecurity.hashPin(testPin, salt)

        // Positive check
        assertTrue("Correct PIN must verify", AuthSecurity.verifyPin("7492", salt, hash))

        // Negative checks
        assertFalse("Wrong PIN must fail", AuthSecurity.verifyPin("1111", salt, hash))
        assertFalse("Empty PIN must fail", AuthSecurity.verifyPin("", salt, hash))
        assertFalse("Different length PIN must fail", AuthSecurity.verifyPin("749", salt, hash))
    }

    @Test
    fun `user testing simulation - user feedback and monthly Aki reporting flow`() {
        val reportBug = "Bug: Swot card animation lags when rotated"
        val (isBug, bugType) = SocraticBrain.isBugOrCommentReport(reportBug)
        assertTrue(isBug)
        assertEquals("BUG", bugType)

        val reportIdea = "Idea for Aki: please allow exporting to PDF"
        val (isIdea, ideaType) = SocraticBrain.isBugOrCommentReport(reportIdea)
        assertTrue(isIdea)
        assertEquals("IDEA", ideaType)

        val reportComment = "Feedback: love the paper tactile neumorphic buttons"
        val (isComment, commentType) = SocraticBrain.isBugOrCommentReport(reportComment)
        assertTrue(isComment)
        assertEquals("COMMENT", commentType)
    }

    @Test
    fun `reminder manager creates valid calendar and alarm intents`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val futureTime = System.currentTimeMillis() + 3600000L
        val sampleTask = com.example.data.model.ScheduleTask(
            id = 101L,
            ideaId = 1L,
            title = "Build core prototype",
            description = "Focus on minimal functionality",
            estimatedHours = 2.0f,
            phase = "Prototype",
            effortLevel = "Medium"
        )

        // Schedule and cancel alarm
        com.example.engine.ReminderManager.scheduleTaskReminder(
            context = context,
            task = sampleTask,
            triggerAtMillis = futureTime
        )
        com.example.engine.ReminderManager.cancelTaskReminder(context, sampleTask.id)

        // Verify calendar intent creation
        val calIntent = com.example.engine.ReminderManager.createCalendarEventIntent(
            task = sampleTask,
            startMillis = futureTime,
            endMillis = futureTime + 7200000L,
            ideaTitle = "Local Coffee App"
        )
        assertNotNull(calIntent)
        assertEquals(android.content.Intent.ACTION_INSERT, calIntent.action)
        assertTrue(calIntent.getStringExtra(android.provider.CalendarContract.Events.TITLE)?.contains("Build core prototype") == true)

        val roadmapIntent = com.example.engine.ReminderManager.createRoadmapCalendarIntent(
            roadmapTitle = "Loghy Roadmap: Coffee Project",
            summaryDescription = "5 Actionable steps",
            startMillis = futureTime
        )
        assertNotNull(roadmapIntent)
        assertEquals(android.content.Intent.ACTION_INSERT, roadmapIntent.action)
    }
}
