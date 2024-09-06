package com.utkusarican.customswipetodelete



import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var visibleToDeleteView by remember {
        mutableStateOf(false)
    }
    var deleteCardWidth by remember { mutableStateOf(0.dp) }
    var deleteCardAlpha by remember { mutableStateOf(0f) }
    val localDensity = LocalDensity.current
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .offset { IntOffset(offsetX.roundToInt(), 0) }
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            offsetX += delta
                            deleteCardWidth = if (offsetX.absoluteValue > localDensity.run { 32.dp.toPx() })
                                localDensity.run { (offsetX.absoluteValue).toDp() } else 0.dp
                            deleteCardAlpha = if (offsetX.absoluteValue > localDensity.run { 32.dp.toPx() }) kotlin.math.min(
                                1f,offsetX.absoluteValue / 500f)
                                 else 0f
                            if (localDensity.run { offsetX.absoluteValue.toDp() } > 48.dp) {
                                visibleToDeleteView = true
                            }
                        }
                    )) {
                Text(
                    text = "Swipe to delete",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

            }
            androidx.compose.animation.AnimatedVisibility(
                visible = visibleToDeleteView,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Card(
                    onClick = { TODO() },
                    modifier = Modifier
                        .width(deleteCardWidth)
                        .alpha(deleteCardAlpha)
                        .fillMaxHeight()
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    Text(
                        text = "Delete",
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}