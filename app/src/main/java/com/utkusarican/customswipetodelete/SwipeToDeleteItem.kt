package com.utkusarican.customswipetodelete

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun SwipeToDeleteItem(
    foregroundView : @Composable (modifier : Modifier) -> Unit,
){
    Box(
        modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize()){
        BackgroundCard(backgroundColor = Color.Red)
        foregroundView(
            modifier = Modifier.swipeToDismiss {
                Log.d("SwipeToDeleteItem", "Dismissed")
            }
        )
    }
}

@Composable
fun BackgroundCard(
    backgroundColor : Color
){
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
    ) {
        Text(text = "Delete", modifier = Modifier
            .fillMaxSize()
            .align(Alignment.CenterHorizontally), textAlign = TextAlign.Right)
    }
}

fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    val offsetX = remember { Animatable(0f) }
    pointerInput(Unit) {
        Log.d("SwipeToDeleteItem", "View width ${size.width}")
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
                        launch {
                            offsetX.snapTo(
                                offsetX.value + change.positionChange().x
                            )
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
                    Log.d("SwipeToDeleteItem", "targetOffsetX: $targetOffsetX")
                    if (targetOffsetX.absoluteValue >= 400f){
                        offsetX.animateTo(
                            targetValue = -400f,
                            initialVelocity = velocity
                        )
                    }
                    else if (targetOffsetX.absoluteValue <= size.width) {
                        // Not enough velocity; Slide back.
                        offsetX.animateTo(
                            targetValue = 0f,
                            initialVelocity = velocity
                        )
                    } else {
                        // The element was swiped away.
                        offsetX.animateDecay(velocity, decay)
                        onDismissed()
                    }
                }
            }
        }
    }
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
}
