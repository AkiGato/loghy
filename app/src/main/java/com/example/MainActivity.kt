package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import com.example.engine.ReminderManager
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.components.CreatureState
import com.example.ui.components.FluidBulletDroplet
import com.example.ui.components.FluidCreature
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicDebossedBox
import com.example.ui.components.NeumorphicRaisedBox
import com.example.ui.screens.AuthLockScreen
import com.example.ui.screens.BugAndCommentsReportDialog
import com.example.ui.screens.IdeaSelectorDialog
import com.example.ui.screens.LateralThinkingScreen
import com.example.ui.screens.NewIdeaDialog
import com.example.ui.screens.NotesVaultScreen
import com.example.ui.screens.PersonalityReactionDialog
import com.example.ui.screens.ScheduleScreen
import com.example.ui.screens.SettingsDialog
import com.example.ui.screens.StageChatStream
import com.example.ui.screens.SwotStudioScreen
import com.example.ui.theme.FaintInk
import com.example.ui.theme.GhostInk
import com.example.ui.theme.InkBlack
import com.example.ui.theme.MutedInk
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PaperBase
import com.example.ui.theme.PaperCard
import com.example.ui.theme.PaperSurface
import com.example.ui.viewmodel.LoghyViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: LoghyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                LoghyApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun LoghyApp(viewModel: LoghyViewModel) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val isAuthenticated by viewModel.isAuthenticated.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val pinInput by viewModel.pinInput.collectAsState()
    val pinError by viewModel.pinError.collectAsState()

    val currentTab by viewModel.currentTab.collectAsState()
    val activeIdea by viewModel.activeIdea.collectAsState()
    val allIdeas by viewModel.allIdeas.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val activeSwot by viewModel.activeSwot.collectAsState()
    val scheduleTasks by viewModel.scheduleTasks.collectAsState()

    val allNotes by viewModel.allNotes.collectAsState()
    val editingNote by viewModel.editingNote.collectAsState()
    val editingNoteSnapshots by viewModel.editingNoteSnapshots.collectAsState()
    val isEditingNote by viewModel.showNoteEditor.collectAsState()
    val editorStartWithHistory by viewModel.editorStartWithHistory.collectAsState()

    val lateralSessions by viewModel.lateralSessions.collectAsState()
    val activeLateralStimulus by viewModel.activeLateralStimulus.collectAsState()

    val creatureState by viewModel.creatureState.collectAsState()
    val gazeDirection by viewModel.gazeDirection.collectAsState()
    val activeInspiringThought by viewModel.activeInspiringThought.collectAsState()
    val activeThinkingQuote by viewModel.activeThinkingQuote.collectAsState()
    val personalityReaction by viewModel.personalityReaction.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val audioLevel by viewModel.audioLevel.collectAsState()

    val showSettings by viewModel.showSettings.collectAsState()
    val showFeedbackDialog by viewModel.showFeedbackDialog.collectAsState()
    val allFeedbackReports by viewModel.allFeedbackReports.collectAsState()
    val showNewIdeaDialog by viewModel.showNewIdeaDialog.collectAsState()
    var showIdeaSelector by remember { mutableStateOf(false) }

    var isSettingPinFlow by remember { mutableStateOf(false) }

    val toastNotice by viewModel.toastNotice.collectAsState()

    LaunchedEffect(toastNotice) {
        toastNotice?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissToast()
        }
    }

    // Audio record permission launcher
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startVoiceDictation()
        } else {
            Toast.makeText(context, "Microphone permission required for voice dictation", Toast.LENGTH_SHORT).show()
        }
    }

    // Notification permission launcher
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.showToast("Notifications enabled")
        }
    }

    // Deep navigation from notification intent
    LaunchedEffect(Unit) {
        val activity = context as? android.app.Activity
        val tab = activity?.intent?.getStringExtra("navigate_to_tab")
        if (tab == "HIT_LIST") {
            viewModel.selectTab(LoghyViewModel.ScreenTab.SCHEDULE)
        }
    }

    // --- Authentication Gate ---
    if (!isAuthenticated && userProfile?.isPinSet == true) {
        AuthLockScreen(
            thinkerName = userProfile?.thinkerName ?: "Thinker",
            pinInput = pinInput,
            pinError = pinError,
            isSettingPin = false,
            onDigitClick = { viewModel.enterPinDigit(it) },
            onDeleteClick = { viewModel.deletePinDigit() }
        )
        return
    }

    if (isSettingPinFlow) {
        AuthLockScreen(
            thinkerName = userProfile?.thinkerName ?: "Thinker",
            pinInput = pinInput,
            pinError = pinError,
            isSettingPin = true,
            onDigitClick = { viewModel.enterPinDigit(it) },
            onDeleteClick = { viewModel.deletePinDigit() },
            onSetupPinSubmit = { pin ->
                viewModel.setPin(pin)
                isSettingPinFlow = false
            }
        )
        return
    }

    // Handle Android system back gestures
    BackHandler(enabled = isEditingNote) {
        viewModel.closeNoteEditor()
    }
    BackHandler(enabled = !isEditingNote && showIdeaSelector) {
        showIdeaSelector = false
    }
    BackHandler(enabled = !isEditingNote && !showIdeaSelector && showSettings) {
        viewModel.toggleSettingsDialog(false)
    }
    BackHandler(enabled = !isEditingNote && !showIdeaSelector && !showSettings && showFeedbackDialog) {
        viewModel.toggleFeedbackDialog(false)
    }
    BackHandler(enabled = !isEditingNote && !showIdeaSelector && !showSettings && currentTab != LoghyViewModel.ScreenTab.STAGE) {
        viewModel.selectTab(LoghyViewModel.ScreenTab.STAGE)
    }

    // --- Main Application UI ---
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = PaperBase,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isCompactHeight = maxHeight < 580.dp
            val isWideScreen = maxWidth >= 840.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (isWideScreen) Modifier.widthIn(max = 840.dp).align(Alignment.TopCenter) else Modifier)
            ) {
                LoghyTopBar(
                    activeIdeaTitle = activeIdea?.title ?: "Select Idea",
                    domain = activeIdea?.domain ?: "Socratic",
                    isPinProtected = userProfile?.isPinSet == true,
                    onIdeaSelectorClick = { showIdeaSelector = true },
                    onSettingsClick = { viewModel.toggleSettingsDialog(true) },
                    onLockClick = { viewModel.lockApp() }
                )

                // Main character & Inspiring Thought / Thinking Bubble
                // In Companion screen, Loghy is the biggest; on compact/landscape or other screens, scales down gracefully
                val isCompanionScreen = currentTab == LoghyViewModel.ScreenTab.STAGE
                val characterTargetSize = when {
                    isCompactHeight -> if (isCompanionScreen) 64.dp else 40.dp
                    else -> if (isCompanionScreen) 130.dp else 68.dp
                }
                val characterSize by animateDpAsState(
                    targetValue = characterTargetSize,
                    animationSpec = spring(dampingRatio = 0.82f, stiffness = 300f),
                    label = "character_size"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = if (isCompactHeight) 1.dp else if (isCompanionScreen) 6.dp else 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FluidCreature(
                            sizeDp = characterSize,
                            state = creatureState,
                            gazeDirection = gazeDirection,
                            audioLevel = audioLevel,
                            onTap = {
                                viewModel.onCreatureTap()
                            }
                        )

                        // Inspiring thought or Loading/Thinking witty quote bubble next to Loghy
                        val isThinking = creatureState == CreatureState.THINKING
                        val hasThought = activeInspiringThought != null
                        val showBubble = isThinking || (isCompanionScreen && hasThought)

                        AnimatedVisibility(
                            visible = showBubble,
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut()
                        ) {
                            val bubbleText = if (isThinking) activeThinkingQuote else (activeInspiringThought ?: "")
                            if (bubbleText.isNotEmpty()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(start = 10.dp)
                                ) {
                                    LoghyThoughtBubble(
                                        text = bubbleText,
                                        isThinking = isThinking,
                                        onDismiss = { viewModel.dismissInspiringThought() }
                                    )
                                }
                            }
                        }
                    }
                }

                // Selection panel directly beneath character: responsive width with clean padding
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                LoghySelectionPanel(
                    selectedTab = currentTab,
                    onTabSelected = { viewModel.selectTab(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 520.dp)
                )
            }

            // Main Content Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_content_animation"
                ) { targetTab ->
                    when (targetTab) {
                        LoghyViewModel.ScreenTab.STAGE -> {
                            StageChatStream(
                                activeIdea = activeIdea,
                                messages = messages,
                                onSendMessage = { text, uri, bytes, mime ->
                                    viewModel.sendMessage(text, uri, bytes, mime)
                                },
                                onToggleTakeaway = { viewModel.toggleTakeaway(it) },
                                onTransformToNote = { viewModel.transformMessageToNote(it) },
                                onMultiChannelAnswer = { viewModel.requestMultiChannelAnswer(it) },
                                onSpeakMessage = { viewModel.audioController.speak(it) },
                                onStartVoice = {
                                    val hasPermission = ContextCompat.checkSelfPermission(
                                        context, Manifest.permission.RECORD_AUDIO
                                    ) == PackageManager.PERMISSION_GRANTED
                                    if (hasPermission) {
                                        viewModel.startVoiceDictation()
                                    } else {
                                        audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    }
                                },
                                onStopVoice = { viewModel.stopVoiceDictation() },
                                isListening = isListening,
                                onTypingChanged = { viewModel.setUserTyping(it) },
                                onOpenFeedbackReport = { viewModel.toggleFeedbackDialog(true) }
                            )
                        }
                        LoghyViewModel.ScreenTab.SWOT -> {
                            SwotStudioScreen(
                                activeIdea = activeIdea,
                                swot = activeSwot,
                                onRefreshSwot = { viewModel.refreshSwot() },
                                onCaptureTakeaway = { title, content ->
                                    viewModel.captureContentToVault(title, content)
                                }
                            )
                        }
                        LoghyViewModel.ScreenTab.LATERAL -> {
                            LateralThinkingScreen(
                                activeIdea = activeIdea,
                                activeStimulus = activeLateralStimulus,
                                history = lateralSessions,
                                onTriggerRandomWord = { viewModel.triggerRandomWord() },
                                onTriggerProvocation = { viewModel.triggerProvocation() },
                                onTriggerReversal = { viewModel.triggerReversal() },
                                onTriggerHat = { viewModel.triggerHat(it) },
                                onSaveReflection = { viewModel.saveLateralReflection(it) }
                            )
                        }
                        LoghyViewModel.ScreenTab.SCHEDULE -> {
                            ScheduleScreen(
                                idea = activeIdea,
                                tasks = scheduleTasks,
                                onToggleCompleted = { taskId, done ->
                                    viewModel.toggleTaskCompleted(taskId, done)
                                },
                                onDeleteTask = { taskId ->
                                    viewModel.deleteTask(taskId)
                                },
                                onRecalculateSchedule = {
                                    viewModel.calculateDevSchedule()
                                },
                                onAddTask = { title, desc, hours, phase, effort ->
                                    viewModel.addCustomTask(title, desc, hours, phase, effort)
                                },
                                onExportToCloud = {
                                    viewModel.exportActiveIdeaToCloud(context)
                                },
                                onSetReminder = { taskId, triggerAtMillis ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                        !ReminderManager.hasNotificationPermission(context)
                                    ) {
                                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                    viewModel.setTaskReminder(context, taskId, triggerAtMillis)
                                },
                                onClearReminder = { taskId ->
                                    viewModel.clearTaskReminder(context, taskId)
                                },
                                onOpenCalendar = { taskId, start, end ->
                                    viewModel.openTaskInCalendar(context, taskId, start, end)
                                },
                                onSyncToCalendar = {
                                    viewModel.exportRoadmapToCalendar(context)
                                }
                            )
                        }
                        LoghyViewModel.ScreenTab.VAULT -> {
                            NotesVaultScreen(
                                notes = allNotes,
                                editingNote = editingNote,
                                snapshots = editingNoteSnapshots,
                                isEditing = isEditingNote,
                                initialShowSnapshots = editorStartWithHistory,
                                onOpenNote = { viewModel.openNoteEditor(it, startWithHistory = false) },
                                onOpenNoteHistory = { viewModel.openNoteEditor(it, startWithHistory = true) },
                                onCloseEditor = { viewModel.closeNoteEditor() },
                                onSaveNote = { title, content, paperTone, tags ->
                                    viewModel.saveNote(title, content, paperTone, tags)
                                },
                                onRollback = { snapshot ->
                                    viewModel.rollbackNote(snapshot)
                                },
                                onDeleteNote = { noteId ->
                                    viewModel.deleteNote(noteId)
                                }
                            )
                        }
                    }
                }
            }
    }

    // --- Dialogs ---
        if (showSettings) {
            SettingsDialog(
                profile = userProfile,
                onDismiss = { viewModel.toggleSettingsDialog(false) },
                onSaveSettings = { name, domain, tts, key, style, cynicism, nerdiness ->
                    viewModel.updateSettings(name, domain, tts, key, style, cynicism, nerdiness)
                },
                onAdjustPersonality = { style, cynicism, nerdiness ->
                    viewModel.reactToPersonalityChange(style, cynicism, nerdiness)
                },
                onLockApp = {
                    viewModel.toggleSettingsDialog(false)
                    viewModel.lockApp()
                },
                onSetPinClick = {
                    viewModel.toggleSettingsDialog(false)
                    isSettingPinFlow = true
                },
                onRemovePinClick = {
                    viewModel.removePin()
                },
                onOpenFeedbackReports = {
                    viewModel.toggleSettingsDialog(false)
                    viewModel.toggleFeedbackDialog(true)
                }
            )
        }

        personalityReaction?.let { reaction ->
            PersonalityReactionDialog(
                reactionText = reaction,
                onDismiss = { viewModel.dismissPersonalityReaction() }
            )
        }

        if (showNewIdeaDialog) {
            NewIdeaDialog(
                onDismiss = { viewModel.toggleNewIdeaDialog(false) },
                onCreateIdea = { title, concept, domain ->
                    viewModel.createNewIdea(title, concept, domain)
                }
            )
        }

        if (showIdeaSelector) {
            IdeaSelectorDialog(
                ideas = allIdeas,
                activeIdeaId = activeIdea?.id,
                onSelectIdea = { viewModel.selectIdea(it) },
                onNewIdeaClick = { viewModel.toggleNewIdeaDialog(true) },
                onDeleteCurrentIdea = { viewModel.deleteCurrentIdea() },
                onDismiss = { showIdeaSelector = false }
            )
        }

        if (showFeedbackDialog) {
            BugAndCommentsReportDialog(
                feedbackReports = allFeedbackReports,
                onDismiss = { viewModel.toggleFeedbackDialog(false) },
                onSubmitReport = { type, content ->
                    viewModel.submitFeedbackReport(type, content)
                },
                onDeleteReport = { reportId ->
                    viewModel.deleteFeedbackReport(reportId)
                },
                onTriggerPromptInChat = {
                    viewModel.triggerFeedbackReportPrompt()
                }
            )
        }
    }
}
}

