package com.example.di.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.di.domain.ExampleUseCase
import com.example.di.domain.Item
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = ExampleViewModel.Factory::class)
class ExampleViewModel @AssistedInject constructor(
    private val exampleUseCase: ExampleUseCase,
    @Assisted("item") private val item: Item
) : ViewModel() {
    fun exampleMethod() {
        Log.d("ExampleTest", "ËxampleUseCase invoke $item")
        exampleUseCase(item)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("item") item: Item
        ): ExampleViewModel
    }
}

// Manual assisted injection
//class ExampleViewModelFactory @Inject constructor(
//    private val exampleUseCase: ExampleUseCase
//) {
//    fun create(item: Item): ExampleViewModel {
//        return ExampleViewModel(exampleUseCase, item)
//    }
//}
