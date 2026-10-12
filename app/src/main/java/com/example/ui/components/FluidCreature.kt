package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.InkBlack
import com.example.ui.theme.PaperBase
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class CreatureState {
    IDLE,
    THINKING,
    SPEAKING,
    LISTENING,
    CURIOUS,
    EXCITED
}

enum class GazeDirection {
    CENTER,
    LOOK_DOWN,       // Focused down toward the keyboard while typing
    LOOK_UP_RIGHT,   // Looking at thought bubble to the right
    LOOK_UP_LEFT     // Looking at thought bubble to the left
}

/**
 * Centered Fluid Creature (Loghy):
 * An organic ink entity rendered with cubic Bézier spline interpolation
 * across 8 radial control points, undulating with harmonic sine waves.
 * Features realistic ink physics, dynamic gaze direction, and lively spinning/bouncing
 * when in THINKING state.
 */
@Composable
fun FluidCreature(
    modifier: Modifier = Modifier,
    sizeDp: Dp = 104.dp,
    state: CreatureState = CreatureState.IDLE,
    gazeDirection: GazeDirection = GazeDirection.CENTER,
    audioLevel: Float = 0f,
    onTap: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "fluid_creature_transition")
    val breathingPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "breathing_phase"
    )

    // Dynamic spin while thinking
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "creature_spin"
    )

    // Dynamic bounce vertical oscillation while thinking
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -18f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 260, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "creature_bounce"
    )

    // Eye blink animation: blinks every 4 seconds
    val blinkFactor by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                1f at 0
                1f at 3700
                0.06f at 3850
                1f at 4000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "creature_blink"
    )

    // Jiggle impulse on tap
    val jiggleImpulse = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .size(sizeDp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    coroutineScope.launch {
                        jiggleImpulse.snapTo(1f)
                        jiggleImpulse.animateTo(
                            targetValue = 0f,
                            animationSpec = spring(dampingRatio = 0.35f, stiffness = 400f)
                        )
                    }
                    onTap()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseRadius = size.minDimension * 0.36f

            // Dynamic perturbation based on state and audio
            val perturbationFactor = when (state) {
                CreatureState.THINKING -> 0.16f
                CreatureState.SPEAKING -> 0.16f + (audioLevel * 0.18f)
                CreatureState.LISTENING -> 0.10f + (audioLevel * 0.22f)
                CreatureState.CURIOUS -> 0.14f
                CreatureState.EXCITED -> 0.22f
                CreatureState.IDLE -> 0.06f
            } + (jiggleImpulse.value * 0.28f)

            drawFluidInkDrop(
                center = center,
                baseRadius = baseRadius,
                phase = breathingPhase,
                perturbation = perturbationFactor,
                jiggle = jiggleImpulse.value,
                state = state,
                gazeDirection = gazeDirection,
                spinAngle = if (state == CreatureState.THINKING) spinAngle else 0f,
                bounceOffsetY = if (state == CreatureState.THINKING) bounceOffset else 0f,
                blinkFactor = blinkFactor
            )
        }
    }
}

private fun DrawScope.drawFluidInkDrop(
    center: Offset,
    baseRadius: Float,
    phase: Float,
    perturbation: Float,
    jiggle: Float,
    state: CreatureState,
    gazeDirection: GazeDirection,
    spinAngle: Float,
    bounceOffsetY: Float,
    blinkFactor: Float
) {
    val pointsCount = 8
    val points = ArrayList<Offset>(pointsCount)

    for (i in 0 until pointsCount) {
        val angle = (i * 2 * PI / pointsCount).toFloat()
        val harmonic1 = sin(phase + i * 0.9f)
        val harmonic2 = cos(phase * 1.5f + i * 1.3f)
        val jiggleOffset = sin(jiggle * 12f + i) * (jiggle * baseRadius * 0.3f)

        val dynamicRadius = baseRadius * (1f + (harmonic1 * 0.05f + harmonic2 * 0.03f) * (perturbation / 0.06f)) + jiggleOffset
        val px = center.x + dynamicRadius * cos(angle)
        val py = center.y + dynamicRadius * sin(angle)
        points.add(Offset(px, py))
    }

    // Closed smooth cubic Bézier spline
    val path = Path()
    if (points.isNotEmpty()) {
        path.moveTo(
            (points[0].x + points[pointsCount - 1].x) / 2f,
            (points[0].y + points[pointsCount - 1].y) / 2f
        )
        for (i in 0 until pointsCount) {
            val current = points[i]
            val next = points[(i + 1) % pointsCount]
            val midX = (current.x + next.x) / 2f
            val midY = (current.y + next.y) / 2f
            path.quadraticTo(current.x, current.y, midX, midY)
        }
        path.close()
    }

    // Body and eyes: rotate and bounce together in mid-air
    val bouncePx = bounceOffsetY.dp.toPx()
    withTransform({
        translate(top = bouncePx)
        rotate(degrees = spinAngle, pivot = center)
    }) {
        // Solid Ink Black Body
        drawPath(path = path, color = InkBlack, style = Fill)

        // Companion Eyes
        val eyeOffsetX = baseRadius * 0.26f
        val (gazeOffsetX, gazeOffsetY) = when (gazeDirection) {
            GazeDirection.LOOK_DOWN -> Pair(0f, 5.5.dp.toPx())
            GazeDirection.LOOK_UP_RIGHT -> Pair(4.5.dp.toPx(), -4.5.dp.toPx())
            GazeDirection.LOOK_UP_LEFT -> Pair(-4.5.dp.toPx(), -4.5.dp.toPx())
            GazeDirection.CENTER -> Pair(0f, 0f)
        }

        val eyeBaseY = center.y - baseRadius * 0.05f + (sin(phase) * 2f)
        val eyeY = eyeBaseY + gazeOffsetY

        val baseSquash = when {
            state == CreatureState.THINKING -> 0.35f
            gazeDirection == GazeDirection.LOOK_DOWN -> 0.52f
            state == CreatureState.SPEAKING -> 0.9f
            state == CreatureState.LISTENING -> 1.1f
            state == CreatureState.CURIOUS -> 1.3f
            state == CreatureState.EXCITED -> 1.4f
            else -> 0.7f
        }
        val eyeSquash = (baseSquash * blinkFactor).coerceAtLeast(0.06f)

        // Left Eye
        drawOval(
            color = PaperBase,
            topLeft = Offset(center.x - eyeOffsetX + gazeOffsetX - 3.5.dp.toPx(), eyeY - (4.dp.toPx() * eyeSquash)),
            size = Size(7.dp.toPx(), 8.dp.toPx() * eyeSquash)
        )

        // Right Eye
        drawOval(
            color = PaperBase,
            topLeft = Offset(center.x + eyeOffsetX + gazeOffsetX - 3.5.dp.toPx(), eyeY - (4.dp.toPx() * eyeSquash)),
            size = Size(7.dp.toPx(), 8.dp.toPx() * eyeSquash)
        )
    }
}
