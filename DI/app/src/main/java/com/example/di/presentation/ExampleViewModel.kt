package com.example.di.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.di.domain.ExampleUseCase
import com.example.di.domain.Item

class ExampleViewModel(
    private val exampleUseCase: ExampleUseCase
) : ViewModel() {
    fun exampleMethod(item: Item) {
        Log.d("ExampleTest", "ËxampleUseCase invoke $item")
        exampleUseCase(item)
    }
}
