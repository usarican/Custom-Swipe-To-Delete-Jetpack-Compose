package com.utkusarican.customswipetodelete



import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
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
                    .fillMaxSize()
                    .offset { IntOffset(offsetX.roundToInt(), 0) }
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            offsetX += delta
                            visibleToDeleteView = if (offsetX > 16.dp.value) true else false
                            if (offsetX <= 56.dp.value){
                                deleteCardWidth += offsetX.dp
                            }
                        }
                    ),
                onClick = { /*TODO*/ }) {
                Text(
                    text = "Swipe to delete",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

            }
            this@Column.AnimatedVisibility(visible = visibleToDeleteView) {
                Card(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.align(Alignment.CenterStart)
                        .width(deleteCardWidth)
                        .fillMaxHeight()
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    Text(
                        text = "Delete",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
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