@Composable
private fun LoghyTopBar(
    activeIdeaTitle: String,
    domain: String,
    isPinProtected: Boolean,
    onIdeaSelectorClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLockClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Loghy Brand with Ink Droplet
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.testTag("app_brand_logo")
        ) {
            FluidBulletDroplet(sizeDp = 12.dp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "LOGHY",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                color = InkBlack
            )
        }

        // Active Idea Pill Selector
        NeumorphicRaisedBox(
            modifier = Modifier
                .clickable { onIdeaSelectorClick() }
                .testTag("top_bar_idea_selector"),
            shape = RoundedCornerShape(20.dp),
            elevation = 2.5.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = activeIdeaTitle,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = InkBlack,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "▾",
                    style = MaterialTheme.typography.labelSmall,
                    color = GhostInk
                )
            }
        }

        // Action Buttons (Lock + Settings)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (isPinProtected) {
                NeumorphicButton(
                    onClick = onLockClick,
                    modifier = Modifier
                        .size(38.dp)
                        .testTag("top_bar_lock"),
                    shape = RoundedCornerShape(19.dp),
                    contentPadding = 0.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock App",
                        tint = InkBlack,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            NeumorphicButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(38.dp)
                    .testTag("top_bar_settings"),
                shape = RoundedCornerShape(19.dp),
                contentPadding = 0.dp
            ) {
                Icon(
                    imageVector = Icons.Default.BubbleChart,
                    contentDescription = "Settings",
                    tint = InkBlack,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun LoghySelectionPanel(
    selectedTab: LoghyViewModel.ScreenTab,
    onTabSelected: (LoghyViewModel.ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NeumorphicDebossedBox(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(27.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tabs = listOf(
                Triple(LoghyViewModel.ScreenTab.STAGE, Icons.Default.BubbleChart, "Chat"),
                Triple(LoghyViewModel.ScreenTab.SWOT, Icons.Default.Shield, "SWOT"),
                Triple(LoghyViewModel.ScreenTab.LATERAL, Icons.Default.Psychology, "Lateral"),
                Triple(LoghyViewModel.ScreenTab.SCHEDULE, Icons.Default.Schedule, "Hit List"),
                Triple(LoghyViewModel.ScreenTab.VAULT, Icons.Default.FolderSpecial, "Vault")
            )

            tabs.forEach { (tab, icon, label) ->
                val isSelected = selectedTab == tab
                val testTagSuffix = when (tab) {
                    LoghyViewModel.ScreenTab.STAGE -> "companion"
                    LoghyViewModel.ScreenTab.SWOT -> "swot"
                    LoghyViewModel.ScreenTab.LATERAL -> "lateral"
                    LoghyViewModel.ScreenTab.SCHEDULE -> "schedule"
                    LoghyViewModel.ScreenTab.VAULT -> "folders"
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) InkBlack else PaperSurface.copy(alpha = 0f))
                        .clickable { onTabSelected(tab) }
                        .testTag("tab_$testTagSuffix"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isSelected) PaperSurface else GhostInk,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = if (isSelected) PaperSurface else MutedInk,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoghyThoughtBubble(
    text: String,
    isThinking: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    NeumorphicRaisedBox(
        modifier = modifier
            .widthIn(max = 210.dp)
            .clickable(onClick = onDismiss)
            .testTag("loghy_thought_bubble"),
        shape = RoundedCornerShape(14.dp),
        backgroundColor = PaperSurface,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = if (isThinking) Icons.Default.Psychology else Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = InkBlack,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = if (isThinking) "LOGHY PONDERS" else "WHISPERED SPARK",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    ),
                    color = GhostInk
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.5.sp,
                    lineHeight = 15.5.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium
                ),
                color = InkBlack
            )
        }
    }
}


