package com.example.di.domain

import android.util.Log
import com.example.di.data.ExampleRepositoryImpl
import javax.inject.Inject

class ExampleUseCase @Inject constructor(
//    private val repository: ExampleRepository
    private val repository: ExampleRepositoryImpl

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

/*In hilt if we need constructor injection we add the inject annotation to the constructor, if we need
* field injection add the inject annotation to the fields*/
