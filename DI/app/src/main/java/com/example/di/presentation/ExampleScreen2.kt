package com.example.di.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.di.domain.Item


@Composable
fun ExampleScreen2(
    modifier: Modifier = Modifier, exampleViewModel2: ExampleViewModel2 = hiltViewModel()
) {
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), onClick = {
                exampleViewModel2.exampleMethod(Item(2))
            }) { Text("Click Meeeeeeeee") }
    }
}
