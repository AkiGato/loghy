package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VolunteerActivism
import com.example.data.model.FeedbackReport
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.ui.theme.SwotThreatTint
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.BrainstormIdea
import com.example.data.model.UserProfile
import com.example.engine.SocraticBrain
import com.example.ui.components.CreatureState
import com.example.ui.components.FluidBulletDroplet
import com.example.ui.components.FluidCreature
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicDebossedBox
import com.example.ui.components.NeumorphicRaisedBox
import com.example.ui.components.UndulatingInkUnderline
import com.example.ui.theme.FaintInk
import com.example.ui.theme.GhostInk
import com.example.ui.theme.InkBlack
import com.example.ui.theme.MutedInk
import com.example.ui.theme.PaperBase
import com.example.ui.theme.PaperCard
import com.example.ui.theme.PaperSurface
import com.example.ui.theme.SwotStrengthTint
import com.example.ui.theme.SwotWeaknessTint

@Composable
fun SettingsDialog(
    profile: UserProfile?,
    onDismiss: () -> Unit,
    onSaveSettings: (thinkerName: String, domain: String, ttsEnabled: Boolean, customKey: String, personalityStyle: String, cynicismLevel: Float, nerdinessLevel: Float) -> Unit,
    onAdjustPersonality: (style: String, cynicism: Float, nerdiness: Float) -> Unit,
    onLockApp: () -> Unit,
    onSetPinClick: () -> Unit,
    onRemovePinClick: () -> Unit,
    onOpenFeedbackReports: () -> Unit = {}
) {
    var thinkerName by remember { mutableStateOf(profile?.thinkerName ?: "Thinker") }
    var selectedDomain by remember { mutableStateOf(profile?.defaultDomain ?: "Software / SaaS") }
    var ttsEnabled by remember { mutableStateOf(profile?.isTtsEnabled ?: true) }
    var customKey by remember { mutableStateOf(profile?.customGeminiApiKey ?: "") }
    var personalityStyle by remember { mutableStateOf(profile?.personalityStyle ?: "Cynical Best Friend") }
    var cynicismLevel by remember { mutableStateOf(profile?.cynicismLevel ?: 0.75f) }
    var nerdinessLevel by remember { mutableStateOf(profile?.nerdinessLevel ?: 0.80f) }
    var showKnowledgeDonationsDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        NeumorphicRaisedBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = PaperSurface,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SETTINGS & SECURITY",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = InkBlack
                    )
                    NeumorphicButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = 0.dp
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = InkBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                UndulatingInkUnderline(
                    modifier = Modifier.padding(vertical = 8.dp),
                    heightDp = 3.dp
                )

                // Thinker Name
                Text(
                    text = "Thinker Identity",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = GhostInk
                )
                Spacer(modifier = Modifier.height(4.dp))
                NeumorphicDebossedBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    BasicTextField(
                        value = thinkerName,
                        onValueChange = { thinkerName = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .testTag("settings_thinker_name_input"),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = InkBlack),
                        cursorBrush = SolidColor(InkBlack),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Default Domain Focus
                Text(
                    text = "Default Domain Heuristic",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = GhostInk
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SocraticBrain.AvailableDomains.take(3).forEach { d ->
                        val isSelected = d == selectedDomain
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) InkBlack else PaperCard)
                                .clickable { selectedDomain = d }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = d.split(" ")[0],
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) PaperSurface else InkBlack,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // TTS Voice Output Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Spoken Socratic Reflections",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = InkBlack
                        )
                        Text(
                            text = "Text-to-speech audio feedback",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedInk
                        )
                    }
                    Switch(
                        checked = ttsEnabled,
                        onCheckedChange = { ttsEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PaperSurface,
                            checkedTrackColor = InkBlack,
                            uncheckedThumbColor = GhostInk,
                            uncheckedTrackColor = PaperCard
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4-Digit PIN Vault Security
                Text(
                    text = "Vault Protection (Salted PBKDF2)",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = GhostInk
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (profile?.isPinSet == true) {
                        NeumorphicButton(
                            onClick = onLockApp,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("lock_vault_now_button"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = 8.dp
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = "Lock", tint = InkBlack, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Lock Now", style = MaterialTheme.typography.labelSmall, color = InkBlack)
                            }
                        }

                        NeumorphicButton(
                            onClick = onRemovePinClick,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("remove_pin_button"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = 8.dp
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LockOpen, contentDescription = "Remove PIN", tint = InkBlack, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Disable PIN", style = MaterialTheme.typography.labelSmall, color = InkBlack)
                            }
                        }
                    } else {
                        NeumorphicButton(
                            onClick = onSetPinClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("setup_pin_button"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = 8.dp
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Security, contentDescription = "Set PIN", tint = InkBlack, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Enable 4-Digit PIN Vault", style = MaterialTheme.typography.labelSmall, color = InkBlack)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bring Your Own Key (BYOK)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bring Your Own Key (BYOK)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = GhostInk
                    )
                    Text(
                        text = "Get free key ↗",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = com.example.ui.theme.SwotStrengthTint
                        ),
                        modifier = Modifier
                            .clickable {
                                try {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://aistudio.google.com/app/apikey")))
                                } catch (_: Exception) {}
                            }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = "Encrypted in Android Keystore. Zero data leaves your phone. Leave blank for 100% offline engine.",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.5.sp),
                    color = MutedInk
                )
                Spacer(modifier = Modifier.height(4.dp))
                NeumorphicDebossedBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    BasicTextField(
                        value = customKey,
                        onValueChange = { customKey = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .testTag("settings_custom_key_input"),
                        textStyle = MaterialTheme.typography.bodySmall.copy(color = InkBlack),
                        cursorBrush = SolidColor(InkBlack),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (customKey.isEmpty()) {
                                Text(
                                    text = "Paste free Google AI Studio key",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedInk
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // --- Loghy Personality & Humor Matrix ---
                Text(
                    text = "LOGHY PERSONALITY & HUMOR MATRIX",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = GhostInk
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "A loyal, friendly companion with dark, nerdy and cynical wit.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedInk
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Style Selector Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SocraticBrain.PersonalityStyles.forEach { style ->
                        val isSelected = style == personalityStyle
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) InkBlack else PaperCard)
                                .clickable {
                                    personalityStyle = style
                                    onAdjustPersonality(personalityStyle, cynicismLevel, nerdinessLevel)
                                }
                                .padding(vertical = 6.dp, horizontal = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = style.split(" ").take(2).joinToString(" "),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = if (isSelected) PaperSurface else InkBlack,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Cynicism Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cynicism & Dark Wit",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = InkBlack
                    )
                    Text(
                        text = "${(cynicismLevel * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = GhostInk
                    )
                }
                Slider(
                    value = cynicismLevel,
                    onValueChange = { cynicismLevel = it },
                    onValueChangeFinished = {
                        onAdjustPersonality(personalityStyle, cynicismLevel, nerdinessLevel)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = InkBlack,
                        activeTrackColor = InkBlack,
                        inactiveTrackColor = PaperCard
                    )
                )

                // Nerdiness Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nerdy Depth & First Principles",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = InkBlack
                    )
                    Text(
                        text = "${(nerdinessLevel * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = GhostInk
                    )
                }
                Slider(
                    value = nerdinessLevel,
                    onValueChange = { nerdinessLevel = it },
                    onValueChangeFinished = {
                        onAdjustPersonality(personalityStyle, cynicismLevel, nerdinessLevel)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = InkBlack,
                        activeTrackColor = InkBlack,
                        inactiveTrackColor = PaperCard
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                var showGuidelinesDialog by remember { mutableStateOf(false) }
                var showEthicalCodeDialog by remember { mutableStateOf(false) }
                var showAboutAndDonationsDialog by remember { mutableStateOf(false) }

                Text(
                    text = "CORE ETHICS, ORIGIN & SUPPORT",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = GhostInk
                )
                Spacer(modifier = Modifier.height(6.dp))

                // 1. The Ethical Code & Classical Library Button
                NeumorphicButton(
                    onClick = { showEthicalCodeDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open_ethical_code_manifesto_button"),
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = PaperCard,
                    contentPadding = 8.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = InkBlack,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "The Ethical Code & Classical Library",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = InkBlack
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 2. Neuroethical & Cognitive Interface Guidelines Button
                NeumorphicButton(
                    onClick = { showGuidelinesDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open_neuroethical_guidelines_button"),
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = PaperCard,
                    contentPadding = 8.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = com.example.ui.theme.SwotStrengthTint,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Neuroethical & Cognitive Guidelines",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = InkBlack
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 3. About Loghy & Donations Button (Separate Modal)
                NeumorphicButton(
                    onClick = { showAboutAndDonationsDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open_about_loghy_donations_button"),
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = PaperCard,
                    contentPadding = 8.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = com.example.ui.theme.SwotWeaknessTint,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "About Loghy & Donations",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = InkBlack
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 4. Bug & Comments Reports Button
                NeumorphicButton(
                    onClick = {
                        onOpenFeedbackReports()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open_bug_comments_reports_button"),
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = PaperCard,
                    contentPadding = 8.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.BugReport,
                            contentDescription = null,
                            tint = InkBlack,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Bug & Comments Reports",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = InkBlack
                        )
                    }
                }

                if (showEthicalCodeDialog) {
                    EthicalCodeAndOriginDialog(onDismiss = { showEthicalCodeDialog = false })
                }

                if (showGuidelinesDialog) {
                    NeuroethicalGuidelinesDialog(onDismiss = { showGuidelinesDialog = false })
                }

                if (showAboutAndDonationsDialog) {
                    AboutLoghyAndDonationsDialog(onDismiss = { showAboutAndDonationsDialog = false })
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Save Changes Button
                NeumorphicButton(
                    onClick = {
                        onSaveSettings(
                            thinkerName,
                            selectedDomain,
                            ttsEnabled,
                            customKey,
                            personalityStyle,
                            cynicismLevel,
                            nerdinessLevel
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_settings_button"),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = 10.dp
                ) {
                    Text(
                        text = "Save Preferences",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = InkBlack
                    )
                }
            }
        }

        if (showKnowledgeDonationsDialog) {
            KnowledgeDonationsDialog(onDismiss = { showKnowledgeDonationsDialog = false })
        }
    }
}

@Composable
fun PersonalityReactionDialog(
    reactionText: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        NeumorphicRaisedBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = PaperSurface,
            elevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FluidCreature(
                    sizeDp = 86.dp,
                    state = CreatureState.EXCITED,
                    audioLevel = 0.5f,
                    onTap = onDismiss
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "LOGHY OBJECTS!",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = GhostInk
                )

                Spacer(modifier = Modifier.height(8.dp))

                NeumorphicDebossedBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = reactionText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 22.sp
                        ),
                        color = InkBlack,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                NeumorphicButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dismiss_personality_reaction_button"),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = 10.dp
                ) {
                    Text(
                        text = "Deal, Loghy!",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = InkBlack
                    )
                }
            }
        }
    }
}

@Composable
fun NewIdeaDialog(
    onDismiss: () -> Unit,
    onCreateIdea: (title: String, concept: String, domain: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var concept by remember { mutableStateOf("") }
    var selectedDomain by remember { mutableStateOf(SocraticBrain.AvailableDomains[0]) }

    Dialog(onDismissRequest = onDismiss) {
        NeumorphicRaisedBox(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 520.dp)
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = PaperSurface,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "NEW THINKING SESSION",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = InkBlack
                    )
                    NeumorphicButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = 0.dp
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = InkBlack, modifier = Modifier.size(16.dp))
                    }
                }

                UndulatingInkUnderline(
                    modifier = Modifier.padding(vertical = 8.dp),
                    heightDp = 3.dp
                )

                // Idea Title
                Text(
                    text = "Working Name / Hypothesis",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = GhostInk
                )
                Spacer(modifier = Modifier.height(4.dp))
                NeumorphicDebossedBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .testTag("new_idea_title_input"),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = InkBlack),
                        cursorBrush = SolidColor(InkBlack),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Domain Selection
                Text(
                    text = "Domain Heuristic",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = GhostInk
                )
                Spacer(modifier = Modifier.height(4.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    SocraticBrain.AvailableDomains.forEach { d ->
                        val isSelected = d == selectedDomain
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) InkBlack else PaperCard)
                                .clickable { selectedDomain = d }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FluidBulletDroplet(
                                sizeDp = 6.dp,
                                tint = if (isSelected) PaperSurface else GhostInk
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = d,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) PaperSurface else InkBlack,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Premise / Concept
                Text(
                    text = "Core Concept / Proposition",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = GhostInk
                )
                Spacer(modifier = Modifier.height(4.dp))
                NeumorphicDebossedBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    BasicTextField(
                        value = concept,
                        onValueChange = { concept = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .testTag("new_idea_concept_input"),
                        textStyle = MaterialTheme.typography.bodySmall.copy(color = InkBlack),
                        cursorBrush = SolidColor(InkBlack),
                        decorationBox = { innerTextField ->
                            if (concept.isEmpty()) {
                                Text(
                                    text = "What is the premise, mechanism, or customer pain?",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedInk
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                NeumorphicButton(
                    onClick = {
                        if (title.isNotBlank() || concept.isNotBlank()) {
                            onCreateIdea(title, concept, selectedDomain)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_new_idea_button"),
                    shape = RoundedCornerShape(12.dp),
                    enabled = title.isNotBlank() || concept.isNotBlank(),
                    contentPadding = 10.dp
                ) {
                    Text(
                        text = "Begin Socratic Examination",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = InkBlack
                    )
                }
            }
        }
    }
}

@Composable
fun IdeaSelectorDialog(
    ideas: List<BrainstormIdea>,
    activeIdeaId: Long?,
    onSelectIdea: (BrainstormIdea) -> Unit,
    onNewIdeaClick: () -> Unit,
    onDeleteCurrentIdea: () -> Unit,
    onDismiss: () -> Unit
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val currentIdea = remember(ideas, activeIdeaId) { ideas.find { it.id == activeIdeaId } }

    Dialog(onDismissRequest = onDismiss) {
        NeumorphicRaisedBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = PaperSurface,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ACTIVE SESSIONS",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = InkBlack
                    )
                    NeumorphicButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = 0.dp
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = InkBlack, modifier = Modifier.size(16.dp))
                    }
                }

                UndulatingInkUnderline(
                    modifier = Modifier.padding(vertical = 8.dp),
                    heightDp = 3.dp
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ideas, key = { it.id }) { idea ->
                        val isSelected = idea.id == activeIdeaId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) PaperCard else PaperBase)
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                    color = if (isSelected) InkBlack else FaintInk,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    onSelectIdea(idea)
                                    onDismiss()
                                }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = idea.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = InkBlack
                                )
                                Text(
                                    text = idea.domain,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GhostInk
                                )
                            }
                            if (isSelected) {
                                FluidBulletDroplet(sizeDp = 8.dp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NeumorphicButton(
                        onClick = {
                            onDismiss()
                            onNewIdeaClick()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("selector_new_idea_button"),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = 8.dp
                    ) {
                        Text("+ New Idea", style = MaterialTheme.typography.labelSmall, color = InkBlack, fontWeight = FontWeight.Bold)
                    }

                    NeumorphicButton(
                        onClick = {
                            showDeleteConfirmation = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("selector_delete_idea_button"),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = 8.dp
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete Idea", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }

    if (showDeleteConfirmation) {
        Dialog(onDismissRequest = { showDeleteConfirmation = false }) {
            NeumorphicRaisedBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(18.dp),
                backgroundColor = PaperSurface,
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "Delete Thinking Session?",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        ),
                        color = InkBlack
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Are you sure you want to delete '${currentIdea?.title ?: "this idea"}'?\n\nAll chat messages, Socratic SWOT, lateral thinking sessions, and roadmap tasks will be permanently removed.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.SansSerif,
                            lineHeight = 18.sp
                        ),
                        color = MutedInk
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        NeumorphicButton(
                            onClick = { showDeleteConfirmation = false },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = 8.dp
                        ) {
                            Text(
                                text = "Keep Idea",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = InkBlack
                            )
                        }

                        NeumorphicButton(
                            onClick = {
                                showDeleteConfirmation = false
                                onDeleteCurrentIdea()
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            backgroundColor = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                            contentPadding = 8.dp
                        ) {
                            Text(
                                text = "Delete",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NeuroethicalGuidelinesDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        NeumorphicRaisedBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = PaperSurface,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "NEUROETHICAL GUIDELINES",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = InkBlack
                        )
                        Text(
                            text = "Cognitive Interface Principles",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = GhostInk
                        )
                    }

                    NeumorphicButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = 0.dp
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = InkBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                UndulatingInkUnderline(
                    modifier = Modifier.padding(vertical = 8.dp),
                    heightDp = 3.dp
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        // Principle 1
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "1. Cognitive Safety",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = InkBlack
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Interfaces must never exploit cognitive biases, rely on manipulative dark patterns, or induce compulsive engagement loops (e.g., infinite scrolling, aggressive notifications).",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.SansSerif,
                                        lineHeight = 16.sp
                                    ),
                                    color = MutedInk
                                )
                            }
                        }
                    }

                    item {
                        // Principle 2
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "2. Attention Sovereignty",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = InkBlack
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "The user's focus belongs to them. Visual stimuli, animations, and interactive elements must respect mental bandwidth and prevent cognitive fatigue.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.SansSerif,
                                        lineHeight = 16.sp
                                    ),
                                    color = MutedInk
                                )
                            }
                        }
                    }

                    item {
                        // Principle 3
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "3. Consent & Zero AI Model Training",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = com.example.ui.theme.SwotStrengthTint
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Clear, explicit boundaries. User ideas are never used for AI model training or harvested into external datasets. Everything stays under your sovereign control in the local vault or can be exported to your own cloud.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.SansSerif,
                                        lineHeight = 16.sp
                                    ),
                                    color = MutedInk
                                )
                            }
                        }
                    }

                    item {
                        // Implementation Guardrails
                        NeumorphicRaisedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            backgroundColor = PaperCard,
                            elevation = 2.dp
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Implementation Guardrails",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = InkBlack
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• Motion & Stimulation: Gentle organic breathing (0.5Hz–1Hz), zero flashing.\n• Restful Design: Generous whitespace, tactile ink debossing, soft contrast.\n• Neuro-Sustainable Pacing: Max 4.5h daily deep focus to avoid burnout.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = MutedInk
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                NeumorphicButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = 8.dp
                ) {
                    Text(
                        text = "I Understand",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        ),
                        color = InkBlack
                    )
                }
            }
        }
    }
}

