package com.utkusarican.customswipetodelete

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissListItems(
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = {
            Log.d("SwipeToDismissListItems", "positionalThreshold: $it")
            it / 2
        },
        confirmValueChange = {
            Log.d("SwipeToDismissListItems", "confirmValueChange: $it.")
            true
        }
    )
    val coroutineScope = rememberCoroutineScope()
    SwipeToDismissBox(
        modifier = modifier
            .clickable {
                coroutineScope.launch { dismissState.reset() }
            },
        enableDismissFromStartToEnd = false,
        state = dismissState,
        backgroundContent = {
            val color by
            animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> Color.LightGray
                    SwipeToDismissBoxValue.StartToEnd -> Color.Green
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                }, label = ""
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
            )
        }
    ) {
        OutlinedCard(shape = RectangleShape) {
            ListItem(
                modifier = Modifier.clickable {
                    coroutineScope.launch { dismissState.reset() }
                },
                headlineContent = { Text("Cupcake") },
                supportingContent = { Text("Swipe me left or right!") }
            )
        }
    }
}

enum class DragAnchors {
    Start,
    End,
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnchoredDraggableBox(
    modifier: Modifier = Modifier
) {
    val localDensity = LocalDensity.current

    val decayAnimation = rememberSplineBasedDecay<Float>()

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(localDensity) { 100.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimation
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    DragAnchors.Start at 0f
                    DragAnchors.End at 400f
                }
            )
        }
    }
    Log.d("AnchoredDraggableBox", "Offset: ${state.requireOffset()}")
    Box(
        Modifier
            .size(48.dp)
            .background(Color.Red)
            .offset {
                IntOffset(
                    // 2
                    x = state
                        .requireOffset()
                        .roundToInt(),
                    y = 0,
                )
            }
            .anchoredDraggable(
                state = state,
                orientation = Orientation.Horizontal
            )
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun HorizontalDraggableSample(
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val decayAnimation = rememberSplineBasedDecay<Float>()
    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimation
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    DragAnchors.Start at 0f
                    DragAnchors.End at 400f
                }
            )
        }
    }

    Box(
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            modifier = Modifier
                .size(80.dp)
                .offset {
                    IntOffset(
                        x = state
                            .requireOffset()
                            .roundToInt(), y = 0
                    )
                }
                .anchoredDraggable(state, Orientation.Horizontal),
            contentDescription = null,
        )
    }
}

