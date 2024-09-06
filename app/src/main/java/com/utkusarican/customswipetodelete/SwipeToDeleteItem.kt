package com.utkusarican.customswipetodelete

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


fun Modifier.swipeToDismiss(
    onDismissed: (viewSize: Int) -> Unit,
    offsetXCallBack: (x: Float, viewSize: Int) -> Unit,
    removeDeleteItem: () -> Unit,
    maxWidthOfDeleteItem: () -> Unit
): Modifier = composed {
    val offsetX = remember { Animatable(0f) }
    val localDensity = LocalDensity
    pointerInput(Unit) {
        // Used to calculate fling decay.
        val decay = splineBasedDecay<Float>(this)
        // Use suspend functions for touch events and the Animatable.
        coroutineScope {
            while (true) {
                val velocityTracker = VelocityTracker()
                // Stop any ongoing animation.
                offsetX.stop()
                awaitPointerEventScope {
                    // Detect a touch down event.
                    val pointerId = awaitFirstDown().id

                    horizontalDrag(pointerId) { change ->
                        // Update the animation value with touch events.
                        offsetXCallBack(offsetX.value + change.positionChange().x, size.width)
                        launch {
                            if (offsetX.value >= 0f && offsetX.value + change.positionChange().x > localDensity.run { 40.dp.toPx() }) {
                            } else {
                                offsetX.snapTo(
                                    offsetX.value + change.positionChange().x
                                )
                            }
                        }

                        velocityTracker.addPosition(
                            change.uptimeMillis,
                            change.position
                        )
                    }
                }
                // No longer receiving touch events. Prepare the animation.
                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(
                    offsetX.value,
                    velocity
                )
                // The animation stops when it reaches the bounds.
                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat()
                )
                launch {
                    if (targetOffsetX <= -size.width) {
                        launch { offsetX.animateDecay(velocity, decay) }
                        launch { onDismissed(size.width) }

                    } else {
                        if (offsetX.value.absoluteValue > (size.width * 0.70)) {
                            launch {
                                offsetX.animateTo(
                                    targetValue = -size.width.toFloat(),
                                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                                )
                            }
                            launch { onDismissed(size.width) }


                        } else if (offsetX.value.absoluteValue < (size.width * 0.70) && offsetX.value.absoluteValue < localDensity.run { 80.dp.toPx() }) {
                            removeDeleteItem()
                            offsetX.animateTo(
                                targetValue = 0f,
                                initialVelocity = velocity
                            )
                        } else if (offsetX.value.absoluteValue < (size.width * 0.70) && localDensity.run { offsetX.value.absoluteValue.toDp() } >= 80.dp) {
                            maxWidthOfDeleteItem()
                            offsetX.animateTo(
                                targetValue = -localDensity.run { 120.dp.toPx() },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy
                                )
                            )
                        } else {
                            launch { removeDeleteItem() }
                            launch {
                                offsetX.animateTo(
                                    targetValue = 0f,
                                    initialVelocity = velocity
                                )
                            }
                        }
                    }
                }
            }
        }
    }
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
}

