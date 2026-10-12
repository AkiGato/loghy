package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BrainstormIdea
import com.example.data.model.SwotAnalysis
import com.example.ui.components.FluidBulletDroplet
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicRaisedBox
import com.example.ui.components.UndulatingInkUnderline
import com.example.ui.theme.GhostInk
import com.example.ui.theme.InkBlack
import com.example.ui.theme.MutedInk
import com.example.ui.theme.PaperBase
import com.example.ui.theme.PaperSurface
import com.example.ui.theme.SwotOpportunityTint
import com.example.ui.theme.SwotStrengthTint
import com.example.ui.theme.SwotThreatTint
import com.example.ui.theme.SwotWeaknessTint

@Composable
fun SwotStudioScreen(
    activeIdea: BrainstormIdea?,
    swot: SwotAnalysis?,
    onRefreshSwot: () -> Unit,
    onCaptureTakeaway: (title: String, content: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBase)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SOCRATIC SWOT",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = InkBlack
                )
                Text(
                    text = "Four-quadrant reality check",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = GhostInk
                )
            }

            NeumorphicButton(
                onClick = onRefreshSwot,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("refresh_swot_button"),
                shape = RoundedCornerShape(22.dp),
                contentPadding = 0.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Re-evaluate SWOT",
                    tint = InkBlack,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        UndulatingInkUnderline(
            modifier = Modifier.padding(vertical = 8.dp),
            heightDp = 4.dp
        )

        if (swot == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
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
                            text = "No SWOT analysis yet",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            ),
                            color = InkBlack,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap refresh above to run a dialectic reality check on your active idea.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedInk,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SwotSectionCard(
                        angleTitle = "WHAT HOLDS UP",
                        subtext = "Fundamental Strengths & Structural Advantages",
                        items = swot.whatHoldsUp,
                        accentTint = SwotStrengthTint,
                        onCaptureItem = { item ->
                            onCaptureTakeaway("Holds Up: ${activeIdea?.title}", item)
                        }
                    )
                }

                item {
                    SwotSectionCard(
                        angleTitle = "VULNERABLE SPOTS",
                        subtext = "Fatal Weaknesses & Friction Points",
                        items = swot.vulnerableSpots,
                        accentTint = SwotWeaknessTint,
                        onCaptureItem = { item ->
                            onCaptureTakeaway("Vulnerability: ${activeIdea?.title}", item)
                        }
                    )
                }

                item {
                    SwotSectionCard(
                        angleTitle = "OUTSIDE HURDLES",
                        subtext = "Market Threats & Competitive Headwinds",
                        items = swot.outsideHurdles,
                        accentTint = SwotThreatTint,
                        onCaptureItem = { item ->
                            onCaptureTakeaway("Hurdle: ${activeIdea?.title}", item)
                        }
                    )
                }

                item {
                    SwotSectionCard(
                        angleTitle = "HIDDEN UPSIDE",
                        subtext = "Asymmetric Opportunities & Ignored Niches",
                        items = swot.hiddenUpside,
                        accentTint = SwotOpportunityTint,
                        onCaptureItem = { item ->
                            onCaptureTakeaway("Upside: ${activeIdea?.title}", item)
                        }
                    )
                }

                if (swot.ethicsAndNeuroSustainability.isNotBlank()) {
                    item {
                        EthicsAndNeuroSustainabilityCard(
                            ethicsText = swot.ethicsAndNeuroSustainability,
                            onCapture = {
                                onCaptureTakeaway("Ethics & Neuro-Sustainability: ${activeIdea?.title}", swot.ethicsAndNeuroSustainability)
                            }
                        )
                    }
                }

                item {
                    // Actionable Fixes & Socratic Inquiries
                    SwotSectionCard(
                        angleTitle = "ACTIONABLE TESTS & FIXES",
                        subtext = "Pragmatic steps to falsify vulnerabilities this week",
                        items = swot.actionableFixes,
                        accentTint = InkBlack,
                        onCaptureItem = { item ->
                            onCaptureTakeaway("Action Test: ${activeIdea?.title}", item)
                        }
                    )
                }

                item {
                    // Probing Socratic Inquiries
                    SwotSectionCard(
                        angleTitle = "SOCRATIC PROBES",
                        subtext = "Challenging core assumptions to avoid delusion",
                        items = swot.probingQuestions,
                        accentTint = InkBlack,
                        onCaptureItem = { item ->
                            onCaptureTakeaway("Socratic Probe: ${activeIdea?.title}", item)
                        }
                    )
                }

                item {
                    // Relevant Reading Sources
                    SwotSectionCard(
                        angleTitle = "RELEVANT READING & MODELS",
                        subtext = "Tested mental models and literature from innovators",
                        items = swot.readingSources,
                        accentTint = GhostInk,
                        onCaptureItem = { item ->
                            onCaptureTakeaway("Model Source: ${activeIdea?.title}", item)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun SwotSectionCard(
    angleTitle: String,
    subtext: String,
    items: List<String>,
    accentTint: Color,
    onCaptureItem: (String) -> Unit
) {
    NeumorphicRaisedBox(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = PaperSurface,
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FluidBulletDroplet(sizeDp = 10.dp, tint = accentTint)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = angleTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = InkBlack
                    )
                }
            }

            Text(
                text = subtext,
                style = MaterialTheme.typography.bodySmall,
                color = MutedInk,
                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
            )

            UndulatingInkUnderline(
                heightDp = 2.dp,
                color = accentTint.copy(alpha = 0.25f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            items.forEachIndexed { index, bullet ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCaptureItem(bullet) }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "${index + 1}.",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = accentTint,
                        modifier = Modifier.width(20.dp)
                    )

                    Text(
                        text = bullet,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = 22.sp,
                            fontFamily = FontFamily.Serif
                        ),
                        color = InkBlack,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { onCaptureItem(bullet) },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = "Save to Vault",
                            tint = accentTint.copy(alpha = 0.8f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EthicsAndNeuroSustainabilityCard(
    ethicsText: String,
    onCapture: () -> Unit
) {
    NeumorphicRaisedBox(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = PaperSurface,
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFF2E7D32), RoundedCornerShape(5.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "ETHICS & NEURO-SUSTAINABILITY",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = InkBlack
                        )
                        Text(
                            text = "Cognitive Well-being · Anti-Dopamine Hijacking · Humane Marketing",
                            style = MaterialTheme.typography.bodySmall,
                            color = GhostInk
                        )
                    }
                }

                IconButton(
                    onClick = onCapture,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkAdd,
                        contentDescription = "Capture ethics insight",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2E7D32).copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = ethicsText,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = InkBlack
                )
            }
        }
    }
}
