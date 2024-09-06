package com.utkusarican.customswipetodelete

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun SwipeContainerView(
    modifier: Modifier = Modifier,
    view: @Composable () -> Unit
) {
    val deleteButtonAlphaAnimation = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    var deleteViewWidth by remember { mutableStateOf(0.dp) }
    var deleteCardWidthAnimation = remember { Animatable(0f) }
    val localDensity = LocalDensity.current
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .swipeToDismiss(
                    deleteItemWidth = deleteViewWidth,
                    onDismissed = {
                        coroutineScope.launch { deleteCardWidthAnimation.animateTo(it.toFloat()) }

                    },
                    offsetXCallBack = { x, width ->
                        coroutineScope.launch {
                            when {
                                x.absoluteValue <= localDensity.run { 80.dp.toPx() } -> {
                                    deleteButtonAlphaAnimation.animateTo(0f)
                                }

                                x.absoluteValue >= localDensity.run { 80.dp.toPx() } && x <= localDensity.run { 120.dp.toPx() } -> {
                                    deleteButtonAlphaAnimation.animateTo(x.absoluteValue / localDensity.run { 120.dp.toPx() })
                                }

                                else -> {
                                    deleteButtonAlphaAnimation.animateTo(1f)
                                }
                            }
                        }
                        coroutineScope.launch {
                            when {
                                x.absoluteValue <= localDensity.run { 80.dp.toPx() } -> {
                                    deleteCardWidthAnimation.animateTo(0f)
                                }

                                x.absoluteValue >= localDensity.run { 80.dp.toPx() } && x.absoluteValue <= localDensity.run { 120.dp.toPx() } -> {
                                    deleteCardWidthAnimation.animateTo(
                                        minOf(
                                            localDensity.run { 120.dp.toPx() },
                                            x.absoluteValue
                                        )
                                    )
                                }

                                x.absoluteValue > width * 0.4 -> {
                                    deleteCardWidthAnimation.animateTo(localDensity.run { localDensity.run { x.absoluteValue } - localDensity.run { 16.dp.toPx() } })
                                }

                                else -> {
                                    deleteCardWidthAnimation.animateTo(1f)
                                }
                            }
                        }
                    },
                    removeDeleteItem = {
                        coroutineScope.launch {
                            launch { deleteButtonAlphaAnimation.animateTo(0f) }
                            launch { deleteCardWidthAnimation.animateTo(0f) }
                        }
                    },
                    maxWidthOfDeleteItem = {
                        coroutineScope.launch {
                            deleteCardWidthAnimation.animateTo(localDensity.run { 120.dp.toPx() })
                        }

                    }
                )
        ) {
            view()
        }
        Card(
            onClick = { TODO() },
            modifier = Modifier
                .onGloballyPositioned {
                    deleteViewWidth = localDensity.run { it.size.width.toDp() }
                }
                .height(56.dp)
                .alpha(deleteButtonAlphaAnimation.value)
                .width(localDensity.run { deleteCardWidthAnimation.value.toDp() })
                .align(Alignment.CenterEnd)
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .height(64.dp)
                    .width(120.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Delete",
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ContainerView(
    modifier: Modifier = Modifier,
    view: @Composable () -> Unit
) {

}