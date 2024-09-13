package com.utkusarican.customswipetodelete

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
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
    MediumOne,
    MediumTwo,
    MediumThree,
    End,
}


@Composable
@OptIn(ExperimentalFoundationApi::class)
fun HorizontalDraggableSample(
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val decayAnimation = rememberSplineBasedDecay<Float>()
    var totalDistance by remember { mutableFloatStateOf(0f) }
    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            positionalThreshold = { distance: Float ->
                distance},
            velocityThreshold = { with(density) { 200.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimation
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    DragAnchors.Start at 0f
                    DragAnchors.MediumOne at 200f
                    DragAnchors.MediumTwo at 400f
                    DragAnchors.MediumThree at 600f
                    DragAnchors.End at totalDistance
                }
            )
        }
    }

    Log.d("AnchoredDraggableBox", "Offset: ${state.currentValue}")
    Box(
        modifier = modifier,
    ) {
        Card(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        x = state
                            .requireOffset()
                            .roundToInt(), y = 0
                    )
                }
                .anchoredDraggable(state, Orientation.Horizontal),
            colors = CardDefaults.cardColors(
                containerColor = Color.Blue
            )
        ){
            Box(modifier = Modifier
                .size(64.dp)
                .background(Color.LightGray))
        }
    }
}

