package com.example.di.domain

import android.util.Log

class ExampleUseCase(
    private val repository: ExampleRepository
) {

    operator fun invoke(item: Item) {
        Log.d("ExampleUseCase", "Invoke example method $item")
        repository.exampleMethod(item)
    }
}

/*A component class or a model or a function should not create its own dependencies. They must be provided from the outside
*
* This will violate that rule since it creates its own dependency
* class ExampleUseCase(
) {
    private val repository: ExampleRepository

    operator fun invoke(item: Item) {
        repository.exampleMethod()
    }
}
* */