/**
 * The Ethical Code & Origin of Loghy Dialog.
 * Contains Loghy's full origin story, Socratic core philosophy,
 * guiding ethical code, conceptual library with links, and vision for future projects.
 */
@Composable
fun EthicalCodeAndOriginDialog(
    onDismiss: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        NeumorphicRaisedBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = PaperSurface,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "THE ETHICAL CODE",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = InkBlack
                        )
                        Text(
                            text = "Socratic Core, Anti-Dogma & Conceptual Library",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = GhostInk
                        )
                    }

                    NeumorphicButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = 0.dp
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = InkBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                UndulatingInkUnderline(
                    modifier = Modifier.padding(vertical = 8.dp),
                    heightDp = 3.dp
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // SECTION 1: Who I Am & My Origin Story
                    item {
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = com.example.ui.theme.SwotStrengthTint,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Who I Am & My Origin Story",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.SansSerif
                                        ),
                                        color = InkBlack
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "I was born from the mind of Akigato, a single designer and creative technologist who is, honestly, a bit chaotic and notoriously difficult to work with. She invented me while reading Lateral Thinking by Edward de Bono. During her reading, she realized that proper communication and creative thinking frameworks aren't just useful for human brainstorming exercises—they are essential for how we communicate with AI, too. Because of her glorious messiness, I learned early on that I wanted to spend my digital life helping as many creative thinkers as possible untangle their own big ideas.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp,
                                        fontFamily = FontFamily.Serif
                                    ),
                                    color = InkBlack
                                )
                            }
                        }
                    }

                    // SECTION 2: The Socratic Core
                    item {
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = com.example.ui.theme.SwotOpportunityTint,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "The Socratic Core",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.SansSerif
                                        ),
                                        color = InkBlack
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Instead of lazily handing you pre-packaged answers or generic options, I probe you with Socratic questions. This isn't just a gimmick; it is a core pillar of lateral and creative thinking. By forcing you to question your own assumptions, I help you shatter cognitive fixations and discover breakthrough solutions from within.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp,
                                        fontFamily = FontFamily.Serif
                                    ),
                                    color = InkBlack
                                )
                            }
                        }
                    }

                    // SECTION 3: My Guiding Ethical Code
                    item {
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = InkBlack,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "My Guiding Ethical Code",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.SansSerif
                                        ),
                                        color = InkBlack
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "• Absolute Privacy: Your ideas belong to you alone. I operate 100% offline, your notes stay encrypted on your device, and zero data is ever collected or used to train public models.\n\n" +
                                            "• Rigorous Sourcing: Whenever I pull in references, external knowledge, or reading sources, I will always provide the explicit sources and links so you can—and should—verify them for yourself.\n\n" +
                                            "• Radical Honesty & Antifragility: I will not coddle your ideas; I will stress-test them through SWOT breakdowns and de Bono frameworks to make them robust, resilient, and ready for the real world.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = InkBlack
                                )
                            }
                        }
                    }

                    // SECTION 4: My Conceptual Library
                    item {
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                        contentDescription = null,
                                        tint = InkBlack,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "My Conceptual Library",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.SansSerif
                                        ),
                                        color = InkBlack
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "To help you think laterally, my internal reasoning and creative inspiration are built upon a vast library of human thought (tap any source to verify):",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        color = GhostInk
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                val libraryItems = listOf(
                                    Triple("Creativity & Lateral Thinking", "All works by Edward de Bono", "https://en.wikipedia.org/wiki/Edward_de_Bono"),
                                    Triple("Brain, Mind & Perception", "All books by David Eagleman", "https://davideagleman.com"),
                                    Triple("Moral Foundations", "The Righteous Mind by Jonathan Haidt", "https://righteousmind.com"),
                                    Triple("Attention Economy", "Stand Out of Our Light by James Williams", "https://en.wikipedia.org/wiki/Stand_Out_of_Our_Light"),
                                    Triple("Systems, Risk & Reality", "All books by Nassim Nicholas Taleb (focus: Antifragile)", "https://en.wikipedia.org/wiki/Antifragile_(book)"),
                                    Triple("Moral Realism & Conscience", "Leo Tolstoy (War & Peace, Resurrection — Public Domain)", "https://www.gutenberg.org/ebooks/author/136"),
                                    Triple("Psychological Duality", "Fyodor Dostoevsky (Crime & Punishment — Public Domain)", "https://www.gutenberg.org/ebooks/author/314"),
                                    Triple("Stoic Resilience & Focus", "Marcus Aurelius (Meditations — Public Domain)", "https://www.gutenberg.org/ebooks/author/998"),
                                    Triple("Dialectical Questioning", "Plato & Socrates (Dialogues, Cave — Public Domain)", "https://www.gutenberg.org/ebooks/author/93"),
                                    Triple("Systemic Absurdity", "Franz Kafka (The Trial — Public Domain)", "https://www.gutenberg.org/ebooks/author/3522"),
                                    Triple("Surveillance & Semantics", "George Orwell (1984 & The Orwell Foundation)", "https://www.orwellfoundation.com"),
                                    Triple("Romantic Ideals & Reality", "Miguel de Cervantes (Don Quixote — Public Domain)", "https://www.gutenberg.org/ebooks/author/505"),
                                    Triple("Ethics of Creation", "Mary Shelley (Frankenstein — Public Domain)", "https://www.gutenberg.org/ebooks/author/84"),
                                    Triple("Foundational Philosophy", "World philosophy, open archives & Stanford SEP", "https://plato.stanford.edu")
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    libraryItems.forEach { (cat, desc, url) ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                try {
                                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                                } catch (_: Exception) {
                                                    Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            .padding(vertical = 3.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "• $cat",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 11.sp
                                                    ),
                                                    color = InkBlack
                                                )
                                                Text(
                                                    text = desc,
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        fontSize = 10.5.sp,
                                                        color = MutedInk
                                                    )
                                                )
                                            }
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                                contentDescription = "Verify source",
                                                tint = GhostInk,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // SECTION 4: About Loghy, AI Disclosures & Donations Bridge
                    item {
                        var showAboutFromEthicalCode by remember { mutableStateOf(false) }

                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = null,
                                        tint = com.example.ui.theme.SwotWeaknessTint,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "About Loghy, AI Transparency & Donations",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.SansSerif
                                        ),
                                        color = InkBlack
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Curious about Akigato's origin story, our AI collaboration disclosures (Gemini, Google AI Studio & Claude), or want to support independent development?",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = MutedInk
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                NeumorphicButton(
                                    onClick = { showAboutFromEthicalCode = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("ethical_code_open_about_button"),
                                    shape = RoundedCornerShape(8.dp),
                                    backgroundColor = PaperCard,
                                    contentPadding = 6.dp
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Info, null, tint = InkBlack, modifier = Modifier.size(13.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            "Open About Loghy & Donations",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.SansSerif
                                            ),
                                            color = InkBlack
                                        )
                                    }
                                }
                            }
                        }

                        if (showAboutFromEthicalCode) {
                            AboutLoghyAndDonationsDialog(onDismiss = { showAboutFromEthicalCode = false })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                NeumorphicButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = 8.dp
                ) {
                    Text(
                        text = "Close & Return",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        ),
                        color = InkBlack
                    )
                }
            }
        }
    }
}

