package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Psychology
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BrainstormIdea
import com.example.data.model.LateralSession
import com.example.engine.SocraticBrain
import com.example.ui.components.FluidBulletDroplet
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicDebossedBox
import com.example.ui.components.NeumorphicRaisedBox
import com.example.ui.components.UndulatingInkUnderline
import com.example.ui.theme.GhostInk
import com.example.ui.theme.InkBlack
import com.example.ui.theme.MutedInk
import com.example.ui.theme.PaperBase
import com.example.ui.theme.PaperCard
import com.example.ui.theme.PaperSurface

private data class HatItem(
    val name: String,
    val color: Color,
    val subtitle: String
)

@Composable
fun LateralThinkingScreen(
    activeIdea: BrainstormIdea?,
    activeStimulus: Pair<String, String>?,
    history: List<LateralSession>,
    onTriggerRandomWord: () -> Unit,
    onTriggerProvocation: () -> Unit,
    onTriggerReversal: () -> Unit,
    onTriggerHat: (Int) -> Unit,
    onSaveReflection: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var reflectionText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBase)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "LATERAL THINKING",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = InkBlack
            )
            Text(
                text = "Edward de Bono creative prompts",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = GhostInk
            )

            UndulatingInkUnderline(
                modifier = Modifier.padding(vertical = 8.dp),
                heightDp = 4.dp
            )
        }

        // Lateral Method Triggers Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NeumorphicButton(
                    onClick = onTriggerRandomWord,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("lateral_random_word_button"),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = 8.dp
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = "Random Word",
                            tint = InkBlack,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Random Word",
                            style = MaterialTheme.typography.labelSmall,
                            color = InkBlack,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                NeumorphicButton(
                    onClick = onTriggerProvocation,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("lateral_provocation_button"),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = 8.dp
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Provocation PO",
                            tint = InkBlack,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Provoke PO",
                            style = MaterialTheme.typography.labelSmall,
                            color = InkBlack,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                NeumorphicButton(
                    onClick = onTriggerReversal,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("lateral_reversal_button"),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = 8.dp
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Loop,
                            contentDescription = "Reversal",
                            tint = InkBlack,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Reversal",
                            style = MaterialTheme.typography.labelSmall,
                            color = InkBlack,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Six Thinking Hats Quick Selector Row
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SIX THINKING HATS (DE BONO)",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp, fontWeight = FontWeight.Bold),
                        color = GhostInk
                    )
                    Text(
                        text = "Select a lens",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = MutedInk)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val hats = listOf(
                        HatItem("White", Color(0xFFE0DDD5), "Facts"),
                        HatItem("Red", Color(0xFFD32F2F), "Gut"),
                        HatItem("Black", Color(0xFF2E3338), "Caution"),
                        HatItem("Yellow", Color(0xFFE5A823), "Upside"),
                        HatItem("Green", Color(0xFF388E3C), "Growth"),
                        HatItem("Blue", Color(0xFF1976D2), "Process")
                    )
                    hats.forEachIndexed { index, hat ->
                        NeumorphicRaisedBox(
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onTriggerHat(index) }
                                .testTag("hat_button_${hat.name.lowercase()}"),
                            shape = RoundedCornerShape(10.dp),
                            elevation = 2.dp,
                            backgroundColor = PaperSurface
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 4.dp, horizontal = 2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(hat.color)
                                        .border(0.5.dp, InkBlack.copy(alpha = 0.2f), CircleShape)
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = hat.name,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = InkBlack
                                )
                                Text(
                                    text = hat.subtitle,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 8.5.sp,
                                        color = GhostInk
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Stimulus Exercise Card
        if (activeStimulus != null) {
            item {
                NeumorphicRaisedBox(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    backgroundColor = PaperSurface,
                    elevation = 5.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = activeStimulus.first.uppercase(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = InkBlack
                            )
                            FluidBulletDroplet(sizeDp = 10.dp)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = activeStimulus.second,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = FontFamily.Serif,
                                lineHeight = 22.sp
                            ),
                            color = InkBlack
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Reflection Input
                        NeumorphicDebossedBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(84.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            BasicTextField(
                                value = reflectionText,
                                onValueChange = { reflectionText = it },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)
                                    .testTag("lateral_reflection_field"),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = InkBlack),
                                cursorBrush = SolidColor(InkBlack),
                                decorationBox = { innerTextField ->
                                    if (reflectionText.isEmpty()) {
                                        Text(
                                            text = "Write your lateral connection or idea here...",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MutedInk
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            NeumorphicButton(
                                onClick = {
                                    if (reflectionText.isNotBlank()) {
                                        onSaveReflection(reflectionText)
                                        reflectionText = ""
                                    }
                                },
                                modifier = Modifier.testTag("save_lateral_reflection_button"),
                                shape = RoundedCornerShape(12.dp),
                                enabled = reflectionText.isNotBlank(),
                                contentPadding = 8.dp
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = InkBlack,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Commit to Vault",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = InkBlack,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // History of Lateral Breakthroughs
        item {
            Text(
                text = "RECORDED LATERAL BREAKTHROUGHS",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp, fontWeight = FontWeight.Bold),
                color = GhostInk
            )
        }

        if (history.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    NeumorphicRaisedBox(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        backgroundColor = PaperSurface
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            FluidBulletDroplet(sizeDp = 14.dp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No lateral reflections yet",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = InkBlack,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Choose a prompt above (Random Word, Provocation, Reversal, or 6 Hats) to challenge assumptions.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedInk,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        } else {
            items(history, key = { it.id }) { item ->
                NeumorphicRaisedBox(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    backgroundColor = PaperCard,
                    elevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FluidBulletDroplet(sizeDp = 6.dp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.technique,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = InkBlack
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.userReflection,
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Serif),
                            color = InkBlack
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
