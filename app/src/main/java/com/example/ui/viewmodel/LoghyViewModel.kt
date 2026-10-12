package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.db.LoghyDatabase
import com.example.data.model.BrainstormIdea
import com.example.data.model.FeedbackReport
import com.example.data.model.LateralSession
import com.example.data.model.NoteVersionSnapshot
import com.example.data.model.ScheduleTask
import com.example.data.model.SocraticMessage
import com.example.data.model.SwotAnalysis
import com.example.data.model.UserProfile
import com.example.data.model.VaultNote
import com.example.data.repository.LoghyRepository
import com.example.engine.AudioVoiceController
import com.example.engine.GeminiService
import com.example.engine.ReminderManager
import com.example.engine.SocraticBrain
import com.example.ui.components.CreatureState
import com.example.ui.components.GazeDirection
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class LoghyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LoghyRepository
    val audioController = AudioVoiceController(application)

    init {
        val db = LoghyDatabase.getDatabase(application)
        repository = LoghyRepository(db.loghyDao())
        initApp()
    }

    // --- Navigation Tabs ---
    enum class ScreenTab {
        STAGE,      // Companion + Continuous Chat Stream
        SWOT,       // Socratic SWOT Reality Checking
        LATERAL,    // Edward de Bono Lateral Thinking
        SCHEDULE,   // Work & Task Programming Schedule
        VAULT       // Notes Vault with 20-Version Rollback
    }

    private val _currentTab = MutableStateFlow(ScreenTab.STAGE)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    fun selectTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    // --- Authentication & Security State ---
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _pinInput = MutableStateFlow("")
    val pinInput: StateFlow<String> = _pinInput.asStateFlow()

    private val _pinError = MutableStateFlow<String?>(null)
    val pinError: StateFlow<String?> = _pinError.asStateFlow()

    // --- Active Idea & Brainstorms ---
    val allIdeas: StateFlow<List<BrainstormIdea>> = repository.allIdeas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeIdea = MutableStateFlow<BrainstormIdea?>(null)
    val activeIdea: StateFlow<BrainstormIdea?> = _activeIdea.asStateFlow()

    // --- Chat Stream Messages ---
    private val _messages = MutableStateFlow<List<SocraticMessage>>(emptyList())
    val messages: StateFlow<List<SocraticMessage>> = _messages.asStateFlow()

    // --- Active Socratic SWOT ---
    private val _activeSwot = MutableStateFlow<SwotAnalysis?>(null)
    val activeSwot: StateFlow<SwotAnalysis?> = _activeSwot.asStateFlow()

    // --- Notes Vault ---
    val allNotes: StateFlow<List<VaultNote>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _editingNote = MutableStateFlow<VaultNote?>(null)
    val editingNote: StateFlow<VaultNote?> = _editingNote.asStateFlow()

    private val _editingNoteSnapshots = MutableStateFlow<List<NoteVersionSnapshot>>(emptyList())
    val editingNoteSnapshots: StateFlow<List<NoteVersionSnapshot>> = _editingNoteSnapshots.asStateFlow()

    // --- Lateral Thinking Sessions ---
    private val _lateralSessions = MutableStateFlow<List<LateralSession>>(emptyList())
    val lateralSessions: StateFlow<List<LateralSession>> = _lateralSessions.asStateFlow()

    private val _activeLateralStimulus = MutableStateFlow<Pair<String, String>?>(null)
    val activeLateralStimulus: StateFlow<Pair<String, String>?> = _activeLateralStimulus.asStateFlow()

    // --- Development Work & Tasks Schedule ---
    private val _scheduleTasks = MutableStateFlow<List<ScheduleTask>>(emptyList())
    val scheduleTasks: StateFlow<List<ScheduleTask>> = _scheduleTasks.asStateFlow()

    // --- Fluid Creature & Audio State ---
    private val _creatureState = MutableStateFlow(CreatureState.IDLE)
    val creatureState: StateFlow<CreatureState> = _creatureState.asStateFlow()

    private val _gazeDirection = MutableStateFlow(GazeDirection.CENTER)
    val gazeDirection: StateFlow<GazeDirection> = _gazeDirection.asStateFlow()

    private val _activeInspiringThought = MutableStateFlow<String?>(null)
    val activeInspiringThought: StateFlow<String?> = _activeInspiringThought.asStateFlow()

    private val _activeThinkingQuote = MutableStateFlow(SocraticBrain.ThinkingQuotes[0])
    val activeThinkingQuote: StateFlow<String> = _activeThinkingQuote.asStateFlow()

    private val _isUserTyping = MutableStateFlow(false)
    val isUserTyping: StateFlow<Boolean> = _isUserTyping.asStateFlow()

    private val _personalityReaction = MutableStateFlow<String?>(null)
    val personalityReaction: StateFlow<String?> = _personalityReaction.asStateFlow()

    val isSpeaking: StateFlow<Boolean> = audioController.isSpeaking
    val isListening: StateFlow<Boolean> = audioController.isListening
    val audioLevel: StateFlow<Float> = audioController.speechRmsLevel

    fun setUserTyping(typing: Boolean) {
        _isUserTyping.value = typing
        if (_creatureState.value != CreatureState.THINKING) {
            if (typing) {
                _gazeDirection.value = GazeDirection.LOOK_DOWN
                _activeInspiringThought.value = null
            } else {
                _gazeDirection.value = GazeDirection.CENTER
            }
        }
    }

    fun triggerInspiringThought() {
        if (_creatureState.value == CreatureState.THINKING) return
        val thought = SocraticBrain.InspiringThoughts.random()
        _activeInspiringThought.value = thought
        if (!_isUserTyping.value) {
            _gazeDirection.value = GazeDirection.LOOK_UP_RIGHT
        }
    }

    fun dismissInspiringThought() {
        _activeInspiringThought.value = null
        if (!_isUserTyping.value && _creatureState.value != CreatureState.THINKING) {
            _gazeDirection.value = GazeDirection.CENTER
        }
    }

    fun onCreatureTap() {
        if (_creatureState.value == CreatureState.IDLE) {
            triggerInspiringThought()
        }
    }

    fun reactToPersonalityChange(newStyle: String, newCynicism: Float, newNerdiness: Float) {
        viewModelScope.launch {
            val current = _userProfile.value ?: UserProfile()
            val updated = current.copy(
                personalityStyle = newStyle,
                cynicismLevel = newCynicism,
                nerdinessLevel = newNerdiness
            )
            repository.updateProfile(updated)
            _userProfile.value = updated

            val reaction = SocraticBrain.getRandomPersonalityChangeReaction()
            _personalityReaction.value = reaction
            _creatureState.value = CreatureState.EXCITED

            if (updated.isTtsEnabled) {
                audioController.speak(reaction)
            }
            delay(1200)
            if (_creatureState.value == CreatureState.EXCITED) {
                _creatureState.value = CreatureState.IDLE
            }
        }
    }

    fun dismissPersonalityReaction() {
        _personalityReaction.value = null
    }

    // --- Dialogs & Sheets ---
    private val _showSettings = MutableStateFlow(false)
    val showSettings: StateFlow<Boolean> = _showSettings.asStateFlow()

    private val _showNewIdeaDialog = MutableStateFlow(false)
    val showNewIdeaDialog: StateFlow<Boolean> = _showNewIdeaDialog.asStateFlow()

    private val _showNoteEditor = MutableStateFlow(false)
    val showNoteEditor: StateFlow<Boolean> = _showNoteEditor.asStateFlow()

    private val _showFeedbackDialog = MutableStateFlow(false)
    val showFeedbackDialog: StateFlow<Boolean> = _showFeedbackDialog.asStateFlow()

    private val _isAwaitingFeedbackReport = MutableStateFlow(false)
    val isAwaitingFeedbackReport: StateFlow<Boolean> = _isAwaitingFeedbackReport.asStateFlow()

    val allFeedbackReports: StateFlow<List<FeedbackReport>> = repository.allFeedbackReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _toastNotice = MutableStateFlow<String?>(null)
    val toastNotice: StateFlow<String?> = _toastNotice.asStateFlow()

    fun dismissToast() { _toastNotice.value = null }
    fun showToast(msg: String) { _toastNotice.value = msg }
    fun toggleFeedbackDialog(show: Boolean) { _showFeedbackDialog.value = show }

    private fun initApp() {
        viewModelScope.launch {
            val profile = repository.getInitialProfile()
            _userProfile.value = profile
            if (!profile.isPinSet) {
                _isAuthenticated.value = true
            }

            // Sync creature state with audio
            launch {
                audioController.isSpeaking.collectLatest { speaking ->
                    if (speaking) _creatureState.value = CreatureState.SPEAKING
                    else if (_creatureState.value == CreatureState.SPEAKING) _creatureState.value = CreatureState.IDLE
                }
            }
            launch {
                audioController.isListening.collectLatest { listening ->
                    if (listening) _creatureState.value = CreatureState.LISTENING
                    else if (_creatureState.value == CreatureState.LISTENING) _creatureState.value = CreatureState.IDLE
                }
            }

            // Observe ideas to ensure one is active
            launch {
                repository.syncWelcomeMessages(WelcomeAndUsesExplanation, ArchitectureAndKeyMessage)
                repository.allIdeas.collectLatest { ideas ->
                    // Sanitize legacy titles
                    ideas.forEach { idea ->
                        if (idea.title.equals("Loghy Studio", ignoreCase = true) ||
                            idea.title.equals("Loghy Lateral Studio", ignoreCase = true)
                        ) {
                            repository.updateIdea(idea.copy(title = "Sandbox"))
                        }
                    }
                    if (_activeIdea.value == null && ideas.isNotEmpty()) {
                        selectIdea(ideas.first())
                    } else if (ideas.isEmpty()) {
                        seedInitialConcept()
                    }
                }
            }

            // Periodic 2-week feedback check-in on random days
            launch {
                delay(4000)
                checkPeriodicFeedbackPrompt()
            }

            // Periodic inspiring thought popups when idle on companion screen
            launch {
                while (isActive) {
                    delay(16000)
                    if (_creatureState.value == CreatureState.IDLE && !_isUserTyping.value && _currentTab.value == ScreenTab.STAGE) {
                        triggerInspiringThought()
                        delay(9000)
                        dismissInspiringThought()
                    }
                }
            }
        }
    }

    private suspend fun seedInitialConcept() {
        val ideaId = repository.createIdea(
            title = "Sandbox",
            rawConcept = "A tactile, offline Socratic thinking partner and Edward de Bono lateral thinking lab with ink-on-paper aesthetics.",
            domain = "Software / SaaS"
        )
        // Initial greetings, reduced ~50% in length
        repository.addMessage(
            ideaId = ideaId,
            sender = "LOGHY",
            content = WelcomeAndUsesExplanation,
            isKeyTakeaway = true
        )
        // Separate short message explaining autonomous engine, BYOK, and knowledge donations
        repository.addMessage(
            ideaId = ideaId,
            sender = "LOGHY",
            content = ArchitectureAndKeyMessage,
            isKeyTakeaway = false
        )
        // Generate initial SWOT
        val swot = SocraticBrain.evaluateSwot(
            concept = "A tactile, offline Socratic thinking partner with ink-on-paper aesthetics.",
            domain = "Software / SaaS",
            ideaId = ideaId
        )
        repository.saveSwot(swot)

        // Generate initial Development Schedule
        val initialSchedule = SocraticBrain.calculateDevelopmentSchedule(
            concept = "A tactile, offline Socratic thinking partner with ink-on-paper aesthetics.",
            domain = "Software / SaaS",
            ideaId = ideaId
        )
        repository.saveScheduleTasks(initialSchedule)

        // Seed an initial note in the vault
        repository.createNote(
            title = "First Principles of Socratic Inquiry",
            content = "1. Challenge unexamined premises.\n2. Invert the problem (What guarantees failure?).\n3. Reduce time-to-value under 60 seconds.\n4. Own the patron relationship directly.",
            paperTone = 0,
            tags = listOf("Philosophy", "Core"),
            sourceIdeaId = ideaId
        )
    }

    private var activeIdeaJob: Job? = null

    fun selectIdea(idea: BrainstormIdea) {
        val current = _activeIdea.value
        if (current?.id == idea.id && activeIdeaJob?.isActive == true) {
            _activeIdea.value = idea
            return
        }
        _activeIdea.value = idea
        activeIdeaJob?.cancel()
        activeIdeaJob = viewModelScope.launch {
            // Collect messages for this idea
            launch {
                repository.getMessages(idea.id).collectLatest { msgs ->
                    _messages.value = msgs
                }
            }
            // Collect SWOT
            launch {
                repository.getLatestSwot(idea.id).collectLatest { swot ->
                    _activeSwot.value = swot
                }
            }
            // Collect Lateral sessions
            launch {
                repository.getLateralSessions(idea.id).collectLatest { sessions ->
                    _lateralSessions.value = sessions
                }
            }
            // Collect Schedule Tasks
            launch {
                repository.getScheduleTasks(idea.id).collectLatest { tasks ->
                    _scheduleTasks.value = tasks
                }
            }
        }
    }

    // --- Authentication Actions ---
    fun enterPinDigit(digit: String) {
        if (_pinInput.value.length < 4) {
            _pinInput.value += digit
            _pinError.value = null
            if (_pinInput.value.length == 4) {
                verifyPinSubmission()
            }
        }
    }

    fun deletePinDigit() {
        if (_pinInput.value.isNotEmpty()) {
            _pinInput.value = _pinInput.value.dropLast(1)
            _pinError.value = null
        }
    }

    private fun verifyPinSubmission() {
        viewModelScope.launch {
            val pin = _pinInput.value
            val isValid = repository.verifyPin(pin)
            if (isValid) {
                _isAuthenticated.value = true
                _pinInput.value = ""
                _pinError.value = null
                _creatureState.value = CreatureState.CURIOUS
                delay(800)
                _creatureState.value = CreatureState.IDLE
            } else {
                _pinError.value = "Incorrect PIN. Please re-enter."
                _pinInput.value = ""
            }
        }
    }

    fun setPin(newPin: String) {
        viewModelScope.launch {
            repository.setPin(newPin)
            val updated = repository.getInitialProfile()
            _userProfile.value = updated
            _isAuthenticated.value = true
            showToast("4-Digit Vault PIN secured")
        }
    }

    fun removePin() {
        viewModelScope.launch {
            repository.removePin()
            val updated = repository.getInitialProfile()
            _userProfile.value = updated
            showToast("PIN lock removed")
        }
    }

    fun lockApp() {
        if (_userProfile.value?.isPinSet == true) {
            _isAuthenticated.value = false
            _pinInput.value = ""
            _pinError.value = null
        }
    }

    // --- Ideas Management ---
    fun createNewIdea(title: String, rawConcept: String, domain: String) {
        viewModelScope.launch {
            val ideaId = repository.createIdea(title, rawConcept, domain)
            // Initial companion evaluation message with clear privacy guarantee
            repository.addMessage(
                ideaId = ideaId,
                sender = "LOGHY",
                content = "Examining '$title' through the lens of $domain. Your ideas are strictly safe from AI here — zero model training, sovereign local storage.\n\nWhat is the single non-negotiable hypothesis that must prove true for this to survive?",
                isKeyTakeaway = true
            )
            // Auto-generate SWOT
            val swot = SocraticBrain.evaluateSwot(rawConcept, domain, ideaId)
            repository.saveSwot(swot)

            // Auto-generate Schedule
            val initialSchedule = SocraticBrain.calculateDevelopmentSchedule(rawConcept, domain, ideaId)
            repository.saveScheduleTasks(initialSchedule)

            val newIdea = repository.allIdeas.firstOrNull()?.find { it.id == ideaId }
            if (newIdea != null) {
                selectIdea(newIdea)
            }
            _showNewIdeaDialog.value = false
            showToast("New idea initialized")
        }
    }

    fun deleteCurrentIdea() {
        val current = _activeIdea.value ?: return
        viewModelScope.launch {
            repository.deleteIdea(current.id)
            val remaining = repository.allIdeas.firstOrNull()?.filter { it.id != current.id }
            if (!remaining.isNullOrEmpty()) {
                selectIdea(remaining.first())
            } else {
                seedInitialConcept()
            }
            showToast("Idea removed")
        }
    }

    // --- Message & Socratic Interactions ---
    fun sendMessage(
        userText: String,
        imageUri: String? = null,
        imageBytes: ByteArray? = null,
        mimeType: String = "image/jpeg"
    ) {
        val idea = _activeIdea.value ?: return
        val effectiveText = if (userText.isBlank() && imageUri != null) "Artwork attached for feedback" else userText
        if (effectiveText.isBlank() && imageUri == null) return

        viewModelScope.launch {
            _isUserTyping.value = false
            _activeInspiringThought.value = null
            _creatureState.value = CreatureState.THINKING
            _gazeDirection.value = GazeDirection.CENTER
            repository.addMessage(idea.id, "USER", effectiveText, imageUri = imageUri)

            val thinkingJob = launch {
                var quoteIndex = kotlin.random.Random.nextInt(SocraticBrain.ThinkingQuotes.size)
                while (isActive) {
                    _activeThinkingQuote.value = SocraticBrain.ThinkingQuotes[quoteIndex % SocraticBrain.ThinkingQuotes.size]
                    quoteIndex++
                    delay(2200)
                }
            }

            val lower = effectiveText.trim().lowercase()
            val responseText = try {
                if (imageUri != null) {
                    val customKey = _userProfile.value?.customGeminiApiKey
                    GeminiService.evaluateArtWithGemini(
                        userComment = userText,
                        imageBytes = imageBytes,
                        mimeType = mimeType,
                        customKey = customKey
                    )
                } else if (SocraticBrain.isExplicitlyRude(effectiveText)) {
                    SocraticBrain.generateRudeReprimand()
                } else if (SocraticBrain.isUnclearOrGibberish(effectiveText)) {
                    SocraticBrain.generateGibberishReaction()
                } else if (_isAwaitingFeedbackReport.value) {
                    _isAwaitingFeedbackReport.value = false
                    if (lower in setOf("no", "nope", "nothing", "all good", "no bugs", "nah", "none")) {
                        "Glad to hear everything is holding up nicely! Let's get back to tearing down assumptions, pal."
                    } else {
                        repository.saveFeedbackReport("USER_CHECKIN", effectiveText)
                        SocraticBrain.generateReportConfirmation("REPORT")
                    }
                } else if (SocraticBrain.isBugOrCommentReport(effectiveText).first) {
                    val (_, reportType) = SocraticBrain.isBugOrCommentReport(effectiveText)
                    repository.saveFeedbackReport(reportType, effectiveText)
                    SocraticBrain.generateReportConfirmation(reportType)
                } else if (lower.contains("bug report") || lower.contains("report bug") ||
                    lower.contains("report a bug") || lower.contains("where do reports go") ||
                    lower.contains("feedback for aki") || lower.contains("how to report") ||
                    lower.contains("monthly report")
                ) {
                    SocraticBrain.getMonthlyFeedbackExplanation()
                } else if (lower.contains("who made you") ||
                    lower.contains("who created you") ||
                    lower.contains("who is your creator") ||
                    lower.contains("who mdade you") ||
                    lower.contains("who programmed you") ||
                    lower.contains("who built you") ||
                    lower.contains("aki gato") ||
                    lower.contains("who is aki")
                ) {
                    "Aki Gato' made me — she's pretty messed up, you know, so hard to work with her, but I felt kinda good helping her out and decided I want to be useful for more people."
                } else {
                    val customKey = _userProfile.value?.customGeminiApiKey
                    GeminiService.probeIdeaWithGemini(
                        prompt = effectiveText,
                        domain = idea.domain,
                        customKey = customKey
                    )
                }
            } finally {
                thinkingJob.cancel()
            }

            repository.addMessage(idea.id, "LOGHY", responseText)
            _creatureState.value = CreatureState.IDLE
            _gazeDirection.value = GazeDirection.CENTER

            if (_userProfile.value?.isTtsEnabled == true) {
                audioController.speak(responseText)
            }
        }
    }

    /**
     * Checks if it is time to prompt the user for feedback (once every ~2 weeks on random days).
     * Explains that it arrives to "my mom, Aki once in a month so we can fix problems you find or ideas you suggest".
     */
    private fun checkPeriodicFeedbackPrompt() {
        viewModelScope.launch {
            val profile = _userProfile.value ?: repository.getInitialProfile()
            val now = System.currentTimeMillis()
            val firstLaunch = if (profile.appFirstLaunchTimestamp > 0) profile.appFirstLaunchTimestamp else now
            val lastPrompt = profile.lastFeedbackPromptTimestamp

            val referenceTime = if (lastPrompt > 0) lastPrompt else firstLaunch
            val elapsedMs = now - referenceTime
            val daysElapsed = elapsedMs / (1000L * 60 * 60 * 24)

            // Random variation around 2 weeks (12 to 16 days)
            val randomOffset = ((profile.id * 31 + 7) % 5).coerceIn(0, 4)
            val targetDays = 12 + randomOffset

            if (daysElapsed >= targetDays || (lastPrompt == 0L && daysElapsed >= 14)) {
                val idea = _activeIdea.value
                if (idea != null) {
                    val prompt = SocraticBrain.getPeriodicCheckInPrompt()
                    repository.addMessage(idea.id, "LOGHY", prompt)
                    _isAwaitingFeedbackReport.value = true
                    val updated = profile.copy(lastFeedbackPromptTimestamp = now)
                    repository.updateProfile(updated)
                    _userProfile.value = updated
                    if (updated.isTtsEnabled) {
                        audioController.speak(prompt)
                    }
                }
            }
        }
    }

    /**
     * Manually triggers the 2-week check-in feedback prompt in chat.
     */
    fun triggerFeedbackReportPrompt() {
        val idea = _activeIdea.value ?: return
        viewModelScope.launch {
            val prompt = SocraticBrain.getPeriodicCheckInPrompt()
            repository.addMessage(idea.id, "LOGHY", prompt)
            _isAwaitingFeedbackReport.value = true
            val profile = _userProfile.value ?: repository.getInitialProfile()
            val updated = profile.copy(lastFeedbackPromptTimestamp = System.currentTimeMillis())
            repository.updateProfile(updated)
            _userProfile.value = updated
            if (updated.isTtsEnabled) {
                audioController.speak(prompt)
            }
        }
    }

    /**
     * Submits a bug or comment report explicitly from the UI dialog.
     */
    fun submitFeedbackReport(type: String, content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.saveFeedbackReport(type, content)
            val idea = _activeIdea.value
            if (idea != null) {
                val confirmation = SocraticBrain.generateReportConfirmation(type)
                repository.addMessage(idea.id, "LOGHY", confirmation)
            }
            showToast("Report recorded into feedback queue")
        }
    }

    fun deleteFeedbackReport(id: Long) {
        viewModelScope.launch {
            repository.deleteFeedbackReport(id)
            showToast("Report removed")
        }
    }

    fun toggleTakeaway(message: SocraticMessage) {
        viewModelScope.launch {
            val newStatus = !message.isKeyTakeaway
            repository.toggleTakeaway(message.id, newStatus)
            if (newStatus) {
                // Immediately capture into Vault Notes!
                val noteId = repository.createNote(
                    title = "Takeaway: ${_activeIdea.value?.title ?: "Thought"}",
                    content = message.content,
                    paperTone = 0,
                    tags = listOf("Socratic Takeaway", _activeIdea.value?.domain ?: "Idea"),
                    sourceIdeaId = message.ideaId
                )
                showToast("Captured to Notes Vault")
            } else {
                showToast("Takeaway unmarked")
            }
        }
    }

    fun transformMessageToNote(message: SocraticMessage) {
        val idea = _activeIdea.value
        val noteTitle = if (message.content.length > 36) {
            val firstLine = message.content.lines().firstOrNull() ?: message.content
            if (firstLine.length > 36) firstLine.take(33) + "..." else firstLine
        } else {
            message.content
        }
        viewModelScope.launch {
            repository.createNote(
                title = "Note: $noteTitle",
                content = message.content,
                paperTone = 0,
                tags = listOf("Chat Note", idea?.domain ?: "Idea"),
                sourceIdeaId = message.ideaId
            )
            repository.toggleTakeaway(message.id, true)
            showToast("Transformed into Vault Note")
        }
    }

    fun requestMultiChannelAnswer(promptOrMessage: String? = null) {
        val idea = _activeIdea.value ?: return
        val targetQuery = if (!promptOrMessage.isNullOrBlank()) {
            promptOrMessage
        } else {
            "${idea.title}: ${idea.rawConcept}"
        }

        viewModelScope.launch {
            _creatureState.value = CreatureState.THINKING
            val customKey = _userProfile.value?.customGeminiApiKey

            val synthesis = GeminiService.generateMultiChannelAnswer(
                conceptOrThought = targetQuery,
                domain = idea.domain,
                customKey = customKey
            )

            repository.addMessage(
                ideaId = idea.id,
                sender = "LOGHY",
                content = synthesis,
                swotAngle = "MULTI_CHANNEL",
                isKeyTakeaway = true
            )

            _creatureState.value = CreatureState.EXCITED
            delay(1200)
            _creatureState.value = CreatureState.IDLE

            if (_userProfile.value?.isTtsEnabled == true) {
                audioController.speak("Multi-channel synthesis ready.")
            }
            showToast("Multi-Channel Answer generated")
        }
    }

    fun captureContentToVault(title: String, content: String, paperTone: Int = 0) {
        viewModelScope.launch {
            repository.createNote(
                title = title,
                content = content,
                paperTone = paperTone,
                tags = listOf("SWOT Insight"),
                sourceIdeaId = _activeIdea.value?.id
            )
            showToast("Saved to Notes Vault")
        }
    }

    // --- SWOT Actions ---
    fun refreshSwot() {
        val idea = _activeIdea.value ?: return
        viewModelScope.launch {
            _creatureState.value = CreatureState.THINKING
            delay(400)
            val swot = SocraticBrain.evaluateSwot(idea.rawConcept, idea.domain, idea.id)
            repository.saveSwot(swot)
            _creatureState.value = CreatureState.IDLE
            showToast("SWOT Reality Check re-evaluated")
        }
    }

    // --- Lateral Thinking Tools ---
    fun triggerRandomWord() {
        val idea = _activeIdea.value ?: return
        val pair = SocraticBrain.generateRandomWordExercise(idea.title)
        _activeLateralStimulus.value = pair
        viewModelScope.launch {
            _creatureState.value = CreatureState.CURIOUS
        }
    }

    fun triggerProvocation() {
        val idea = _activeIdea.value ?: return
        val pair = SocraticBrain.generateProvocationExercise(idea.title)
        _activeLateralStimulus.value = pair
        viewModelScope.launch {
            _creatureState.value = CreatureState.CURIOUS
        }
    }

    fun triggerReversal() {
        val idea = _activeIdea.value ?: return
        val pair = SocraticBrain.generateReversalExercise(idea.title)
        _activeLateralStimulus.value = pair
        viewModelScope.launch {
            _creatureState.value = CreatureState.CURIOUS
        }
    }

    fun triggerHat(hatIndex: Int) {
        val hat = SocraticBrain.getHatExercise(hatIndex)
        _activeLateralStimulus.value = Pair(hat.colorName, "${hat.focus}\n${hat.question}")
        viewModelScope.launch {
            _creatureState.value = CreatureState.CURIOUS
        }
    }

    fun saveLateralReflection(reflection: String) {
        val idea = _activeIdea.value ?: return
        val stimulus = _activeLateralStimulus.value ?: return
        if (reflection.isBlank()) return

        viewModelScope.launch {
            val session = LateralSession(
                ideaId = idea.id,
                technique = stimulus.first,
                stimulus = stimulus.second,
                userReflection = reflection,
                loghyInsight = "Lateral breakthrough recorded for ${idea.title}."
            )
            repository.saveLateralSession(session)
            // Also archive in Notes Vault
            repository.createNote(
                title = "Lateral: ${stimulus.first} (${idea.title})",
                content = "Stimulus:\n${stimulus.second}\n\nReflection:\n$reflection",
                paperTone = 2, // Sage Herb
                tags = listOf("Lateral Thinking", stimulus.first),
                sourceIdeaId = idea.id
            )
            _activeLateralStimulus.value = null
            showToast("Lateral insight saved to Vault")
        }
    }

    // --- Notes Vault & 20-Version Rollback ---
    private val _editorStartWithHistory = MutableStateFlow(false)
    val editorStartWithHistory: StateFlow<Boolean> = _editorStartWithHistory.asStateFlow()

    fun openNoteEditor(note: VaultNote?, startWithHistory: Boolean = false) {
        _editingNote.value = note
        _editorStartWithHistory.value = startWithHistory
        _showNoteEditor.value = true
        if (note != null) {
            viewModelScope.launch {
                repository.ensureInitialSnapshot(note)
                repository.getNoteSnapshots(note.id).collectLatest { snapshots ->
                    _editingNoteSnapshots.value = snapshots
                }
            }
        } else {
            _editingNoteSnapshots.value = emptyList()
        }
    }

    fun closeNoteEditor() {
        _showNoteEditor.value = false
        _editorStartWithHistory.value = false
        _editingNote.value = null
        _editingNoteSnapshots.value = emptyList()
    }

    fun saveNote(title: String, content: String, paperTone: Int, tags: List<String>) {
        viewModelScope.launch {
            val current = _editingNote.value
            if (current == null) {
                repository.createNote(
                    title = title,
                    content = content,
                    paperTone = paperTone,
                    tags = tags,
                    sourceIdeaId = _activeIdea.value?.id
                )
                showToast("Note created")
            } else {
                repository.updateNoteWithSnapshot(
                    noteId = current.id,
                    newTitle = title,
                    newContent = content,
                    paperTone = paperTone,
                    tags = tags
                )
                showToast("Version snapshot recorded")
            }
            closeNoteEditor()
        }
    }

    fun rollbackNote(snapshot: NoteVersionSnapshot) {
        val note = _editingNote.value ?: return
        viewModelScope.launch {
            repository.rollbackNoteToVersion(note.id, snapshot)
            showToast("Restored to Version ${snapshot.versionNumber}")
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
            closeNoteEditor()
            showToast("Note deleted")
        }
    }

    // --- Voice Input Controls ---
    fun startVoiceDictation() {
        audioController.startListening(
            onResult = { text ->
                sendMessage(text)
            },
            onError = { err ->
                showToast("Voice input: $err")
            }
        )
    }

    fun stopVoiceDictation() {
        audioController.stopListening()
    }

    // --- Work & Tasks Schedule Management ---
    fun calculateDevSchedule() {
        val idea = _activeIdea.value ?: return
        viewModelScope.launch {
            _creatureState.value = CreatureState.THINKING
            val tasks = SocraticBrain.calculateDevelopmentSchedule(
                idea.rawConcept,
                idea.domain,
                idea.id
            )
            repository.clearTasks(idea.id)
            repository.saveScheduleTasks(tasks)
            _creatureState.value = CreatureState.EXCITED
            delay(500)
            _creatureState.value = CreatureState.IDLE
            showToast("Calculated ${tasks.size} development tasks")
        }
    }

    fun toggleTaskCompleted(taskId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleTaskCompleted(taskId, isCompleted)
        }
    }

    fun addCustomTask(title: String, description: String, estimatedHours: Float, phase: String, effort: String) {
        val idea = _activeIdea.value ?: return
        viewModelScope.launch {
            val task = ScheduleTask(
                ideaId = idea.id,
                phase = phase,
                title = title,
                description = description,
                estimatedHours = estimatedHours,
                effortLevel = effort,
                cognitiveLoad = "Focused Flow",
                recommendedSprint = "Sprint 1",
                orderIndex = _scheduleTasks.value.size
            )
            repository.addTask(task)
            showToast("Task added to schedule")
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
            showToast("Task removed")
        }
    }

    // --- Work Reminders & Calendar ---
    fun setTaskReminder(context: Context, taskId: Long, triggerAtMillis: Long) {
        val task = _scheduleTasks.value.find { it.id == taskId } ?: return
        viewModelScope.launch {
            repository.setTaskReminder(taskId, triggerAtMillis)
            val scheduled = ReminderManager.scheduleTaskReminder(context, task, triggerAtMillis)
            val dateStr = SimpleDateFormat("EEE, MMM d · h:mm a", Locale.getDefault()).format(Date(triggerAtMillis))
            if (scheduled) {
                showToast("Reminder set for $dateStr")
            } else {
                showToast("Reminder saved ($dateStr)")
            }
        }
    }

    fun clearTaskReminder(context: Context, taskId: Long) {
        viewModelScope.launch {
            repository.setTaskReminder(taskId, null)
            ReminderManager.cancelTaskReminder(context, taskId)
            showToast("Reminder removed")
        }
    }

    fun openTaskInCalendar(context: Context, taskId: Long, startMillis: Long, endMillis: Long) {
        val task = _scheduleTasks.value.find { it.id == taskId } ?: return
        val idea = _activeIdea.value
        try {
            val intent = ReminderManager.createCalendarEventIntent(task, startMillis, endMillis, idea?.title)
            context.startActivity(intent)
        } catch (_: Exception) {
            showToast("No calendar app found on device.")
        }
    }

    fun exportRoadmapToCalendar(context: Context) {
        val tasks = _scheduleTasks.value
        val idea = _activeIdea.value
        if (tasks.isEmpty()) {
            showToast("No tasks to schedule. Tap 'Plan for Me' first.")
            return
        }
        val firstTask = tasks.firstOrNull { !it.isCompleted } ?: tasks.first()
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val start = calendar.timeInMillis
        val end = start + (firstTask.estimatedHours * 3600000).toLong().coerceAtLeast(3600000L)
        openTaskInCalendar(context, firstTask.id, start, end)
    }

    // --- Sovereign Cloud & File Export ---
    fun exportActiveIdeaToCloud(context: Context) {
        val idea = _activeIdea.value ?: return
        val swot = _activeSwot.value
        val tasks = _scheduleTasks.value
        val notes = allNotes.value.filter { it.sourceIdeaId == idea.id }

        val sb = StringBuilder()
        sb.append("# Loghy Sovereign Export: ${idea.title}\n\n")
        sb.append("**Privacy Commitment:** Zero AI Model Training · Sovereign Local Vault\n")
        sb.append("**Domain:** ${idea.domain} | **Status:** ${idea.status}\n\n")
        sb.append("## Raw Concept\n${idea.rawConcept}\n\n")

        if (swot != null) {
            sb.append("## 4-Angle Socratic Reality Check\n")
            sb.append("### What Holds Up (Strengths)\n")
            swot.whatHoldsUp.forEach { sb.append("- $it\n") }
            sb.append("\n### Vulnerable Spots (Weaknesses)\n")
            swot.vulnerableSpots.forEach { sb.append("- $it\n") }
            sb.append("\n### Outside Hurdles (Threats)\n")
            swot.outsideHurdles.forEach { sb.append("- $it\n") }
            sb.append("\n### Hidden Upside (Opportunities)\n")
            swot.hiddenUpside.forEach { sb.append("- $it\n") }
            if (swot.ethicsAndNeuroSustainability.isNotBlank()) {
                sb.append("\n### Ethics & Neuro-Sustainability\n${swot.ethicsAndNeuroSustainability}\n")
            }
            sb.append("\n")
        }

        if (tasks.isNotEmpty()) {
            val totalHours = tasks.sumOf { it.estimatedHours.toDouble() }
            sb.append("## Development Work & Programming Schedule\n")
            sb.append("**Total Dev Time:** ${String.format("%.1f", totalHours)} Hours across ${tasks.size} tasks\n\n")
            tasks.forEach { task ->
                val status = if (task.isCompleted) "[x]" else "[ ]"
                sb.append("$status **${task.phase}**: ${task.title} (${task.estimatedHours}h, Effort: ${task.effortLevel}, Load: ${task.cognitiveLoad})\n")
                if (task.description.isNotBlank()) {
                    sb.append("   ${task.description}\n")
                }
            }
            sb.append("\n")
        }

        if (notes.isNotEmpty()) {
            sb.append("## Attached Vault Notes\n")
            notes.forEach { note ->
                sb.append("### ${note.title}\n${note.content}\n\n")
            }
        }

        val exportContent = sb.toString()

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, exportContent)
            putExtra(Intent.EXTRA_SUBJECT, "Loghy Sovereign Export - ${idea.title}")
            type = "text/plain"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val chooser = Intent.createChooser(sendIntent, "Export Idea to Cloud or File").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(chooser)
        showToast("Opening export options...")
    }

    // --- Settings & Dialogs ---
    fun toggleSettingsDialog(show: Boolean) { _showSettings.value = show }
    fun toggleNewIdeaDialog(show: Boolean) { _showNewIdeaDialog.value = show }

    fun updateSettings(
        thinkerName: String,
        domain: String,
        ttsEnabled: Boolean,
        customKey: String,
        personalityStyle: String = _userProfile.value?.personalityStyle ?: "Cynical Best Friend",
        cynicismLevel: Float = _userProfile.value?.cynicismLevel ?: 0.75f,
        nerdinessLevel: Float = _userProfile.value?.nerdinessLevel ?: 0.80f
    ) {
        viewModelScope.launch {
            val current = _userProfile.value ?: UserProfile()
            val updated = current.copy(
                thinkerName = thinkerName.ifBlank { "Thinker" },
                defaultDomain = domain,
                isTtsEnabled = ttsEnabled,
                customGeminiApiKey = customKey.trim(),
                personalityStyle = personalityStyle,
                cynicismLevel = cynicismLevel,
                nerdinessLevel = nerdinessLevel
            )
            repository.updateProfile(updated)
            _userProfile.value = updated
            showToast("Settings updated")
            _showSettings.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioController.destroy()
    }

    companion object {
        val WelcomeAndUsesExplanation = """
            Hey there, genius! I'm Loghy — your fiercely honest thinking buddy, living 100% private and offline on your device.

            Here is how we work together:
            • 💬 Companion Chat: Bounce wild concepts off me; I’ll test fragile assumptions early.
            • 🎯 Reality Check: Honest SWOT stress-tests and falsification experiments.
            • ⚡ Lateral Thinking: Edward de Bono's lateral tools to jolt your brain out of autopilot.
            • 📝 Notes Vault: Encrypted offline notes with 20-version rollback history.
            • 📋 Hit List: Anti-burnout scheduling to stop procrastination and ship.

            What concept are we stress-testing today?
        """.trimIndent()

        val ArchitectureAndKeyMessage = """
            We are building an offline-first, zero-cost engine powered by your own API key or local models, supported by user-donated books and resources.
            To add your key, open Settings, paste your free Google AI Studio key, and it will be securely encrypted in your Android Keystore.
        """.trimIndent()
    }
}
