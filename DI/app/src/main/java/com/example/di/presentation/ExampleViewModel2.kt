package com.example.di.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.di.domain.ExampleUseCase
import com.example.di.domain.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel

class ExampleViewModel2 @Inject constructor(
    private val exampleUseCase: ExampleUseCase,
) : ViewModel() {

    fun exampleMethod(item: Item) {
        Log.d("ExampleTest", "ExampleViewModel2 invoke $item")
        exampleUseCase(item)
    }
}
