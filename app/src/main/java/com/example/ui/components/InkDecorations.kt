package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.FaintInk
import com.example.ui.theme.GhostInk
import com.example.ui.theme.InkBlack
import com.example.ui.theme.NotePaperTones
import com.example.ui.theme.ShadowDark
import kotlin.math.sin

/**
 * Living Droplet Bullet Point:
 * Organic ink bead marking Socratic takeaways.
 */
@Composable
fun FluidBulletDroplet(
    modifier: Modifier = Modifier,
    sizeDp: Dp = 12.dp,
    tint: Color = InkBlack
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val radius = size.minDimension / 2f
        val center = Offset(size.width / 2f, size.height / 2f)

        // Soft micro ground shadow
        drawCircle(
            color = ShadowDark.copy(alpha = 0.35f),
            center = Offset(center.x, center.y + 1.2.dp.toPx()),
            radius = radius * 0.9f
        )

        // Viscous droplet gradient
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(tint, tint.copy(alpha = 0.85f)),
                center = Offset(center.x - radius * 0.25f, center.y - radius * 0.25f),
                radius = radius
            ),
            center = center,
            radius = radius
        )

        // Specular core light refraction
        drawCircle(
            color = Color.White.copy(alpha = 0.45f),
            center = Offset(center.x - radius * 0.3f, center.y - radius * 0.35f),
            radius = radius * 0.25f
        )
    }
}

/**
 * Undulating Ink Underline:
 * Calligraphic wet ink stroke under section headers.
 */
@Composable
fun UndulatingInkUnderline(
    modifier: Modifier = Modifier,
    heightDp: Dp = 6.dp,
    color: Color = InkBlack,
    strokeWidth: Dp = 1.8.dp
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(heightDp)
    ) {
        val path = Path()
        val width = size.width
        val midY = size.height / 2f

        path.moveTo(0f, midY)
        var x = 0f
        val step = 14f
        while (x <= width) {
            val y = midY + (sin(x * 0.045f) * (size.height * 0.35f))
            path.lineTo(x, y)
            x += step
        }
        path.lineTo(width, midY)

        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}

/**
 * Paper Palette Picker:
 * Choose across 7 tactile tones for vault notes.
 */
@Composable
fun PaperPalettePicker(
    selectedToneIndex: Int,
    onToneSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NotePaperTones.forEachIndexed { index, toneColor ->
            val isSelected = index == selectedToneIndex
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(toneColor)
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) InkBlack else FaintInk,
                        shape = CircleShape
                    )
                    .clickable { onToneSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    FluidBulletDroplet(sizeDp = 8.dp)
                }
            }
        }
    }
}
