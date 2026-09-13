package com.example.di.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.di.domain.ExampleUseCase
import com.example.di.domain.Item
import javax.inject.Inject

class ExampleViewModel @Inject constructor(
    private val exampleUseCase: ExampleUseCase
) : ViewModel() {
    fun exampleMethod(item: Item) {
        Log.d("ExampleTest", "ËxampleUseCase invoke $item")
        exampleUseCase(item)
    }
}
