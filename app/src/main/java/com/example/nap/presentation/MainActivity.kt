package com.example.nap.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.nap.presentation.ui.theme.NAPTheme
import kotlin.text.iterator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NAPTheme {
                val name = "XYWINGS".filter { it.isLetter() }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Magenta),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (char in name) {
                        Greeting(
                            name = char.toString(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(
        modifier = Modifier.background(color = Color.Green),
        text = name,
        color = Color.Blue,
        letterSpacing = 24.sp
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NAPTheme {
        Greeting("Android")
    }
}
