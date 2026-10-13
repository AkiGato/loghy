package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.KeyboardHide
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.BrainstormIdea
import com.example.data.model.SocraticMessage
import com.example.ui.components.FluidBulletDroplet
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicDebossedBox
import com.example.ui.components.NeumorphicRaisedBox
import com.example.ui.components.StylizedInAppKeyboard
import com.example.ui.components.UndulatingInkUnderline
import com.example.ui.theme.FaintInk
import com.example.ui.theme.GhostInk
import com.example.ui.theme.InkBlack
import com.example.ui.theme.MutedInk
import com.example.ui.theme.PaperBase
import com.example.ui.theme.PaperCard
import com.example.ui.theme.PaperDebossed
import com.example.ui.theme.PaperSurface

@Composable
fun StageChatStream(
    activeIdea: BrainstormIdea?,
    messages: List<SocraticMessage>,
    onSendMessage: (text: String, imageUri: String?, imageBytes: ByteArray?, mimeType: String) -> Unit,
    onToggleTakeaway: (SocraticMessage) -> Unit,
    onTransformToNote: (SocraticMessage) -> Unit,
    onMultiChannelAnswer: (promptOrMessage: String?) -> Unit,
    onSpeakMessage: (String) -> Unit,
    onStartVoice: () -> Unit,
    onStopVoice: () -> Unit,
    isListening: Boolean,
    onTypingChanged: (Boolean) -> Unit = {},
    onOpenFeedbackReport: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    var isKeyboardVisible by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var selectedImageMimeType by remember { mutableStateOf("image/jpeg") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        selectedImageUri = uri
        if (uri != null) {
            try {
                val stream = context.contentResolver.openInputStream(uri)
                selectedImageBytes = stream?.readBytes()
                selectedImageMimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
            } catch (_: Exception) {
                selectedImageBytes = null
            }
        } else {
            selectedImageBytes = null
        }
    }

    val listState = rememberLazyListState()
    val haptic = LocalHapticFeedback.current

    // Notify typing state to adjust Loghy's gaze to look down at keyboard
    LaunchedEffect(inputText) {
        onTypingChanged(inputText.isNotEmpty())
    }

    // Scroll to latest message
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBase)
    ) {
        // --- Continuous Chronological Chat Stream ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (messages.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    FluidBulletDroplet(sizeDp = 18.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Dialogue is clear and waiting.",
                        style = MaterialTheme.typography.titleMedium,
                        color = InkBlack
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Share an assumption, question, or attach artwork for critique.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GhostInk
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(messages, key = { it.id }) { message ->
                        val isUser = message.sender == "USER"

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                        ) {
                            NeumorphicRaisedBox(
                                modifier = Modifier
                                    .fillMaxWidth(if (isUser) 0.84f else 0.94f)
                                    .pointerInput(message.id) {
                                        detectTapGestures(
                                            onDoubleTap = {
                                                onToggleTakeaway(message)
                                            }
                                        )
                                    }
                                    .testTag("chat_message_${message.id}"),
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isUser) 16.dp else 4.dp,
                                    bottomEnd = if (isUser) 4.dp else 16.dp
                                ),
                                backgroundColor = if (isUser) PaperCard else PaperSurface,
                                elevation = if (message.isKeyTakeaway) 6.dp else 3.dp
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    // Header
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            FluidBulletDroplet(
                                                sizeDp = 8.dp,
                                                tint = if (isUser) GhostInk else InkBlack
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (isUser) "THINKER" else "LOGHY",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    letterSpacing = 1.sp,
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = if (isUser) GhostInk else InkBlack
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (!isUser) {
                                                IconButton(
                                                    onClick = { onSpeakMessage(message.content) },
                                                    modifier = Modifier.size(24.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                                        contentDescription = "Speak takeaway",
                                                        tint = GhostInk,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(4.dp))
                                            }

                                            IconButton(
                                                onClick = { onToggleTakeaway(message) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (message.isKeyTakeaway) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                                    contentDescription = "Bookmark",
                                                    tint = if (message.isKeyTakeaway) InkBlack else MutedInk,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Attached Artwork (if present)
                                    if (!message.imageUri.isNullOrBlank()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(PaperDebossed)
                                        ) {
                                            AsyncImage(
                                                model = message.imageUri,
                                                contentDescription = "Attached artwork",
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(max = 240.dp),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }

                                    // Message content
                                    Text(
                                        text = message.content,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            lineHeight = 22.sp,
                                            fontFamily = if (isUser) FontFamily.SansSerif else FontFamily.Serif
                                        ),
                                        color = InkBlack
                                    )

                                    if (message.isKeyTakeaway) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        UndulatingInkUnderline(
                                            heightDp = 2.5.dp,
                                            color = InkBlack.copy(alpha = 0.35f)
                                        )
                                        Text(
                                            text = "Saved in Vault",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MutedInk,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }

                                    // --- Options beneath messages ---
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Option 1: Transform to notes
                                        MessageOptionChip(
                                            icon = Icons.Default.Description,
                                            label = "Transform to notes",
                                            onClick = {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                onTransformToNote(message)
                                            }
                                        )

                                        // Option 2: Multi-channel answer (all together: swot, lateral, logical)
                                        MessageOptionChip(
                                            icon = Icons.Default.AutoAwesome,
                                            label = "Multi-channel answer",
                                            isHighlight = true,
                                            onClick = {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                onMultiChannelAnswer(message.content)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- Quick Actions Bar above Input ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlobalActionChip(
                icon = Icons.Default.AutoAwesome,
                label = "Multi-channel answer",
                onClick = { onMultiChannelAnswer(null) }
            )
            GlobalActionChip(
                icon = Icons.Default.Description,
                label = "Transform to notes",
                onClick = {
                    val lastMsg = messages.lastOrNull()
                    if (lastMsg != null) {
                        onTransformToNote(lastMsg)
                    } else if (activeIdea != null) {
                        onTransformToNote(
                            SocraticMessage(
                                ideaId = activeIdea.id,
                                sender = "IDEA",
                                content = "${activeIdea.title} (${activeIdea.domain}):\n${activeIdea.rawConcept}"
                            )
                        )
                    }
                }
            )
            if (onOpenFeedbackReport != null) {
                NeumorphicButton(
                    onClick = onOpenFeedbackReport,
                    shape = CircleShape,
                    contentPadding = 6.dp,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("bug_report_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.BugReport,
                        contentDescription = "Bug report",
                        tint = InkBlack,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // --- Selected Artwork Attachment Banner ---
        AnimatedVisibility(
            visible = selectedImageUri != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (selectedImageUri != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NeumorphicRaisedBox(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = PaperCard,
                        elevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Artwork preview",
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(PaperDebossed),
                                    contentScale = ContentScale.Crop
                                )
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Palette,
                                            contentDescription = null,
                                            tint = InkBlack,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Art Attached for Feedback",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = InkBlack
                                        )
                                    }
                                    Text(
                                        text = "Socratic critique • Local & private",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp
                                        ),
                                        color = MutedInk
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    selectedImageUri = null
                                    selectedImageBytes = null
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove attached image",
                                    tint = GhostInk,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- Multi-Line Full-Visibility Input Chamber ---
        val canSend = inputText.isNotBlank() || selectedImageUri != null
        val performSend = {
            if (canSend) {
                val uriStr = selectedImageUri?.toString()
                val bytes = selectedImageBytes
                val mime = selectedImageMimeType
                onSendMessage(inputText, uriStr, bytes, mime)
                inputText = ""
                selectedImageUri = null
                selectedImageBytes = null
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Art/Photo attachment button (Android zero-permission photo picker)
            NeumorphicButton(
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier
                    .size(48.dp)
                    .testTag("attach_art_button"),
                shape = CircleShape,
                backgroundColor = if (selectedImageUri != null) PaperDebossed else PaperSurface,
                contentPadding = 0.dp
            ) {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "Attach artwork for feedback",
                    tint = if (selectedImageUri != null) InkBlack else GhostInk,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Voice input mic button
            NeumorphicButton(
                onClick = {
                    if (isListening) onStopVoice() else onStartVoice()
                },
                modifier = Modifier
                    .size(48.dp)
                    .testTag("voice_input_button"),
                shape = CircleShape,
                backgroundColor = if (isListening) PaperCard else PaperSurface,
                contentPadding = 0.dp
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = "Voice Input",
                    tint = if (isListening) MaterialTheme.colorScheme.error else InkBlack,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Spacious Debossed Box for multi-line full message visibility
            NeumorphicDebossedBox(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp, max = 130.dp),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = PaperDebossed
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = InkBlack,
                            lineHeight = 22.sp,
                            fontSize = 15.sp
                        ),
                        cursorBrush = SolidColor(InkBlack),
                        singleLine = false,
                        maxLines = 6,
                        decorationBox = { innerTextField ->
                            if (inputText.isEmpty() && selectedImageUri == null) {
                                Text(
                                    text = "Type an assumption, question, or attach art...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MutedInk
                                )
                            } else if (inputText.isEmpty() && selectedImageUri != null) {
                                Text(
                                    text = "Add an optional prompt for your artwork...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MutedInk
                                )
                            }
                            innerTextField()
                        }
                    )

                    // Toggle in-app keyboard button
                    IconButton(
                        onClick = { isKeyboardVisible = !isKeyboardVisible },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("keyboard_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isKeyboardVisible) Icons.Default.KeyboardHide else Icons.Default.Keyboard,
                            contentDescription = if (isKeyboardVisible) "Hide keyboard" else "Show keyboard",
                            tint = if (isKeyboardVisible) InkBlack else GhostInk,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Send Button
            NeumorphicButton(
                onClick = { performSend() },
                modifier = Modifier
                    .size(48.dp)
                    .testTag("send_message_button"),
                shape = CircleShape,
                enabled = canSend,
                contentPadding = 0.dp
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (canSend) InkBlack else MutedInk,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // --- Stylized In-App Keyboard ---
        AnimatedVisibility(
            visible = isKeyboardVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            StylizedInAppKeyboard(
                onKeyPress = { inputText += it },
                onBackspace = {
                    if (inputText.isNotEmpty()) {
                        inputText = inputText.dropLast(1)
                    }
                },
                onClear = { inputText = "" },
                onSpace = { inputText += " " },
                onSend = { performSend() },
                onClose = { isKeyboardVisible = false },
                canSend = canSend,
                onAttachArt = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )
        }
    }
}

@Composable
private fun MessageOptionChip(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isHighlight: Boolean = false
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isHighlight) PaperDebossed else PaperBase)
            .border(
                width = 0.8.dp,
                color = if (isHighlight) InkBlack.copy(alpha = 0.25f) else FaintInk,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 9.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isHighlight) InkBlack else MutedInk,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = if (isHighlight) FontWeight.SemiBold else FontWeight.Medium
                ),
                color = if (isHighlight) InkBlack else MutedInk
            )
        }
    }
}

@Composable
private fun GlobalActionChip(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(PaperSurface)
            .border(0.6.dp, FaintInk, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = InkBlack,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = InkBlack
            )
        }
    }
}
