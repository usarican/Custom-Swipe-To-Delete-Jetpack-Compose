package com.utkusarican.customswipetodelete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.utkusarican.customswipetodelete.ui.theme.CustomSwipeToDeleteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomSwipeToDeleteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var childViewHeight by remember { mutableStateOf(0.dp) }
                    val localDensity = LocalDensity.current
                    SwipeContainerView(
                        modifier = Modifier.padding(innerPadding),
                        childHeight = childViewHeight,
                    ){
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .height(32.dp)
                                .onGloballyPositioned {
                                    childViewHeight = localDensity.run { it.size.height.toDp() }
                                }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Swipe to delete",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.wrapContentSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
