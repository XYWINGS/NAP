package com.example.di.di

import android.content.Context
import com.example.di.data.Database
import com.example.di.data.ExampleRepositoryImpl
import com.example.di.domain.ExampleUseCase
import com.example.di.presentation.ExampleViewModel
import com.example.di.presentation.MainActivity

class Component(context: Context) {
    val database = Database(context)
    val repository = ExampleRepositoryImpl(database)
    val exampleUseCase = ExampleUseCase(repository)

    /*If you want an object to be created each time it accessed set a getter like this*/
    val exampleViewModel get() = ExampleViewModel(exampleUseCase)

    fun inject(mainActivity: MainActivity) {
        mainActivity.exampleViewModel = exampleViewModel
    }
}
