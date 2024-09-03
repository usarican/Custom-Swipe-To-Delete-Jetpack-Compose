package com.utkusarican.customswipetodelete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(20){ item ->
                Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Red
                        )

                    ) {
                        Text(text = "Delete")
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .swipeToDismiss { },
                    ) {
                        Text(text = "Item $item")
                    }
                }
                /*SwipeToDeleteItem {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                    ) {
                        Text(text = "Item $item", modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Right
                        )
                    }
                }*/
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview(){
    MainScreen()
}