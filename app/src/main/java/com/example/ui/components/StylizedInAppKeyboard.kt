package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardCapslock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GhostInk
import com.example.ui.theme.InkBlack
import com.example.ui.theme.MutedInk
import com.example.ui.theme.PaperBase
import com.example.ui.theme.PaperDebossed
import com.example.ui.theme.PaperHighlight
import com.example.ui.theme.PaperSurface

/**
 * A tactile, Neumorphic In-App Keyboard.
 * Distinctive physical letterpress appearance avoiding generic OS system keyboard clashing.
 */
@Composable
fun StylizedInAppKeyboard(
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit,
    onSpace: () -> Unit,
    onSend: () -> Unit,
    onClose: () -> Unit,
    canSend: Boolean,
    onAttachArt: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var isShiftActive by remember { mutableStateOf(false) }
    var isSymbolsMode by remember { mutableStateOf(false) }

    val row1Letters = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
    val row2Letters = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")
    val row3Letters = listOf("z", "x", "c", "v", "b", "n", "m")

    val row1Numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    val row2Symbols = listOf("@", "#", "$", "%", "&", "-", "+", "(", ")", "/")
    val row3Symbols = listOf("*", "\"", "'", ":", ";", "!", "?")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 680.dp)
            .background(PaperBase)
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .testTag("stylized_in_app_keyboard")
    ) {
        // --- Top Quick-Action Strip ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuickActionChip(label = "PO:") {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onKeyPress("PO: ")
                }
                QuickActionChip(label = "Why?") {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onKeyPress("Why? ")
                }
                QuickActionChip(label = "What if?") {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onKeyPress("What if ")
                }
                QuickActionChip(label = "?") {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onKeyPress("?")
                }
                QuickActionChip(label = "—") {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onKeyPress(" — ")
                }
                QuickActionChip(label = "↵ Line") {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onKeyPress("\n")
                }
                if (onAttachArt != null) {
                    QuickActionChip(label = "🎨 Art") {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onAttachArt()
                    }
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                QuickActionChip(label = "⌨ System") {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClose()
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Clear",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = GhostInk,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onClear()
                        }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )

                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onClose()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Hide Keyboard",
                        tint = MutedInk,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // --- Row 1 ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            val keys = if (isSymbolsMode) row1Numbers else row1Letters
            keys.forEach { rawChar ->
                val char = if (isShiftActive && !isSymbolsMode) rawChar.uppercase() else rawChar
                StylizedKey(
                    label = char,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onKeyPress(char)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // --- Row 2 ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            val keys = if (isSymbolsMode) row2Symbols else row2Letters
            keys.forEach { rawChar ->
                val char = if (isShiftActive && !isSymbolsMode) rawChar.uppercase() else rawChar
                StylizedKey(
                    label = char,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onKeyPress(char)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // --- Row 3 ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shift / Mode key
            Box(
                modifier = Modifier
                    .weight(1.3f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isShiftActive) InkBlack else PaperSurface)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        isShiftActive = !isShiftActive
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardCapslock,
                    contentDescription = "Shift",
                    tint = if (isShiftActive) PaperBase else InkBlack,
                    modifier = Modifier.size(18.dp)
                )
            }

            val keys = if (isSymbolsMode) row3Symbols else row3Letters
            keys.forEach { rawChar ->
                val char = if (isShiftActive && !isSymbolsMode) rawChar.uppercase() else rawChar
                StylizedKey(
                    label = char,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onKeyPress(char)
                    }
                )
            }

            // Backspace key
            Box(
                modifier = Modifier
                    .weight(1.3f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PaperSurface)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onBackspace()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Backspace",
                    tint = InkBlack,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // --- Row 4: Mode, Comma, Spacebar, Dot, Send ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ?123 / ABC Toggle
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PaperSurface)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        isSymbolsMode = !isSymbolsMode
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isSymbolsMode) "ABC" else "?123",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = InkBlack
                )
            }

            // Comma
            StylizedKey(
                label = ",",
                modifier = Modifier.weight(0.9f),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onKeyPress(",")
                }
            )

            // Spacebar
            Box(
                modifier = Modifier
                    .weight(3.6f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PaperSurface)
                    .drawBehind {
                        // Subtle neumorphic top specular line
                        drawLine(
                            color = PaperHighlight.copy(alpha = 0.8f),
                            start = Offset(0f, 1f),
                            end = Offset(size.width, 1f),
                            strokeWidth = 1.5f
                        )
                    }
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onSpace()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "space",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                    color = GhostInk
                )
            }

            // Dot
            StylizedKey(
                label = ".",
                modifier = Modifier.weight(0.9f),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onKeyPress(".")
                }
            )

            // Send Button
            val sendBg by animateColorAsState(
                targetValue = if (canSend) InkBlack else PaperDebossed,
                animationSpec = tween(150),
                label = "sendBg"
            )
            Box(
                modifier = Modifier
                    .weight(1.4f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(sendBg)
                    .clickable(enabled = canSend) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSend()
                    }
                    .testTag("keyboard_send_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send Idea",
                    tint = if (canSend) PaperBase else GhostInk,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun StylizedKey(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1.0f,
        animationSpec = tween(70),
        label = "keyScale"
    )

    Box(
        modifier = modifier
            .height(44.dp)
            .scale(scale)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isPressed) PaperDebossed else PaperSurface)
            .drawBehind {
                if (!isPressed) {
                    // Top specular highlight
                    drawLine(
                        color = PaperHighlight.copy(alpha = 0.95f),
                        start = Offset(0f, 1f),
                        end = Offset(size.width, 1f),
                        strokeWidth = 1.5f
                    )
                    // Bottom ink shadow
                    drawLine(
                        color = Color.Black.copy(alpha = 0.14f),
                        start = Offset(0f, size.height - 1f),
                        end = Offset(size.width, size.height - 1f),
                        strokeWidth = 1.5f
                    )
                }
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif
            ),
            color = InkBlack
        )
    }
}

@Composable
private fun QuickActionChip(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(PaperSurface)
            .drawBehind {
                // Subtle specular highlight on top
                drawLine(
                    color = PaperHighlight.copy(alpha = 0.9f),
                    start = Offset(0f, 1f),
                    end = Offset(size.width, 1f),
                    strokeWidth = 1f
                )
                // Subtle shadow on bottom
                drawLine(
                    color = Color.Black.copy(alpha = 0.08f),
                    start = Offset(0f, size.height - 1f),
                    end = Offset(size.width, size.height - 1f),
                    strokeWidth = 1f
                )
            }
            .clickable(onClick = onClick)
            .padding(horizontal = 9.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif
            ),
            color = InkBlack
        )
    }
}