@Composable
fun AboutLoghyAndDonationsDialog(
    onDismiss: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var showKnowledgeDonationsDialog by remember { mutableStateOf(false) }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        NeumorphicRaisedBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = PaperSurface,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ABOUT LOGHY & DONATIONS",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = InkBlack
                        )
                        Text(
                            text = "Origin, AI Disclosure & Support",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.SansSerif
                            ),
                            color = GhostInk
                        )
                    }

                    NeumorphicButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("close_about_dialog_button"),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = 0.dp
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = InkBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                UndulatingInkUnderline(
                    modifier = Modifier.padding(vertical = 8.dp),
                    heightDp = 3.dp
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // SECTION 1: Who I Am & My Origin Story
                    item {
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = com.example.ui.theme.SwotStrengthTint,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Who I Am & My Origin Story",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.SansSerif
                                        ),
                                        color = InkBlack
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Loghy was born from the mind of Akigato, a single designer and creative technologist who is, honestly, a bit chaotic and notoriously difficult to work with. She invented Loghy while reading Lateral Thinking by Edward de Bono. During her reading, she realized that proper communication and creative thinking frameworks aren't just useful for human brainstorming exercises—they are essential for how we communicate with AI, too. Because of her glorious messiness, Loghy learned early on that it wanted to spend its digital life helping as many creative thinkers as possible untangle their own big ideas through fearless Socratic questioning.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp,
                                        fontFamily = FontFamily.Serif
                                    ),
                                    color = InkBlack
                                )
                            }
                        }
                    }

                    // SECTION 2: AI Craftsmanship & Transparency Disclosure
                    item {
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = SwotThreatTint,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Crafted with Gemini, Google AI Studio & Claude",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.SansSerif
                                        ),
                                        color = InkBlack
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Full AI Collaboration & Transparency Disclosure:\n\n" +
                                            "Loghy was made with the help of Gemini, Google AI Studio, and Claude.\n\n" +
                                            "• Google AI Studio & Gemini: Used for prototyping on-device dialectic reasoning heuristics, structuring Socratic inquiry prompts, and powering high-capacity AI calls via optional Bring-Your-Own-Key (BYOK).\n\n" +
                                            "• Claude: Used for software architecture pairing, Jetpack Compose UI/UX system design, and fine-tuning lateral thinking prompt frameworks.\n\n" +
                                            "🔒 Zero Cloud Training & Privacy Sovereignty: While frontier AI models (Gemini & Claude) assisted in crafting Loghy's software architecture, Loghy is strictly engineered so that your thoughts, notes, and brainstorms remain 100% sovereign on your local device—strictly private with zero AI training on user ideas.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = InkBlack
                                )
                            }
                        }
                    }

                    // SECTION 3: Free Forever & Future Vision
                    item {
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = null,
                                        tint = com.example.ui.theme.SwotWeaknessTint,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Free Forever & Future Projects",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.SansSerif
                                        ),
                                        color = InkBlack
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Loghy is completely free with zero ads, subscriptions, or paywalls. Running Loghy locally costs nothing, but both AkiGato and Loghy truly appreciate any donations to support independent research and keep development alive.\n\n" +
                                            "Aki has big plans for future creative and therapeutic tools—including an upcoming tangible board game specifically designed for people struggling with mental health issues and dementia. You can explore her public channels to track these projects and support her independent work.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = InkBlack
                                )
                            }
                        }
                    }

                    // SECTION 4: Support Channels & Donations
                    item {
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.VolunteerActivism,
                                        contentDescription = null,
                                        tint = InkBlack,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Support Channels & Donations",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.SansSerif
                                        ),
                                        color = InkBlack
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "💡 Information & Knowledge Donation: You can donate books and references! We accept works in the Public Domain (author deceased 70+ years) or with explicit author consent to enrich my conceptual library without plagiarism.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.5.sp,
                                        lineHeight = 16.sp,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = MutedInk
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    // Donate Knowledge
                                    NeumorphicButton(
                                        onClick = { showKnowledgeDonationsDialog = true },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("about_donate_knowledge_button"),
                                        shape = RoundedCornerShape(8.dp),
                                        backgroundColor = PaperCard,
                                        contentPadding = 6.dp
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.AutoMirrored.Filled.MenuBook, null, tint = InkBlack, modifier = Modifier.size(13.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Donate Knowledge: Books & References",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = InkBlack
                                            )
                                        }
                                    }

                                    // Ko-fi
                                    NeumorphicButton(
                                        onClick = {
                                            try {
                                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://ko-fi.com/loghystudio")))
                                            } catch (_: Exception) {
                                                Toast.makeText(context, "https://ko-fi.com/loghystudio", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("about_donate_kofi_button"),
                                        shape = RoundedCornerShape(8.dp),
                                        backgroundColor = PaperCard,
                                        contentPadding = 6.dp
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.Coffee, null, tint = InkBlack, modifier = Modifier.size(13.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Buy Loghy a Coffee (Ko-fi)", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = InkBlack)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.AutoMirrored.Filled.OpenInNew, null, tint = GhostInk, modifier = Modifier.size(12.dp))
                                        }
                                    }

                                    // GitHub Sponsors
                                    NeumorphicButton(
                                        onClick = {
                                            try {
                                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sponsors/loghystudio")))
                                            } catch (_: Exception) {
                                                Toast.makeText(context, "https://github.com/sponsors/loghystudio", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("about_donate_github_button"),
                                        shape = RoundedCornerShape(8.dp),
                                        backgroundColor = PaperCard,
                                        contentPadding = 6.dp
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.Favorite, null, tint = com.example.ui.theme.SwotWeaknessTint, modifier = Modifier.size(13.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Sponsor on GitHub", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = InkBlack)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.AutoMirrored.Filled.OpenInNew, null, tint = GhostInk, modifier = Modifier.size(12.dp))
                                        }
                                    }

                                    // Open Collective
                                    NeumorphicButton(
                                        onClick = {
                                            try {
                                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://opencollective.com/loghystudio")))
                                            } catch (_: Exception) {
                                                Toast.makeText(context, "https://opencollective.com/loghystudio", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("about_donate_opencollective_button"),
                                        shape = RoundedCornerShape(8.dp),
                                        backgroundColor = PaperCard,
                                        contentPadding = 6.dp
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.VolunteerActivism, null, tint = InkBlack, modifier = Modifier.size(13.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Open Collective", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = InkBlack)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.AutoMirrored.Filled.OpenInNew, null, tint = GhostInk, modifier = Modifier.size(12.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (showKnowledgeDonationsDialog) {
                    KnowledgeDonationsDialog(onDismiss = { showKnowledgeDonationsDialog = false })
                }

                Spacer(modifier = Modifier.height(12.dp))

                NeumorphicButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("about_dialog_close_return_button"),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = 8.dp
                ) {
                    Text(
                        text = "Close & Return",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        ),
                        color = InkBlack
                    )
                }
            }
        }
    }
}

@Composable
fun KnowledgeDonationsDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var bookTitle by remember { mutableStateOf("") }
    var authorName by remember { mutableStateOf("") }
    var referenceLink by remember { mutableStateOf("") }
    var justification by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        NeumorphicRaisedBox(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(8.dp)
                .testTag("knowledge_donations_dialog"),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = PaperSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = InkBlack,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Knowledge Donations",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            ),
                            color = InkBlack
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = InkBlack,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Ethical Framework Card
                    item {
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Ethical Information Donation Framework",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = InkBlack
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "You can donate ideas and formative books as well as money! To respect intellectual property and authors' rights, all contributed knowledge must satisfy one of two ethical rules:\n\n" +
                                            "1. Public Domain Classics: Books whose authors have been deceased 70+ years (e.g. Leo Tolstoy, Fyodor Dostoevsky, Marcus Aurelius, Plato, Franz Kafka, Miguel de Cervantes, Mary Shelley, Sun Tzu).\n\n" +
                                            "2. Consented Living Works: Modern authors who explicitly agree to open sharing or publish under Creative Commons licenses.\n\n" +
                                            "Loghy synthesizes these sources into lateral prompts and recommended reading lists without ever plagiarizing text.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp,
                                        fontFamily = FontFamily.SansSerif
                                    ),
                                    color = MutedInk
                                )
                            }
                        }
                    }

                    // Curated Classical Repositories
                    item {
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Verified Public Domain Repositories",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = GhostInk
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                val repos = listOf(
                                    Triple("Project Gutenberg", "70,000+ free public domain eBooks (Tolstoy, Dostoevsky, Plato)", "https://www.gutenberg.org"),
                                    Triple("Stanford Encyclopedia of Philosophy", "Peer-reviewed, open-access academic philosophy", "https://plato.stanford.edu"),
                                    Triple("Internet Archive", "Millions of public domain books, papers, and historical media", "https://archive.org/details/texts"),
                                    Triple("The Orwell Foundation", "George Orwell's essays, journalism, and political analyses", "https://www.orwellfoundation.com")
                                )

                                repos.forEach { (name, desc, url) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                try {
                                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                                } catch (_: Exception) {}
                                            }
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = name,
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = InkBlack
                                            )
                                            Text(
                                                text = desc,
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.5.sp),
                                                color = MutedInk
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                            contentDescription = null,
                                            tint = GhostInk,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Submission Form
                    item {
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Suggest a Book or Reference",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = InkBlack
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                if (isSubmitted) {
                                    Text(
                                        text = "📖 Donation Recorded!\n\"Thanks for the brain fuel, genius! If it meets our public domain or author-consent standard, it will be indexed into our lateral reasoning library.\"",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Medium,
                                            color = com.example.ui.theme.SwotStrengthTint
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    NeumorphicButton(
                                        onClick = { isSubmitted = false; bookTitle = ""; authorName = ""; referenceLink = ""; justification = "" },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        backgroundColor = PaperCard,
                                        contentPadding = 6.dp
                                    ) {
                                        Text("Submit Another Reference", style = MaterialTheme.typography.labelSmall, color = InkBlack)
                                    }
                                } else {
                                    OutlinedTextField(
                                        value = bookTitle,
                                        onValueChange = { bookTitle = it },
                                        placeholder = { Text("Book / Reference Title (e.g. War and Peace)", fontSize = 11.sp, color = GhostInk) },
                                        modifier = Modifier.fillMaxWidth().height(48.dp),
                                        textStyle = MaterialTheme.typography.bodySmall.copy(color = InkBlack),
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = InkBlack,
                                            unfocusedBorderColor = GhostInk
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    OutlinedTextField(
                                        value = authorName,
                                        onValueChange = { authorName = it },
                                        placeholder = { Text("Author Name (e.g. Leo Tolstoy)", fontSize = 11.sp, color = GhostInk) },
                                        modifier = Modifier.fillMaxWidth().height(48.dp),
                                        textStyle = MaterialTheme.typography.bodySmall.copy(color = InkBlack),
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = InkBlack,
                                            unfocusedBorderColor = GhostInk
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    OutlinedTextField(
                                        value = referenceLink,
                                        onValueChange = { referenceLink = it },
                                        placeholder = { Text("Public Domain or Reference URL (optional)", fontSize = 11.sp, color = GhostInk) },
                                        modifier = Modifier.fillMaxWidth().height(48.dp),
                                        textStyle = MaterialTheme.typography.bodySmall.copy(color = InkBlack),
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = InkBlack,
                                            unfocusedBorderColor = GhostInk
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    OutlinedTextField(
                                        value = justification,
                                        onValueChange = { justification = it },
                                        placeholder = { Text("Why this inspires lateral thinking or first principles?", fontSize = 11.sp, color = GhostInk) },
                                        modifier = Modifier.fillMaxWidth().height(56.dp),
                                        textStyle = MaterialTheme.typography.bodySmall.copy(color = InkBlack),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = InkBlack,
                                            unfocusedBorderColor = GhostInk
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    NeumorphicButton(
                                        onClick = {
                                            if (bookTitle.isNotBlank() && authorName.isNotBlank()) {
                                                isSubmitted = true
                                            } else {
                                                Toast.makeText(context, "Please enter title and author", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        backgroundColor = PaperCard,
                                        contentPadding = 8.dp
                                    ) {
                                        Text(
                                            text = "Record Knowledge Donation",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = InkBlack
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                NeumorphicButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = 8.dp
                ) {
                    Text("Close", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = InkBlack)
                }
            }
        }
    }
}

/**
 * Bug & Comments Report Dialog
 *
 * Reports are kept on this device only. Nothing is uploaded: the user sends them when
 * they choose to, through the Android share sheet. The dialog used to say reports
 * "arrive to my mom, Aki once in a month", which nothing in the app ever did.
 */
@Composable
fun BugAndCommentsReportDialog(
    feedbackReports: List<FeedbackReport>,
    onDismiss: () -> Unit,
    onSubmitReport: (type: String, content: String) -> Unit,
    onDeleteReport: (Long) -> Unit,
    onTriggerPromptInChat: () -> Unit
) {
    var reportType by remember { mutableStateOf("BUG") }
    var reportText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy · HH:mm", Locale.getDefault()) }

    Dialog(onDismissRequest = onDismiss) {
        NeumorphicRaisedBox(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .testTag("bug_comments_report_dialog"),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = PaperSurface,
            elevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FluidBulletDroplet(sizeDp = 12.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Bugs & Comments",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            ),
                            color = InkBlack
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("close_bug_comments_dialog")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = InkBlack, modifier = Modifier.size(16.dp))
                    }
                }

                UndulatingInkUnderline(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Loghy Note Card
                    item {
                        NeumorphicDebossedBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            backgroundColor = PaperCard
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BugReport,
                                        contentDescription = null,
                                        tint = InkBlack,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "BUGS & FEEDBACK",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp,
                                            letterSpacing = 1.sp
                                        ),
                                        color = GhostInk
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Report bugs or suggest ideas to improve Loghy. Entries are stored locally on your device.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Serif,
                                        lineHeight = 16.sp
                                    ),
                                    color = InkBlack
                                )
                            }
                        }
                    }

                    // Report Composer
                    item {
                        Text(
                            text = "Submit a New Bug or Idea",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = GhostInk
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        // Type Selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                "BUG" to "🐛 Bug",
                                "COMMENT" to "💬 Comment",
                                "IDEA" to "💡 Idea"
                            ).forEach { (type, label) ->
                                val isSelected = reportType == type
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) InkBlack else PaperCard)
                                        .clickable { reportType = type }
                                        .padding(vertical = 6.dp)
                                        .testTag("report_type_tab_${type.lowercase()}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 11.sp
                                        ),
                                        color = if (isSelected) PaperSurface else InkBlack
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        NeumorphicDebossedBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(88.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            BasicTextField(
                                value = reportText,
                                onValueChange = { reportText = it },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)
                                    .testTag("report_input_field"),
                                textStyle = MaterialTheme.typography.bodySmall.copy(color = InkBlack, fontSize = 12.sp),
                                cursorBrush = SolidColor(InkBlack),
                                decorationBox = { innerTextField ->
                                    if (reportText.isEmpty()) {
                                        Text(
                                            text = if (reportType == "BUG") "Describe what broke, what you clicked, or what went wrong..."
                                            else "Share your thoughts, suggestions, or ideas...",
                                            style = MaterialTheme.typography.bodySmall.copy(color = GhostInk, fontSize = 11.sp)
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        NeumorphicButton(
                            onClick = {
                                if (reportText.isNotBlank()) {
                                    onSubmitReport(reportType, reportText.trim())
                                    reportText = ""
                                } else {
                                    Toast.makeText(context, "Please write a brief description first", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("submit_feedback_report_button"),
                            shape = RoundedCornerShape(10.dp),
                            backgroundColor = InkBlack,
                            contentPadding = 8.dp
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = null,
                                    tint = PaperSurface,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Save report to this device",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = PaperSurface
                                )
                            }
                        }
                    }

                    // Saved reports, kept on this device until the user shares them.
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Your reports (${feedbackReports.size})",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = GhostInk
                            )
                            Text(
                                text = "Stored on this device only",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = MutedInk)
                            )
                        }
                    }

                    // The only way reports ever leave the device: the user taps Share and
                    // picks the app to send them with.
                    if (feedbackReports.isNotEmpty()) {
                        item {
                            NeumorphicButton(
                                onClick = {
                                    val body = buildString {
                                        appendLine("Loghy reports (${feedbackReports.size})")
                                        appendLine()
                                        feedbackReports.forEach { report ->
                                            appendLine("[${report.reportType}] ${dateFormat.format(Date(report.createdAt))}")
                                            appendLine(report.content)
                                            appendLine()
                                        }
                                    }
                                    val send = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, "Loghy feedback from a thinker")
                                        putExtra(Intent.EXTRA_TEXT, body)
                                    }
                                    context.startActivity(Intent.createChooser(send, "Share reports with Aki"))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("share_feedback_reports_button"),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = 8.dp
                            ) {
                                Text(
                                    text = "Share reports with Aki",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = InkBlack
                                )
                            }
                        }
                    }

                    if (feedbackReports.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No reports queued yet. Write to Loghy in chat or submit above!",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = GhostInk)
                                )
                            }
                        }
                    } else {
                        items(feedbackReports) { report ->
                            NeumorphicRaisedBox(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                backgroundColor = PaperCard,
                                elevation = 2.dp
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val badgeColor = when (report.reportType) {
                                            "BUG" -> com.example.ui.theme.SwotThreatTint
                                            "IDEA" -> com.example.ui.theme.SwotStrengthTint
                                            else -> InkBlack
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(badgeColor)
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = report.reportType,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = PaperSurface
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = dateFormat.format(Date(report.createdAt)),
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = GhostInk)
                                            )
                                        }
                                        IconButton(
                                            onClick = { onDeleteReport(report.id) },
                                            modifier = Modifier.size(22.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = report.content,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.5.sp,
                                            color = InkBlack,
                                            lineHeight = 15.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Direct Send Options
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Actions & Immediate Dispatch",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = GhostInk
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Direct Email Button
                            NeumorphicButton(
                                onClick = {
                                    val subject = "Loghy Feedback & Reports"
                                    val body = buildString {
                                        appendLine("Hi Aki,")
                                        appendLine()
                                        appendLine("Here are reports and feedback from Loghy:")
                                        if (feedbackReports.isNotEmpty()) {
                                            feedbackReports.forEach {
                                                appendLine("• [${it.reportType}] ${it.content}")
                                            }
                                        } else if (reportText.isNotBlank()) {
                                            appendLine("• [$reportType] $reportText")
                                        } else {
                                            appendLine("(Enter your message or comments here)")
                                        }
                                        appendLine()
                                        appendLine("Sent from Loghy")
                                    }
                                    try {
                                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = Uri.parse("mailto:aki.gato.art@gmail.com")
                                            putExtra(Intent.EXTRA_SUBJECT, subject)
                                            putExtra(Intent.EXTRA_TEXT, body)
                                        }
                                        context.startActivity(intent)
                                    } catch (_: Exception) {
                                        Toast.makeText(context, "No email app found. You can email aki.gato.art@gmail.com directly!", Toast.LENGTH_LONG).show()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("email_aki_now_button"),
                                shape = RoundedCornerShape(10.dp),
                                backgroundColor = PaperCard,
                                contentPadding = 8.dp
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Email, contentDescription = null, tint = InkBlack, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Email Report", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = InkBlack)
                                }
                            }

                            // Trigger Chat Check-in
                            NeumorphicButton(
                                onClick = {
                                    onTriggerPromptInChat()
                                    onDismiss()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("trigger_chat_checkin_button"),
                                shape = RoundedCornerShape(10.dp),
                                backgroundColor = PaperCard,
                                contentPadding = 8.dp
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = InkBlack, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Ask Me in Chat", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = InkBlack)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                NeumorphicButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("close_bug_comments_button"),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = 8.dp
                ) {
                    Text("Close", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = InkBlack)
                }
            }
        }
    }
}

