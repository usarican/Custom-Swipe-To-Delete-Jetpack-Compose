package com.utkusarican.customswipetodelete

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun SwipeContainerView(
    modifier: Modifier = Modifier,
    childHeight: Dp,
    onDismissAction : () -> Unit,
    view: @Composable () -> Unit,
) {
    val deleteButtonAlphaAnimation = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    var deleteViewWidth by remember { mutableStateOf(0.dp) }
    val deleteCardWidthAnimation = remember { Animatable(0f) }
    val localDensity = LocalDensity.current
    var boxWidth by remember { mutableFloatStateOf(0f) }
    var removeDeleteItem by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .swipeToDismiss(
                    offsetXCallBack = { x, width ->
                        boxWidth = width.toFloat()
                        coroutineScope.launch {
                            when {
                                x.absoluteValue <= localDensity.run { 40.dp.toPx() } -> {
                                    deleteButtonAlphaAnimation.animateTo(0f)
                                }

                                x.absoluteValue >= localDensity.run { 40.dp.toPx() } && x.absoluteValue <= localDensity.run { 120.dp.toPx() } -> {
                                    when {
                                        x.absoluteValue >= localDensity.run { 40.dp.toPx() } && x.absoluteValue <= localDensity.run { 60.dp.toPx() } -> {
                                            deleteButtonAlphaAnimation.animateTo(0.2f)
                                        }

                                        x.absoluteValue >= localDensity.run { 60.dp.toPx() } && x.absoluteValue <= localDensity.run { 80.dp.toPx() } -> {
                                            deleteButtonAlphaAnimation.animateTo(0.4f)
                                        }

                                        x.absoluteValue >= localDensity.run { 80.dp.toPx() } && x.absoluteValue <= localDensity.run { 100.dp.toPx() } -> {
                                            deleteButtonAlphaAnimation.animateTo(0.6f)
                                        }

                                        x.absoluteValue >= localDensity.run { 100.dp.toPx() } && x.absoluteValue <= localDensity.run { 120.dp.toPx() } -> {
                                            deleteButtonAlphaAnimation.animateTo(0.8f)
                                        }
                                    }
                                }

                                else -> {
                                    deleteButtonAlphaAnimation.animateTo(1f)
                                }
                            }
                        }
                        coroutineScope.launch {
                            when {
                                x.absoluteValue <= localDensity.run { 48.dp.toPx() } -> {
                                    deleteCardWidthAnimation.animateTo(0f)
                                }

                                x.absoluteValue >= localDensity.run { 48.dp.toPx() } -> {
                                    deleteCardWidthAnimation.animateTo(
                                        x.absoluteValue
                                    )
                                }

                                else -> {
                                    deleteCardWidthAnimation.animateTo(1f)
                                }
                            }
                        }
                    },
                    onDismissed = {
                        coroutineScope.launch {
                            deleteCardWidthAnimation.animateTo(it.toFloat())
                            removeDeleteItem = true
                            onDismissAction()
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
        AnimatedVisibility(
            modifier = Modifier
                .onGloballyPositioned {
                    deleteViewWidth = localDensity.run { it.size.width.toDp() }
                }
                .height(childHeight)
                .alpha(deleteButtonAlphaAnimation.value)
                .width(localDensity.run { deleteCardWidthAnimation.value.toDp() })
                .align(Alignment.CenterEnd)
                .padding(horizontal = 8.dp),
            visible = !removeDeleteItem
        ) {
            Card(
                onClick = {
                    coroutineScope.launch {
                        removeDeleteItem = true
                        onDismissAction()
                    }
                },
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Delete",
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}