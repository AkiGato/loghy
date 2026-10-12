package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.FaintInk
import com.example.ui.theme.HairlineInk
import com.example.ui.theme.PaperBase
import com.example.ui.theme.PaperCard
import com.example.ui.theme.PaperDebossed
import com.example.ui.theme.PaperSurface
import com.example.ui.theme.ShadowDark
import com.example.ui.theme.ShadowDarkSubtle
import com.example.ui.theme.ShadowLight

/**
 * Raised Neumorphic Paper Surface with twin directional lighting:
 * - Top-left specular bloom (White/Cream)
 * - Bottom-right diffuse shadow (Warm Ink Charcoal)
 */
@Composable
fun NeumorphicRaisedBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = PaperSurface,
    elevation: Dp = 4.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .drawBehind {
                val elevationPx = elevation.toPx()
                // Top-left specular light
                drawRoundRect(
                    color = ShadowLight,
                    topLeft = Offset(-elevationPx * 0.75f, -elevationPx * 0.75f),
                    size = size,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
                )
                // Bottom-right diffuse dark shadow
                drawRoundRect(
                    color = ShadowDark,
                    topLeft = Offset(elevationPx * 0.85f, elevationPx * 0.85f),
                    size = size,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
                )
            }
            .clip(shape)
            .background(backgroundColor)
            .border(0.75.dp, HairlineInk, shape),
        content = content
    )
}

/**
 * Debossed / Recessed letterpress tray with inner shadow illusion.
 */
@Composable
fun NeumorphicDebossedBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = PaperDebossed,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .drawBehind {
                // Inset top-left shadow gradient
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(ShadowDarkSubtle, Color.Transparent),
                        start = Offset(0f, 0f),
                        end = Offset(12.dp.toPx(), 12.dp.toPx())
                    ),
                    size = size
                )
                // Inset bottom-right subtle light glow
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Transparent, ShadowLight),
                        start = Offset(size.width - 12.dp.toPx(), size.height - 12.dp.toPx()),
                        end = Offset(size.width, size.height)
                    ),
                    size = size
                )
            }
            .border(1.dp, FaintInk, shape),
        content = content
    )
}

/**
 * Tactile Mechanical Button that depresses on press.
 */
@Composable
fun NeumorphicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(14.dp),
    backgroundColor: Color = PaperSurface,
    contentPadding: Dp = 12.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = tween(durationMillis = 100),
        label = "button_scale"
    )

    val currentBg = if (isPressed) PaperCard else backgroundColor

    Box(
        modifier = modifier
            .scale(scale)
            .drawBehind {
                val elevationPx = if (isPressed) 1.dp.toPx() else 3.5.dp.toPx()
                if (!isPressed) {
                    // Specular highlight top-left
                    drawRoundRect(
                        color = ShadowLight,
                        topLeft = Offset(-elevationPx * 0.7f, -elevationPx * 0.7f),
                        size = size,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(14.dp.toPx())
                    )
                    // Diffuse shadow bottom-right
                    drawRoundRect(
                        color = ShadowDark,
                        topLeft = Offset(elevationPx * 0.8f, elevationPx * 0.8f),
                        size = size,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(14.dp.toPx())
                    )
                }
            }
            .clip(shape)
            .background(currentBg)
            .border(0.75.dp, if (isPressed) FaintInk else HairlineInk, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
        content = content
    )
}
