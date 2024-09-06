package com.utkusarican.customswipetodelete

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SwipeContainerView(
    modifier: Modifier = Modifier,
    view: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .swipeToDismiss { }
        ) {
            view()
        }
        Card(
            onClick = { TODO() },
            modifier = Modifier
                .height(56.dp)
                .wrapContentWidth()
                .align(Alignment.CenterEnd)
                .padding(horizontal = 8.dp)
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

@Composable
fun ContainerView(
    modifier: Modifier = Modifier,
    view: @Composable () -> Unit
) {

}