package com.example.di.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.di.domain.ExampleUseCase
import com.example.di.domain.Item
import javax.inject.Inject

class ExampleViewModel @Inject constructor(
    private val exampleUseCase: ExampleUseCase, private val item: Item
) : ViewModel() {
    fun exampleMethod() {
        Log.d("ExampleTest", "ËxampleUseCase invoke $item")
        exampleUseCase(item)
    }
}

class ExampleViewModelFactory @Inject constructor(
    private val exampleUseCase: ExampleUseCase
) {
    fun create(item: Item): ExampleViewModel {
        return ExampleViewModel(exampleUseCase, item)
    }
}
