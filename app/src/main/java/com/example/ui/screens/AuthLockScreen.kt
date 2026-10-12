package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FluidBulletDroplet
import com.example.ui.components.FluidCreature
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicDebossedBox
import com.example.ui.components.NeumorphicRaisedBox
import com.example.ui.components.UndulatingInkUnderline
import com.example.ui.theme.FaintInk
import com.example.ui.theme.GhostInk
import com.example.ui.theme.InkBlack
import com.example.ui.theme.PaperBase
import com.example.ui.theme.PaperSurface

@Composable
fun AuthLockScreen(
    thinkerName: String,
    pinInput: String,
    pinError: String?,
    isSettingPin: Boolean,
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onSetupPinSubmit: (String) -> Unit = {}
) {
    var setupFirstEntry by remember { mutableStateOf("") }
    var setupStep by remember { mutableStateOf(1) } // 1: enter new, 2: confirm

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PaperBase)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 440.dp)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            // Companion Fluid Avatar
            FluidCreature(
                sizeDp = 90.dp,
                onTap = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "LOGHY VAULT",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                color = InkBlack
            )

            UndulatingInkUnderline(
                modifier = Modifier.width(140.dp),
                heightDp = 4.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isSettingPin) {
                    if (setupStep == 1) "Create 4-Digit Security PIN" else "Confirm Your 4-Digit PIN"
                } else {
                    "Welcome back, $thinkerName. Enter PIN to unlock."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = GhostInk,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 4 Pin Droplet Indicators
            NeumorphicDebossedBox(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(64.dp)
                    .fillMaxWidth(0.7f),
                shape = RoundedCornerShape(32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until 4) {
                        val isFilled = i < pinInput.length
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .size(18.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 1.5.dp,
                                    color = if (isFilled) InkBlack else FaintInk,
                                    shape = CircleShape
                                )
                                .background(if (isFilled) InkBlack else PaperSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isFilled) {
                                FluidBulletDroplet(sizeDp = 10.dp, tint = InkBlack)
                            }
                        }
                    }
                }
            }

            if (!pinError.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = pinError,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Tactile Neumorphic Keypad
            val keyRows = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("LOCK", "0", "DEL")
            )

            Column(
                modifier = Modifier.width(280.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                keyRows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        row.forEach { key ->
                            when (key) {
                                "DEL" -> {
                                    NeumorphicButton(
                                        onClick = onDeleteClick,
                                        modifier = Modifier
                                            .size(72.dp)
                                            .testTag("pin_key_delete"),
                                        shape = RoundedCornerShape(36.dp),
                                        contentPadding = 0.dp
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                                            contentDescription = "Delete digit",
                                            tint = InkBlack,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                "LOCK" -> {
                                    NeumorphicRaisedBox(
                                        modifier = Modifier.size(72.dp),
                                        shape = RoundedCornerShape(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Lock indicator",
                                            tint = GhostInk,
                                            modifier = Modifier
                                                .size(22.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                }
                                else -> {
                                    NeumorphicButton(
                                        onClick = {
                                            if (isSettingPin) {
                                                if (pinInput.length < 4) {
                                                    onDigitClick(key)
                                                    if (pinInput.length + 1 == 4) {
                                                        val entered = pinInput + key
                                                        if (setupStep == 1) {
                                                            setupFirstEntry = entered
                                                            setupStep = 2
                                                            // Clear for step 2
                                                            // Wait for user to confirm
                                                        } else {
                                                            if (entered == setupFirstEntry) {
                                                                onSetupPinSubmit(entered)
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                onDigitClick(key)
                                            }
                                        },
                                        modifier = Modifier
                                            .size(72.dp)
                                            .testTag("pin_key_$key"),
                                        shape = RoundedCornerShape(36.dp),
                                        contentPadding = 0.dp
                                    ) {
                                        Text(
                                            text = key,
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = InkBlack
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
